package org.usfirst.frc.team4276.robot;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriverCameraThread extends Thread implements Runnable {

	private static int camNumber = 0;

	// Microsoft Lifecam HD-3000 diagonal field of view is 68.5 degrees
	// private static final double DRIVERCAM_HORIZ_FOV_DEGREES = 61.0;
	// private static final int DRIVERCAM_FRAMES_PER_SECOND = 30;
	private static final int DRIVERCAM_IMG_WIDTH = 424;
	private static final int DRIVERCAM_IMG_HEIGHT = 240;

	// Enum for "set" camera properties
	public static final int CAP_PROP_FPS = 5;
	public static final int CAP_PROP_FRAME_WIDTH = 3;
	public static final int CAP_PROP_FRAME_HEIGHT = 4;
	public static final int CAP_PROP_BRIGHTNESS = 10;
	public static final int CAP_PROP_CONTRAST = 11;
	public static final int CAP_PROP_SATURATION = 12;
	public static final int CAP_PROP_HUE = 13;
	public static final int CAP_PROP_EXPOSURE = 15;
	public static final int CAP_PROP_AUTO_EXPOSURE = 21;

	private int _driverCameraFrameSequence = 0;

	public synchronized int driverCameraFrameSequence() {
		return _driverCameraFrameSequence;
	}

	private synchronized void incr_driverCameraFrameSequence() {
		_driverCameraFrameSequence++;
	}

	public DriverCameraThread(int nCamera) {
		camNumber = nCamera;
		setDaemon(true);
	}

	void matRotate(Mat matImage, int rotflag) {
		// 1=CW, 2=CCW, 3=180
		switch (rotflag) {
		case 1:
			Core.transpose(matImage, matImage);
			Core.flip(matImage, matImage, 1); // transpose+flip(1)=CW
			break;

		case 2:
			Core.transpose(matImage, matImage);
			Core.flip(matImage, matImage, 0); // transpose+flip(0)=CCW
			break;

		case 3:
			Core.flip(matImage, matImage, -1); // flip(-1)=180
			break;

		default:
		}
	}

	@Override
	public void run() {

		CvSource outputStreamStd = CameraServer.getInstance().putVideo("cam FWD", DRIVERCAM_IMG_WIDTH,
				DRIVERCAM_IMG_HEIGHT);
		VideoCapture camDRIVER = new VideoCapture(camNumber);

		// camDRIVER.set(CAP_PROP_FPS, DRIVERCAM_FRAMES_PER_SECOND);
		// camDRIVER.set(CAP_PROP_AUTO_EXPOSURE, 1);
		camDRIVER.set(CAP_PROP_FRAME_WIDTH, DRIVERCAM_IMG_WIDTH);
		camDRIVER.set(CAP_PROP_FRAME_HEIGHT, DRIVERCAM_IMG_HEIGHT);

		Mat frame = new Mat();
		camDRIVER.read(frame);
		while (true) {
			if (camDRIVER.read(frame)) {
				incr_driverCameraFrameSequence();
				
				matRotate(frame, 1);
				outputStreamStd.putFrame(frame);

				SmartDashboard.putNumber("camDRIVER frame#", driverCameraFrameSequence());
			} else {
				SmartDashboard.putString("camDRIVER frame#", "*** Not Connected ***");
				Timer.delay(1.0);
			}
		}
	}
}
