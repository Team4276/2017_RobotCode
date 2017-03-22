
package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

	static double gearPegOffset = 0; // PLACE HOLDER
	static double boilerOffset = 0; // PLACE HOLDER
	
	
	mecanumNavigation robotLocation;
	mecanumDrive driveSystem;
	Climber climbingSystem;
	gearCollection gearMechanism;
	ArmPID gearArmControl;
	BallShooter Shooter;
	BallCollector ballCollectingMechanism;
	AutoCases autonomous;
	//autoModeSelector autoSelect;
	AutoSelector autoSelectorThread;
	UsbCamera cam;
	static Timer systemTimer;
	static Joystick XBoxController;
	static Joystick logitechJoystick;
	static Joystick autoSelector;
	static Joystick testJoy;
	
	static double yawOffsetToFieldFrame = 0.0;
	static double xyFieldFrameSpeed = 0.0;
	static double xyFieldFrameHeading = 0.0;

	public Robot() {
		imu = new ADIS16448_IMU();
		
		cam=CameraServer.getInstance().startAutomaticCapture();
		cam.setResolution(42, 24);
		cam.setFPS(20);
		cam.setExposureManual(24);

		systemTimer = new Timer();
		systemTimer.start();

		gearArmControl = new ArmPID();
		robotLocation = new mecanumNavigation(0, 1, 2, 3);// dio
		Shooter = new BallShooter(9, 4, 8, 9);// pwm ports 4 & 9, dio ports 8 & 9
		driveSystem = new mecanumDrive(0, 1, 2, 3);// pwm ports
		climbingSystem = new Climber(5, 13);// pwm port 5, dio port 13
		gearMechanism = new gearCollection(6, 7, 7, 4, 5);// pwm ports 6 and 7,
															// dio ports 18, 19, and 20
		
		ballCollectingMechanism = new BallCollector(8);// pwm port 8

		autonomous = new AutoCases(Shooter,driveSystem,gearMechanism);
		//autoSelect = new autoModeSelector();
		autoSelectorThread = new AutoSelector();
		
		//autoSelect.start();
		robotLocation.start();
		gearArmControl.start();
		//autoSelectorThread.start();
		

		XBoxController = new Joystick(3);
		logitechJoystick = new Joystick(0);
		//autoSelector = new Joystick(1);
		testJoy = new Joystick(1);

	}

	public void robotInit() {
		alignRobotAndField();
	}
	
	public void autonomous() {
		SmartDashboard.putString("auto", "yes");
		autonomous.autoModes();
	}
	
	private synchronized void alignRobotAndField() {
		yawOffsetToFieldFrame = 0.0 - imu.getYaw();
	}


	/**
	 * Runs the motors with arcade steering.
	 */
	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {
			driveSystem.Operatordrive();
			climbingSystem.performMainProcessing();
			gearMechanism.performMainProcessing();
			Shooter.performMainProcessing();
			ballCollectingMechanism.performMainProcessing();
			Timer.delay(.005);
		}
	}

	/**
	 * Runs during test mode
	 */
	public void test() {
		
		while (isOperatorControl() && isEnabled()) {
			driveSystem.Operatordrive();
			climbingSystem.performMainProcessing();
			gearMechanism.performMainProcessing();
			Shooter.performMainProcessing();
			ballCollectingMechanism.performMainProcessing();
			Timer.delay(.005);
		}

	}
}
