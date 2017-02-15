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

	// The VEX Spike relay used to control turntable position
	// can open or close no more often than 20 times per second.
	//
	// This control code may start the turntable moving as often as the vision
	// camera frame rate, and stop it again between camera frames, so even if
	// the camera can run faster we should limit the frame rate to 10 per
	// second.
	private static final int GRIPCAM_FRAMES_PER_SECOND = 10;
	private static final int PIXYCAM_FRAMES_PER_SECOND = 10;

	// Microsoft Lifecam HD-3000 diagonal field of view is 68.5 degrees
	private static final double CAM_HORIZ_FOV_DEGREES = 61.0;
	private static final int CAM_IMG_WIDTH = 160;
	private static final int CAM_IMG_HEIGHT = 120;

	private VisionThread visionThread;
	private final Object imgLock = new Object();

	// Using a relay to control turntable position means it stops abruptly,
	// blurring the camera image.
	// This delay is intended to let the vibration settle down so we get a clean
	// image before we decide how to move the turntable next.
	// If the robot isn't moving we expect to be in the dead zone and the
	// turntable should not need to move.
	private static final int NUMBER_OF_FRAMES_BEFORE_CHANGE_SPIN_MODE = 2;
	private int delayCountForSpinModeChange = NUMBER_OF_FRAMES_BEFORE_CHANGE_SPIN_MODE;

	public GripPipeline myGripPipeline;
	public static boolean isValidGripCameraCenterX = false;
	public static double gripCameraCenterX = 0.0;
	
	public int gripCameraExposure = 80;
	public int gripCameraFrameSequence = 0;

	public GripVisionThread() {

		CvSource outputStreamStd = CameraServer.getInstance().putVideo("Gray", CAM_IMG_WIDTH, CAM_IMG_HEIGHT);
		UsbCamera camGRIP = CameraServer.getInstance().startAutomaticCapture("GRIPcam", 0);
		//camGRIP.setExposureManual(89);
		//camGRIP.setFPS(GRIPCAM_FRAMES_PER_SECOND);
		//camGRIP.setResolution(CAM_IMG_WIDTH, CAM_IMG_HEIGHT);

		//UsbCamera camPIXY = CameraServer.getInstance().startAutomaticCapture("PIXYcam", 1);
		//camPIXY.setExposureAuto();
		//camPIXY.setFPS(PIXYCAM_FRAMES_PER_SECOND);
		
		//UsbCamera camDriver = CameraServer.getInstance().startAutomaticCapture("Drivercam", 2);
		//camDriver.setExposureAuto();
		//camDriver.setResolution(CAM_IMG_WIDTH, CAM_IMG_HEIGHT);

		SmartDashboard.putString("debug4", "Before ctor");
		myGripPipeline = new GripPipeline();
		SmartDashboard.putString("debug4", "after ctor1");
		visionThread = new VisionThread(camGRIP, myGripPipeline, pipeline -> {

			gripCameraFrameSequence++;
			Mat frame = myGripPipeline.blurInput;
			SmartDashboard.putString("debug5", "after cap");

			if (pipeline.findContoursOutput().isEmpty()) {
				isValidGripCameraCenterX = false;
			} else {
				SmartDashboard.putString("debug5", "valid");
				Rect emptyRect = new Rect();
				Rect rLargest = findLargestContour(pipeline.findContoursOutput());
				if (rLargest == emptyRect) {
=			} else {
					synchronized (imgLock) {
						gripCameraCenterX = rLargest.x + (rLargest.width / 2);
						SmartDashboard.putNumber("gripCameraCenterX_000", gripCameraCenterX);

						isValidGripCameraCenterX = true;

						Scalar colorGreen = new Scalar(0, 255, 0);

						// Find midpoints of the 4 sides of the rectangle, and
						// draw
						// from those points to the center
						Point pt0 = new Point(rLargest.x, rLargest.y);
						Point pt1 = new Point(rLargest.x + rLargest.width, rLargest.y);
						Point pt2 = new Point(rLargest.x, rLargest.y + rLargest.height);
						Point pt3 = new Point(rLargest.x + rLargest.width, rLargest.y + rLargest.height);

						Imgproc.line(frame, pt0, pt3, colorGreen, 2);
						Imgproc.line(frame, pt1, pt2, colorGreen, 2);
					}
				}
			}
			outputStreamStd.putFrame(frame);
			SmartDashboard.putNumber("camGRIP frame#", gripCameraFrameSequence);
			if (!Robot.isBoilerTrackerEnabled) {
				Robot.turntable1.spinMode = LidarSpin.SpinMode.IDLE;
			} else if (GripVisionThread.isValidGripCameraCenterX) {
				if (Robot.turntable1.spinMode == LidarSpin.SpinMode.SCAN) {
					delayCountForSpinModeChange = NUMBER_OF_FRAMES_BEFORE_CHANGE_SPIN_MODE;
					Robot.turntable1.spinMode = LidarSpin.SpinMode.IDLE;
				}
				if (delayCountForSpinModeChange-- <= 0) {
					Robot.turntable1.spinMode = LidarSpin.SpinMode.FIXED_OFFSET_FROM_YAW;
				}
			} else {
				// !isValidGripCameraCenterX
				if (Robot.turntable1.spinMode == LidarSpin.SpinMode.FIXED_OFFSET_FROM_YAW) {
					delayCountForSpinModeChange = NUMBER_OF_FRAMES_BEFORE_CHANGE_SPIN_MODE;
					Robot.turntable1.spinMode = LidarSpin.SpinMode.IDLE;
				}
				if (delayCountForSpinModeChange-- <= 0) {
					Robot.turntable1.spinMode = LidarSpin.SpinMode.SCAN;
				}
			}

			Robot.boilerTracker.visionUpdate();
			Robot.turntable1.spinnerex();
		});
	}

	public void start() {
		visionThread.start();
	}

	public static double degreesOffCenterX() {

		// Calculate desired yaw angle so the vision target would be in the
		// center of the frame
		double degreesPerPixel = CAM_HORIZ_FOV_DEGREES / CAM_IMG_WIDTH;
		double pixelsOffCenter = gripCameraCenterX - (CAM_IMG_WIDTH / 2);
		return pixelsOffCenter / degreesPerPixel;
	}

	private Rect findLargestContour(ArrayList<MatOfPoint> listContours) {
		Rect rRet = new Rect();
		for (int i = 0; i < listContours.size(); i++) {
			Rect r = Imgproc.boundingRect(listContours.get(i));
			if (rRet.area() < r.area()) {
				rRet = r;
			}
		}
		return rRet;
	}

}
