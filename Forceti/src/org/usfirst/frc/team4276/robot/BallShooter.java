package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

public class BallShooter {

	final double FEEDER_DELAY_TIME = 5.0; // seconds
	final double FEEDER_POWER = 0.5; // -1 to 1
	double FLYWHEEL_SPEED = 3000; // rpm
	double GAIN_PROPORTIONAL = 0.0;
	double GAIN_INTEGRAL = 0.0;
	double GAIN_DERIVATIVE = 0.0;

	Joystick testJoy;
	VictorSP shooterWheel;
	VictorSP feedingWheel;
	Encoder shooterEncoder;
	double assignedPower = 0;
	double currentRate = 0;

	static double errorProportional;
	static double errorProportionalPrevious;
	static double errorIntegral;
	static double errorDerivative;
	static double timeNow;
	static double timePrevious;
	static double timeStep;
	static boolean initializePID = true;
	static boolean initializeShooter = true;
	//Toggler shooterToggler;
	SoftwareTimer feederStartDelayTimer;



	public BallShooter(int pwm9, int pwm4, int dio19, int dio20) {
		testJoy = new Joystick(1);
		shooterWheel = new VictorSP(pwm9);
		feedingWheel = new VictorSP(pwm4);
		shooterEncoder = new Encoder(dio19,dio20); 		// encoder
		shooterEncoder.setDistancePerPulse(60.0/1024.0); // encoder RPM
		//shooterToggler = new Toggler(XBox.RTrigger);
		feederStartDelayTimer = new SoftwareTimer();
	}

	void updateGainsFromDriverInput() {
		if (testJoy.getRawButton(7))
			GAIN_PROPORTIONAL = GAIN_PROPORTIONAL - .001;
		else if (testJoy.getRawButton(8))
			GAIN_PROPORTIONAL = GAIN_PROPORTIONAL + .001;
		if (testJoy.getRawButton(9))
			GAIN_INTEGRAL = GAIN_INTEGRAL - .001;
		else if (testJoy.getRawButton(10))
			GAIN_INTEGRAL = GAIN_INTEGRAL + .001;
		if (testJoy.getRawButton(11))
			GAIN_DERIVATIVE = GAIN_DERIVATIVE - .001;
		else if (testJoy.getRawButton(12))
			GAIN_DERIVATIVE = GAIN_DERIVATIVE + .001;
		if (testJoy.getRawButton(6))
			FLYWHEEL_SPEED = FLYWHEEL_SPEED++;
		else if (testJoy.getRawButton(5))
			FLYWHEEL_SPEED = FLYWHEEL_SPEED--;

		SmartDashboard.putNumber("Kp", GAIN_PROPORTIONAL);
		SmartDashboard.putNumber("Kd", GAIN_DERIVATIVE);
		SmartDashboard.putNumber("Ki", GAIN_INTEGRAL);
		SmartDashboard.putNumber("CommandedSpeed", FLYWHEEL_SPEED);
	}

	void feederTest(){
		if(Robot.XBoxController.getRawAxis(XBox.RStickY)<-.5)
		{
			feedingWheel.set(FEEDER_POWER);
		}
		else
		{
			feedingWheel.set(0);	
		}
	}
	
	double computeFlyWheelPower() {
		//double assignedPower;

		if (initializePID == true) {
			timeNow = Robot.systemTimer.get();
			currentRate = shooterEncoder.getRate();
			errorProportional = FLYWHEEL_SPEED - currentRate;
			errorIntegral = 0;
			assignedPower = 0;
			initializePID = false;
		} else {
			errorProportionalPrevious = errorProportional;
			timePrevious = timeNow;

			timeNow = Robot.systemTimer.get();
			
			
			timeStep = timeNow - timePrevious;
			errorProportional = FLYWHEEL_SPEED - currentRate;
			errorIntegral = errorIntegral + errorProportional * timeStep;
			errorDerivative = (errorProportional - errorProportionalPrevious) / timeStep;
			assignedPower = GAIN_PROPORTIONAL * errorProportional + GAIN_INTEGRAL * errorIntegral
					+ GAIN_DERIVATIVE * errorDerivative;

			if (assignedPower > 1)
				assignedPower = 1;
			else if (assignedPower < -1)
				assignedPower = -1;
		}
		return assignedPower;
	}

	void performMainProcessing() {
		feederTest();
		updateGainsFromDriverInput();
		//shooterToggler.updateMechanismState();
		currentRate = shooterEncoder.getRate();
		SmartDashboard.putNumber("Shooter Speed", currentRate);
		
		//if (shooterToggler.getMechanismState()) {
		if (Robot.XBoxController.getRawAxis(XBox.RTrigger)>0.5) {
			if (initializeShooter) {
				// if initializeShooter is true, then this if statement runs
				feederStartDelayTimer.setTimer(FEEDER_DELAY_TIME);
				initializeShooter = false;
			} else if (feederStartDelayTimer.isExpired()) {
				feedingWheel.set(FEEDER_POWER);
			}
			assignedPower = computeFlyWheelPower();
			shooterWheel.set(assignedPower);
		} else {
			feedingWheel.set(0.0);
			shooterWheel.set(0.0);
			initializePID = true;
			initializeShooter = true;
		}
		
	}

	void giveFeedback() {
		SmartDashboard.putNumber("Shooter Speed", currentRate);
		SmartDashboard.putNumber("Shooter Motor Power", assignedPower);
		SmartDashboard.putNumber("R Trigger", Robot.XBoxController.getRawAxis(XBox.RTrigger));
	}

	static void autoShoot() {
		// double RobotXposition = mecanumNavigation.currentFieldX;
		// double RobotYposition = mecanumNavigation.currentFieldY;
		double XRedBoiler = -27.17;// feet
		double YRedBoiler = -13.5;// feet
		double XBlueBoiler = 27.17;// feet
		double YBlueBoiler = 13.5;// feet
		double distanceToGoal = 0;// default

		double assignedPower;

		/*
		 * if (RobotXposition > 0) // blue alliance { distanceToGoal = Math
		 * .sqrt(Math.pow(XBlueBoiler - RobotXposition, 2) +
		 * Math.pow(YBlueBoiler - RobotYposition, 2)); } else if (RobotXposition
		 * < 0) // red alliance { distanceToGoal = Math
		 * .sqrt(Math.pow(XRedBoiler - RobotXposition, 2) + Math.pow(YRedBoiler
		 * - RobotYposition, 2)); } // distanceToGoal can be used to calculate
		 * speed of shooter
		 */

		/*if (initializeShooter) {
			// if initializeShooter is true, then this if statement runs
			feederStartDelayTimer.setTimer(FEEDER_DELAY_TIME);
			initializeShooter = false;
		} else if (feederStartDelayTimer.isExpired()) {
			feedingWheel.set(FEEDER_POWER);
		}*/
		//assignedPower = computeFlyWheelPower();
		//shooterWheel.set(assignedPower);
	}
}
