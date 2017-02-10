
package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.SampleRobot;

import org.usfirst.frc.team4276.robot.RouteTask.DrivingSpeed;
import org.usfirst.frc.team4276.robot.RouteTask.Operation;

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

	static boolean isValidCurrentRobotFieldPosition = false;
	static RobotPositionPolar currentRobotFieldPosition;

	static BoilerTracker boilerTracker;

	// GRIP vision camera
	static GripVisionThread gripVisionThread;

	// Boiler LIDAR (with GRIP camera on turntable #1)
	static LIDAR boilerLidar;
	static LidarSpin turntable1;

	// TODO: Verify can set this from the driver station.
	//        Want to disable scanning in the pit (or anywhere else there is no vision target)
	//        But need a way to turn it on from the DS in case forgot to turn it back on before the match starts
	static boolean isBoilerTrackerEnabled = false;

	// Autonomous Route Plans
	// TODO: Use Joystick button controls to select the auto route to be used
	// Hard coded to route '2' for testing
	int autoPlanSelection = 2;
	static RoutePlanList planList;
	static RoutePlan planForThisMatch;

	mecanumDrive driveSystem;
	static Timer systemTimer;
	Joystick driveJoy;
	static Joystick XBoxController;
	static Joystick logitechJoystick;
 
	public Robot() {
		imu = new ADIS16448_IMU();

		boilerTracker = new BoilerTracker();
		boilerLidar = new LIDAR("boiler", Port.kMXP, 0x62);
		XBoxController = new Joystick(3);
		logitechJoystick = new Joystick(0);

		int relay = 2;
		int dio11 = 11;
		int dio12 = 12;
		turntable1 = new LidarSpin(relay, dio11, dio12);
		// Scan limits -140 to +230 for competition  
		//     0.0 is straight ahead robot frame.  Want to avoid extended operation with the 
		//     back of the turntable pointed at the boiler, (small variance in robot heading 
		//     would cause the scanner to have to switch sides).  
		turntable1.setScanLimits(-45, 45);   // TMP TMP TMP  for prototype testing

		planList = new RoutePlanList();
		planForThisMatch = planList.get(autoPlanSelection);

		gripVisionThread = new GripVisionThread();
		gripVisionThread.start();

		SmartDashboard.putString("Auto Plan", planForThisMatch.name);
		SmartDashboard.putString("Auto Status", "Auto  " + autoPlanSelection + "  waiting to start");

		// driveJoy = new Joystick(1);
		driveSystem = new mecanumDrive(0, 1, 8, 9);
	}

	/**
	 * Drive left & right motors for 2 seconds then stop
	 */
	public void autonomous() {
		turntable1.spinMode = LidarSpin.SpinMode.SCAN;

		if (Robot.currentRobotFieldPosition.isBlueBoiler) {
			imu.resetYawOffsetToFieldFrame(180.0);
		} else {
			imu.resetYawOffsetToFieldFrame(0.0);
		}

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
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	public void operatorControl() {
		turntable1.spinMode = LidarSpin.SpinMode.SCAN;

		while (isOperatorControl() && isEnabled()) {
			// SmartDashboard.putNumber("Y", driveJoy.getY());
			// SmartDashboard.putNumber("X", driveJoy.getX());
			// SmartDashboard.putNumber("Twist", driveJoy.getTwist());

			//driveSystem.drive();
			driveSystem.modeReadout();
			Timer.delay(.05);
		}

		turntable1.spinMode = LidarSpin.SpinMode.IDLE;
	}

	/**
	 * Runs during test mode
	 */
	public void test() {
		//driveSystem.YTest();
		//driveSystem.XTest();
		//driveSystem.TwistTest();

	}
}
