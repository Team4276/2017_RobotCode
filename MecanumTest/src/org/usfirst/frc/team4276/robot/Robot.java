
package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.SampleRobot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.I2C.Port;

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

	mecanumNavigation robotLocation;
	mecanumDrive driveSystem;
	Climber climbingSystem;
	gearCollection gearMechanism;
	BallShooter Shooter;
	BallCollector ballCollectingMechanism;

	static Timer systemTimer;
	static Joystick XBoxController;
	static Joystick logitechJoystick;

	// Boiler LIDAR (with GRIP camera on turntable #1)
	static LIDAR boilerLidar;
	static LidarSpin turntable1;

	// yawOffsetToFieldFrame is set at start of autonomous when the robot
	// heading is either 0.0 or 180.0
	// Get Field north reference by adding this to imu.getYaw()
	//
	// This loses accuracy over the course of the game, but should be very
	// reliable during autonomous.
	//
	// It is still pretty good in teleop because the errors affect only the
	// polar
	// angle, not the LIDAR range that is continually measured by the vision
	// system. Therefore drive toward an increasingly sloppy range on an arc,
	// but once found you can slide precisely down the arc to the destination.
	static boolean isValidYawOffsetToFieldFrame = false;
	static double yawOffsetToFieldFrame = 0.0;

	static boolean isValidCurrentRobotFieldPosition = false;
	static RobotPositionPolar currentRobotFieldPosition;

	static BoilerTracker boilerTracker;

	// GRIP vision camera
	static GripVisionThread gripVisionThread;

	// TODO: Verify can set this from the driver station.
	// Want to disable scanning in the pit (or anywhere else there is no vision
	// target)
	// But need a way to turn it on from the DS in case forgot to turn it back
	// on before the match starts
	static boolean isBoilerTrackerEnabled = false;

	// Autonomous Route Plans
	// TODO: Use Joystick button controls to select the auto route to be used
	// Hard coded to route '2' for testing
	int autoPlanSelection = 2;
	static RoutePlanList planList;
	static RoutePlan planForThisMatch;

	public Robot() {
		imu = new ADIS16448_IMU();

		robotLocation = new mecanumNavigation(0,1,2,3,4,5,6,7);//dio ports
		driveSystem = new mecanumDrive(0, 1, 2, 3);// pwm ports
		climbingSystem = new Climber(8, 13);// pwm port 8, dio port 13
		gearMechanism = new gearCollection(6,1,2,14,11,12);//pwm port 6, relay ports 1 & 2, dio ports 14, 11, 12
		Shooter = new BallShooter(4, 5, 10);// pwm ports 4 & 5, dio port 10
		ballCollectingMechanism = new BallCollector(7);// pwm port 7

		robotLocation.start();

		XBoxController = new Joystick(3);
		logitechJoystick = new Joystick(0);

		boilerTracker = new BoilerTracker();
		boilerLidar = new LIDAR("boiler", Port.kMXP, 0x62);

		int relay3 = 3;
		int dio8 = 8;
		int dio9 = 9;
		turntable1 = new LidarSpin(relay3, dio8, dio9);
		// Scan limits -140 to +230 for competition
		// 0.0 is straight ahead robot frame. Want to avoid extended operation
		// with the
		// back of the turntable pointed at the boiler, (small variance in robot
		// heading
		// would cause the scanner to have to switch sides).
		turntable1.setScanLimits(-45, 45); // TMP TMP TMP for prototype testing

		planList = new RoutePlanList();
		planForThisMatch = planList.get(autoPlanSelection);

		gripVisionThread = new GripVisionThread();
		gripVisionThread.start();

		SmartDashboard.putString("Auto Plan", planForThisMatch.name);
		SmartDashboard.putString("Auto Status", "Auto  " + autoPlanSelection + "  waiting to start");

		driveSystem = new mecanumDrive(0, 1, 2, 3);
	}

	/**
	 * Drive left & right motors for 2 seconds then stop
	 */
	public void autonomous() {

		if (isBoilerTrackerEnabled) {
			isValidYawOffsetToFieldFrame = false;
			yawOffsetToFieldFrame = 0.0 - imu.getYaw();
			if (isValidCurrentRobotFieldPosition) {
				if (currentRobotFieldPosition.isBlueBoiler) {
					yawOffsetToFieldFrame += 180.0;
					if (yawOffsetToFieldFrame > 180.0) {
						yawOffsetToFieldFrame -= 360.0;
					}
				}
				isValidYawOffsetToFieldFrame = true;
			}

			if (Robot.turntable1.spinMode == LidarSpin.SpinMode.FIXED_OFFSET_FROM_YAW) {
				if (currentRobotFieldPosition.isBlueBoiler) {
					Robot.turntable1.resetEncoderAtOffsetDegrees(-90.0);
				} else {
					Robot.turntable1.resetEncoderAtOffsetDegrees(90.0);
				}
			}

			turntable1.spinMode = LidarSpin.SpinMode.SCAN;

			boolean isError = false;
			for (int i = 0; i < planForThisMatch.size(); i++) {
				RouteTask.ReturnValue retVal = planForThisMatch.get(i).exec();
				if (retVal != RouteTask.ReturnValue.SUCCESS) {
					isError = true;
					SmartDashboard.putString("Auto Status", "Auto Failed step " + i);
					break;
				}
			}
			if (!isError) {
				SmartDashboard.putString("Auto Status", "Auto Complete");
			}
		} else {
			// TODO: use mecanumNavigation() instead of vision system
		}
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	public void operatorControl() {
		isBoilerTrackerEnabled = true;

		turntable1.spinMode = LidarSpin.SpinMode.SCAN;

		while (isOperatorControl() && isEnabled()) {
			driveSystem.Operatordrive();
			climbingSystem.performMainProcessing();
			gearMechanism.performMainProcessing();
			Shooter.performMainProcessing();
			ballCollectingMechanism.performMainProcessing();
			Timer.delay(.05);
		}

		turntable1.spinMode = LidarSpin.SpinMode.IDLE;
		isBoilerTrackerEnabled = false;
	}

	/**
	 * Runs during test mode
	 */
	public void test() {
		// driveSystem.YTest();
		// driveSystem.XTest();
		// driveSystem.TwistTest();

	}
}
