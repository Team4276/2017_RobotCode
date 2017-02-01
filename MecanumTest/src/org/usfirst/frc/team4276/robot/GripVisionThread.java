package org.usfirst.frc.team4276.robot;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class GripVisionThread {

	// GRIP vision camera
	private static final int GRIPCAM_IMG_WIDTH = 320;
	private static final int GRIPCAM_IMG_HEIGHT = 240;
	private VisionThread visionThread;
	private final Object imgLock = new Object();
	double gripCameraCenterX = 0.0;

	public GripVisionThread() {
		CvSource outputStreamStd = CameraServer.getInstance().putVideo("Gray", GRIPCAM_IMG_WIDTH, GRIPCAM_IMG_HEIGHT);		
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture("GRIPcam", 0);
		camera.setExposureManual(0);
		camera.setFPS(10);
		
		visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
			if (!pipeline.findContoursOutput().isEmpty()) {
				Rect rLargest = findLargestContour(pipeline.findContoursOutput());
				synchronized (imgLock) {
					gripCameraCenterX = rLargest.x + (rLargest.width / 2);
					SmartDashboard.putNumber("gripCameraCenterX_000", gripCameraCenterX);
					
					Mat frm = GripPipeline.blurInput;
					Scalar colorGreen = new Scalar(0, 255, 0);
					
					// Find midpoints of the 4 sides of the rectangle, and draw from those points to the center
			        Point pt0 = new Point(rLargest.x, rLargest.y);
			        Point pt1 = new Point(rLargest.x+rLargest.width, rLargest.y);
			        Point pt2 = new Point(rLargest.x, rLargest.y+rLargest.x+rLargest.height);
			        Point pt3 = new Point(rLargest.x+rLargest.width, rLargest.y+rLargest.height);
			        		
			        Imgproc.line(frm, pt0, pt3, colorGreen, 3, 4, 0);
			        Imgproc.line(frm, pt1, pt2, colorGreen, 3, 4, 0);
					outputStreamStd.putFrame(frm);
				}
			}
		});
	}

	public void start() {
		visionThread.start();
	}
	
	private Rect findLargestContour(ArrayList<MatOfPoint> listContours) {
		Rect rRet = new Rect();
		for(int i=0; i<listContours.size(); i++) {
			Rect r = Imgproc.boundingRect(listContours.get(i));		
			if(rRet.area() < r.area()) {
				rRet = r;	
			}
		}		
		return rRet;	
	}

}
