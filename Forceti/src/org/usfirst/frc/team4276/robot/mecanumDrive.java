package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class mecanumDrive {

	static RobotDrive mecanumControl;
	Joystick mecanumJoystick;
	VictorSP forwardRightMotor;
	VictorSP forwardLeftMotor;
	VictorSP backRightMotor;
	VictorSP backLeftMotor;
	boolean robotFrame;
	boolean fieldFrame;
	boolean Xtest;
	boolean Ytest;
	boolean Twisttest;
	static String driveStatus = "initiation";
	
	double linearDeadband = .1;
	double rotateDeadband = .2;
	
	static double distance = 0;
	static boolean driveInit = true;
	
	double X = 0;
	double Y = 0;
	double Rotate = 0;
	double Xnet = 0;
	double Ynet = 0;
	double RotateNet = 0;
	
	double K_X_INDUCED_BY_Y = -.25;//place holder
	double K_X_INDUCED_BY_Rotate = 0;//place holder
	double K_Y_INDUCED_BY_X = 0.0;//real chassis = .025
	double K_Y_INDUCED_BY_Rotate = 0;//place holder
	double K_ROTATE_INDUCED_BY_X = 0.0;//real chassis = .024
	double K_ROTATE_INDUCED_BY_Y = 0.0;//real chassis = .01
	/////////////////////////////////////////////////////////////////////////
	double BIAS_X_INDUCED_BY_Y = 0;
	double BIAS_X_INDUCED_BY_Rotate = 0;
	double BIAS_Y_INDUCED_BY_X = 0;
	double BIAS_Y_INDUCED_BY_Rotate = 0;
	double BIAS_ROTATE_INDUCED_BY_X = 0;
	double BIAS_ROTATE_INDUCED_BY_Y = 0;

	static boolean rotating = false;
	
	int mode = 0;

	public mecanumDrive(int pwm0, int pwm1, int pwm2, int pwm3) {

		mecanumJoystick = new Joystick(0);
		forwardRightMotor = new VictorSP(pwm0);
		forwardLeftMotor = new VictorSP(pwm1);
		backRightMotor = new VictorSP(pwm2);
		backLeftMotor = new VictorSP(pwm3);

		mecanumControl = new RobotDrive(forwardLeftMotor, backLeftMotor, forwardRightMotor, backRightMotor);

	}

	

	void robotFrameDrive() {
		driveStatus = "Driving In Robot Frame";

		double yaw = 0.0;

		if (mecanumJoystick.getRawButton(1)) { //precision mode
			if (Math.abs(mecanumJoystick.getX()) > .1)
				X = mecanumJoystick.getX() / 4;
			else
				X = 0;

			if (Math.abs(mecanumJoystick.getY()) > .1)
				Y = mecanumJoystick.getY() / 4;
			else
				Y = 0;
			if ((mecanumJoystick.getTwist()) > .1)
				Rotate = mecanumJoystick.getTwist() / 4;
			else
				Rotate = 0;
		} else {//high speed mode
			if (Math.abs(mecanumJoystick.getX()) > .1)
				X = mecanumJoystick.getX();
			else
				X = 0;

			if (Math.abs(mecanumJoystick.getY()) > .1)
				Y = mecanumJoystick.getY();
			else
				Y = 0;
			if ((mecanumJoystick.getTwist()) > .1)
				Rotate = mecanumJoystick.getTwist() * mecanumJoystick.getTwist();
			else if ((mecanumJoystick.getTwist()) < -.1)
				Rotate = -1 * mecanumJoystick.getTwist() * mecanumJoystick.getTwist();
			else
				Rotate = 0;
			

		}
		
		BIAS_X_INDUCED_BY_Y = K_X_INDUCED_BY_Y*Y;
		BIAS_X_INDUCED_BY_Rotate = K_X_INDUCED_BY_Rotate*Rotate;
		
		BIAS_Y_INDUCED_BY_X = K_Y_INDUCED_BY_X*X;
		BIAS_Y_INDUCED_BY_Rotate = K_Y_INDUCED_BY_Rotate*Rotate;
		
		BIAS_ROTATE_INDUCED_BY_X = K_ROTATE_INDUCED_BY_X*X;
		BIAS_ROTATE_INDUCED_BY_Y = K_ROTATE_INDUCED_BY_Y*Y;
		
		Xnet = X + BIAS_X_INDUCED_BY_Y + BIAS_X_INDUCED_BY_Rotate ;
		Ynet = Y + BIAS_Y_INDUCED_BY_X + BIAS_Y_INDUCED_BY_Rotate ;
		RotateNet = Rotate + BIAS_ROTATE_INDUCED_BY_Y + BIAS_ROTATE_INDUCED_BY_X ;
		
		
		SmartDashboard.putNumber("X BIAS Y", K_X_INDUCED_BY_Y);
		SmartDashboard.putNumber("X BIAS Rotate", K_X_INDUCED_BY_Rotate);
		
		SmartDashboard.putNumber("Y BIAS X", K_Y_INDUCED_BY_X);
		SmartDashboard.putNumber("Y BIAS Rotate", K_Y_INDUCED_BY_Rotate);
		
		SmartDashboard.putNumber("Rotate BIAS X", K_ROTATE_INDUCED_BY_X);
		SmartDashboard.putNumber("Rotate BIAS Y", K_ROTATE_INDUCED_BY_Y);
		
		mecanumControl.mecanumDrive_Cartesian(Xnet, Ynet, RotateNet, yaw);
	}

	void XTest() {
		fieldFrame = false;
		robotFrame = false;
		Xtest = true;
		Ytest = false;
		Twisttest = false;

		

		if (Math.abs(mecanumJoystick.getX()) > .1)
			X = mecanumJoystick.getX()/2;
		else
			X = 0;

		BIAS_X_INDUCED_BY_Y = K_X_INDUCED_BY_Y*Y;
		BIAS_X_INDUCED_BY_Rotate = K_X_INDUCED_BY_Rotate*Rotate;
		
		BIAS_Y_INDUCED_BY_X = K_Y_INDUCED_BY_X*X;
		BIAS_Y_INDUCED_BY_Rotate = K_Y_INDUCED_BY_Rotate*Rotate;
		
		BIAS_ROTATE_INDUCED_BY_X = K_ROTATE_INDUCED_BY_X*X;
		BIAS_ROTATE_INDUCED_BY_Y = K_ROTATE_INDUCED_BY_Y*Y;
		
		Xnet = X + BIAS_X_INDUCED_BY_Y + BIAS_X_INDUCED_BY_Rotate ;
		Ynet = Y + BIAS_Y_INDUCED_BY_X + BIAS_Y_INDUCED_BY_Rotate ;
		RotateNet = Rotate + BIAS_ROTATE_INDUCED_BY_Y + BIAS_ROTATE_INDUCED_BY_X ;
		
		
		SmartDashboard.putNumber("X BIAS Y", K_X_INDUCED_BY_Y);
		SmartDashboard.putNumber("X BIAS Rotate", K_X_INDUCED_BY_Rotate);
		
		SmartDashboard.putNumber("Y BIAS X", K_Y_INDUCED_BY_X);
		SmartDashboard.putNumber("Y BIAS Rotate", K_Y_INDUCED_BY_Rotate);
		
		SmartDashboard.putNumber("Rotate BIAS X", K_ROTATE_INDUCED_BY_X);
		SmartDashboard.putNumber("Rotate BIAS Y", K_ROTATE_INDUCED_BY_Y);
		
		mecanumControl.mecanumDrive_Cartesian(Xnet, Ynet, RotateNet, 0);
	}

	void YTest() {
		fieldFrame = false;
		robotFrame = false;
		Xtest = false;
		Ytest = true;
		Twisttest = false;

	

		if (Math.abs(mecanumJoystick.getY()) > .02)
			Y = mecanumJoystick.getY();
		else
			Y = 0;

		BIAS_X_INDUCED_BY_Y = K_X_INDUCED_BY_Y*Y;
		BIAS_X_INDUCED_BY_Rotate = K_X_INDUCED_BY_Rotate*Rotate;
		
		BIAS_Y_INDUCED_BY_X = K_Y_INDUCED_BY_X*X;
		BIAS_Y_INDUCED_BY_Rotate = K_Y_INDUCED_BY_Rotate*Rotate;
		
		BIAS_ROTATE_INDUCED_BY_X = K_ROTATE_INDUCED_BY_X*X;
		BIAS_ROTATE_INDUCED_BY_Y = K_ROTATE_INDUCED_BY_Y*Y;
		
		Xnet = X + BIAS_X_INDUCED_BY_Y + BIAS_X_INDUCED_BY_Rotate ;
		Ynet = Y + BIAS_Y_INDUCED_BY_X + BIAS_Y_INDUCED_BY_Rotate ;
		RotateNet = Rotate + BIAS_ROTATE_INDUCED_BY_Y + BIAS_ROTATE_INDUCED_BY_X ;
		
		
		SmartDashboard.putNumber("X BIAS Y", K_X_INDUCED_BY_Y);
		SmartDashboard.putNumber("X BIAS Rotate", K_X_INDUCED_BY_Rotate);
		
		SmartDashboard.putNumber("Y BIAS X", K_Y_INDUCED_BY_X);
		SmartDashboard.putNumber("Y BIAS Rotate", K_Y_INDUCED_BY_Rotate);
		
		SmartDashboard.putNumber("Rotate BIAS X", K_ROTATE_INDUCED_BY_X);
		SmartDashboard.putNumber("Rotate BIAS Y", K_ROTATE_INDUCED_BY_Y);
		
		mecanumControl.mecanumDrive_Cartesian(Xnet, Ynet, RotateNet, 0);
	}

	void TwistTest() {
		fieldFrame = false;
		robotFrame = false;
		Xtest = false;
		Ytest = false;
		Twisttest = true;

		

		if (Math.abs(mecanumJoystick.getTwist()) > .02)
			Rotate = mecanumJoystick.getTwist();
		else
			Rotate = 0;

		BIAS_X_INDUCED_BY_Y = K_X_INDUCED_BY_Y*Y;
		BIAS_X_INDUCED_BY_Rotate = K_X_INDUCED_BY_Rotate*Rotate;
		
		BIAS_Y_INDUCED_BY_X = K_Y_INDUCED_BY_X*X;
		BIAS_Y_INDUCED_BY_Rotate = K_Y_INDUCED_BY_Rotate*Rotate;
		
		BIAS_ROTATE_INDUCED_BY_X = K_ROTATE_INDUCED_BY_X*X;
		BIAS_ROTATE_INDUCED_BY_Y = K_ROTATE_INDUCED_BY_Y*Y;
		
		Xnet = X + BIAS_X_INDUCED_BY_Y + BIAS_X_INDUCED_BY_Rotate ;
		Ynet = Y + BIAS_Y_INDUCED_BY_X + BIAS_Y_INDUCED_BY_Rotate ;
		RotateNet = Rotate + BIAS_ROTATE_INDUCED_BY_Y + BIAS_ROTATE_INDUCED_BY_X ;
		
		
		SmartDashboard.putNumber("X BIAS Y", K_X_INDUCED_BY_Y);
		SmartDashboard.putNumber("X BIAS Rotate", K_X_INDUCED_BY_Rotate);
		
		SmartDashboard.putNumber("Y BIAS X", K_Y_INDUCED_BY_X);
		SmartDashboard.putNumber("Y BIAS Rotate", K_Y_INDUCED_BY_Rotate);
		
		SmartDashboard.putNumber("Rotate BIAS X", K_ROTATE_INDUCED_BY_X);
		SmartDashboard.putNumber("Rotate BIAS Y", K_ROTATE_INDUCED_BY_Y);
		
		mecanumControl.mecanumDrive_Cartesian(Xnet, Ynet, RotateNet, 0);
	}

	void Operatordrive() {
		robotFrameDrive();
	}

	/*
	 * void driveTest() { if(mode < 3 && mecanumJoystick.getRawButton(5)) { mode
	 * ++; } else if(mode >= 3 && mecanumJoystick.getRawButton(5)) { mode = 1; }
	 * 
	 * if(mode == 1) { YTest(); } if(mode == 2) { XTest(); } if(mode == 3) {
	 * TwistTest(); } }
	 */
	static void modeReadout() {
		SmartDashboard.putString("Drive Status:", driveStatus);
	}

	static void drive()
	{
		mecanumControl.mecanumDrive_Cartesian(0, -1, 0, 0);	
	}
	
	static boolean driveStraight(double distanceGoal)
	{
		if(driveInit == true){
			distance = 0;
			driveInit = false;
		}
		driveStatus = "driving " + distanceGoal;

		boolean value = false;
		
		distance = distance + (mecanumNavigation.robotDeltaY/48);
		
		double linearDeadband = .1;
		double driveDiff = distanceGoal - distance;
		double driveConstant = -0.14;// place holder
		double drivePower = driveConstant * driveDiff;
		
		SmartDashboard.putNumber("Distance", distance);
		SmartDashboard.putNumber("Distance Error", driveDiff);
		SmartDashboard.putNumber("Drive Power", drivePower);
		
		SmartDashboard.putString("auto", "drive");
		

		if (drivePower > 0.5) {
			drivePower = 0.5;
		}
		if (drivePower < -0.5) {
			drivePower = -0.5;
		}
		
		/*
		 * This if statement prevent the rotational power from being too high So
		 * that the robot won't rotate too fast
		 */
		
		mecanumControl.mecanumDrive_Cartesian(0, drivePower, 0, 0);
		
		if (Math.abs(driveDiff) < linearDeadband) {

			drivePower = 0;
			value = true;
			driveInit = true;
			SmartDashboard.putString("auto", "finish");
		} else {
			value = false;
		}
		
		
		
		return value;
	}
	static boolean driveToCoordinate(double Xgoal, double Ygoal, double RotationGoal) {

		driveStatus = "Driving to " + Xgoal + ", " + Ygoal + " with a desired rotation of" + RotationGoal;

		boolean Xacheived = false; // default
		boolean Yacheived = false; // default
		boolean rotationAcheived = false; // default

		double linearDeadband = .05;// feet
		double rotationDeadband = 5;// degrees

		double yaw = Robot.imu.getAngleZ();
		double Xdiff = Xgoal - mecanumNavigation.currentFieldX;
		double Ydiff = Ygoal - mecanumNavigation.currentFieldY;
		double RotationDiff = RotationGoal - yaw;

		double XpowerConstant = .2; // place holder
		double YpowerConstant = .2; // place holder
		double rotationPowerConstant = .2; // place holder

		double Xpower = Xdiff * XpowerConstant;
		if (Xpower > .75) {
			Xpower = .75;
		} else if (Xpower < -.75) {
			Xpower = -.75;
		}
		// This if statement prevent the power in the X direction of the field
		// from being too high

		if (Math.abs(Xdiff) < linearDeadband) {
			Xpower = 0;
			Xacheived = true;
		}
		/*
		 * if the difference between the robot's X position on the field and the
		 * goal is less than the deadband, so if the robot is getting close then
		 * the power in the X direction is set to 0, so that the robot stops
		 * moving in the X direction of the field
		 */

		double Ypower = Ydiff * YpowerConstant;
		if (Ypower > .75) {
			Ypower = .75;
		} else if (Ypower < -.75) {
			Ypower = -.75;
		}
		// This if statement prevent the power in the Y direction of the field
		// from being too high

		if (Math.abs(Ydiff) < linearDeadband) {
			Ypower = 0;
			Yacheived = true;
		}
		/*
		 * if the difference between the robot's Y position on the field and the
		 * goal is less than the deadband, so if the robot is getting close then
		 * the power in the Y direction is set to 0, so that the robot stops
		 * moving in the Y direction of the field
		 */

		double rotationPower = RotationDiff * rotationPowerConstant;
		if (rotationPower > .75) {
			rotationPower = .75;
		} else if (rotationPower < -.75) {
			rotationPower = -.75;
		}
		/*
		 * This if statement prevent the rotational power from being too high So
		 * that the robot won't rotate too fast
		 */

		if (Math.abs(RotationDiff) < rotationDeadband) {
			rotationPower = 0;
			rotationAcheived = true;
		}
		/*
		 * if the difference in rotation is less than the deadband, so if the
		 * robot has less of an angle to rotate to get to its desired angle then
		 * the rotational power is less than 0, so that the robot stops rotating
		 */

		mecanumControl.mecanumDrive_Cartesian(rotationPower, Ypower, rotationPower, yaw);

		if ((Xacheived == true && Yacheived == true && rotationAcheived == true)) {
			return true;

		} else {
			return false;
		}
	}

	static boolean rotateToCoordinate(double XGoal, double YGoal) {

		driveStatus = "Rotating to " + XGoal + ", " + YGoal;

		boolean value = false;
		double rotation = mecanumNavigation.yaw;
		double rotationDeadband = 2;// degrees, place holder
		double rotationGoal = routeMapping.findHeading(XGoal, YGoal);
		double RotationDiff = rotationGoal - rotation;
		double rotationConstant = -0.15;// place holder
		double rotationPower = rotationConstant * RotationDiff;
		rotating = true;
		
		SmartDashboard.putString("auto", "rotate");
		

		if (rotationPower > 0.75) {
			rotationPower = 0.75;
		}
		if (rotationPower < -0.75) {
			rotationPower = -0.75;
		}
		
		/*
		 * This if statement prevent the rotational power from being too high So
		 * that the robot won't rotate too fast
		 */
		
		
		if (Math.abs(RotationDiff) < rotationDeadband) {

			rotationPower = 0;
			rotating = false;
			value = true;

			
		} else {
			value = false;

		}
		mecanumControl.mecanumDrive_Cartesian(0, 0, rotationPower,0);
		/*
		 * if the difference in rotation is less than the deadband, so if the
		 * robot has less of an angle to rotate to get to its desired angle then
		 * the rotational power is less than 0, so that the robot stops rotating
		 */
		
		return value;
	}

	static boolean rotateToHeading(double RotationGoal) {

		driveStatus = "Rotating to " + RotationGoal;

		boolean value = false;
		double rotation = mecanumNavigation.yaw;
		double rotationDeadband = 2;// degrees, place holder
		double RotationDiff = RotationGoal - rotation;
		double rotationConstant = -.008;// place holder
		double rotationPower = rotationConstant * RotationDiff;
		
		rotating = true;
		SmartDashboard.putNumber("rotating offset", RotationDiff);
		SmartDashboard.putString("auto", "rotate");
		

		if (rotationPower > 0.5) {
			rotationPower = 0.5;
		}
		if (rotationPower < -0.5) {
			rotationPower = -0.5;
		}
		
		/*
		 * This if statement prevent the rotational power from being too high So
		 * that the robot won't rotate too fast
		 */
		
		
		if (Math.abs(RotationDiff) < rotationDeadband) {

			rotationPower = 0;
			rotating = false;
			value = true;

			Robot.systemTimer.delay(5);
		} else {
			value = false;

		}
		mecanumControl.mecanumDrive_Cartesian(0, 0, rotationPower,0);
		/*
		 * if the difference in rotation is less than the deadband, so if the
		 * robot has less of an angle to rotate to get to its desired angle then
		 * the rotational power is less than 0, so that the robot stops rotating
		 */
		
		return value;
	}
	
	static boolean visionBoilerAlignment(double boilerAngleOffset) // boiler to the right of center = + boiler to the left of center = -
	{

		driveStatus = "Rotating to align with Boiler";

		boolean value = false;
		double rotationConstant = 0.2; // place holder
		double rotationPower = 0; // default
		double rotationDeadband = 10; // pixels
		if (Math.abs(boilerAngleOffset) > rotationDeadband) {
			rotationPower = boilerAngleOffset * rotationConstant;
			value = false;
		} else {
			rotationPower = 0;
			value = true;
		}
		mecanumControl.mecanumDrive_Cartesian(0, 0, 0, rotationPower);
		return value;
	}

	// edit later (rotation for boiler in auto)
	static boolean gearAlign(double targetXOffset) {

		driveStatus = "Aligning with gear lift";

		double deadband = 5; // pixels
		double k = .02;// power applied for every pixel off from center
		double power = k * targetXOffset;

		if (Math.abs(targetXOffset) > deadband) {
			mecanumControl.mecanumDrive_Cartesian(power, 0, 0, 0);
			return false;
		} else {
			return true;
		}
	}

}
