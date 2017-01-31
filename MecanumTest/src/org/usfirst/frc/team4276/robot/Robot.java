
package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.SampleRobot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends SampleRobot {

	static ADIS16448_IMU imu;

	// GRIP vision camera
	private static final int GRIPCAM_IMG_WIDTH = 320;
	private static final int GRIPCAM_IMG_HEIGHT = 240;
	private VisionThread visionThread;
	private final Object imgLock = new Object();
	double gripCameraCenterX = 0.0;

	// Autonomous Route Plans
	RoutePlanList planList = new RoutePlanList();
	
	// TODO:  Use Joystick button controls to select the auto route to be used 
	//        Hard coded to route '2' for testing
	int autoPlanSelection = 2;
	RoutePlan planForThisMatch = planList.get(autoPlanSelection);

	mecanumDrive driveSystem;
	Joystick driveJoy;

	public Robot() {
		imu = new ADIS16448_IMU();

	    SmartDashboard.putString("Auto Plan", planForThisMatch.name);
		SmartDashboard.putString("Auto Status", "Auto  " + autoPlanSelection + "  waiting to start");
		
		// GRIP vision camera
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setExposureManual(20);
		camera.setResolution(GRIPCAM_IMG_WIDTH, GRIPCAM_IMG_HEIGHT);
				
		visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
			if (!pipeline.findContoursOutput().isEmpty()) {
				Rect r = Imgproc.boundingRect(pipeline.findContoursOutput().get(0));
				synchronized (imgLock) {
					gripCameraCenterX = r.x + (r.width / 2);
					SmartDashboard.putNumber("gripCameraCenterX_000", gripCameraCenterX);
				}
			}
		});
		visionThread.start();

		// driveJoy = new Joystick(1);
		driveSystem = new mecanumDrive(0, 1, 8, 9);
	}

	/**
	 * Drive left & right motors for 2 seconds then stop
	 */
	public void autonomous() {
		
		
		
		for (int i = 0; i < planForThisMatch.size(); i++) {
			planForThisMatch.get(i).exec();
			
			// TMP TMP TMP - so can see auto status displayed 
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
		
		SmartDashboard.putString("Auto Status", "Auto Complete");
		
		
		
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {
			// SmartDashboard.putNumber("Y", driveJoy.getY());
			// SmartDashboard.putNumber("X", driveJoy.getX());
			// SmartDashboard.putNumber("Twist", driveJoy.getTwist());
			
			SmartDashboard.putNumber("gripCameraCenterX", gripCameraCenterX);

			driveSystem.drive();
			driveSystem.modeReadout();
			Timer.delay(.05);
		}
	}

	/**
	 * Runs during test mode
	 */
	public void test() {
		driveSystem.YTest();
		driveSystem.XTest();
		driveSystem.TwistTest();

	}
}
