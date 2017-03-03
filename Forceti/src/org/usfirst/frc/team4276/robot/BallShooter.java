package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DigitalInput;

public class BallShooter {

	private int count = 0;
	private double filteredRate;
	private double[] rateSamples = new double[5];

	final double FEEDER_DELAY_TIME = 1.0; // seconds
	final double FEEDER_POWER = 0.75; // -1 to 1
	final double AGITATOR_SPEED = 1;
	double FLYWHEEL_SPEED = 3100; // rpm
	double GAIN_PROPORTIONAL = 0.9e-3;
	double GAIN_INTEGRAL = 10.0e-3;
	double GAIN_DERIVATIVE = 0.0;
	double FEED_FORWARD_K = 0.0;

	Joystick testJoy;
	VictorSP shooterWheel;
	VictorSP feedingWheel;
	VictorSP agitator;
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
	
	double ff_power = 0;
	DigitalInput feedforward;
	//Toggler shooterToggler;
	SoftwareTimer feederStartDelayTimer;



	public BallShooter(int pwm9, int pwm4, int dio8, int dio9) {
		testJoy = new Joystick(1);
		shooterWheel = new VictorSP(pwm9);
		feedingWheel = new VictorSP(pwm4);
		//agitator = new VictorSP(pwm10);
		shooterEncoder = new Encoder(dio8,dio9); 		// encoder
		shooterEncoder.setDistancePerPulse(60.0/1024.0); // encoder RPM
		//shooterToggler = new Toggler(XBox.RTrigger);
		feederStartDelayTimer = new SoftwareTimer();
		rateSamples[0] = 0;
		rateSamples[1] = 0;
		rateSamples[2] = 0;
		rateSamples[3] = 0;
		rateSamples[4] = 0;
	}
	
	/*public BallShooter(int pwm9, int pwm4, int dio19, int dio20, int pwm10 ,int dioFF) {
		testJoy = new Joystick(1);
		shooterWheel = new VictorSP(pwm9);
		feedingWheel = new VictorSP(pwm4);
		agitator = new VictorSP(pwm10);
		shooterEncoder = new Encoder(dio19,dio20); 		// encoder
		feedforward = new DigitalInput(dioFF);
		
		shooterEncoder.setDistancePerPulse(60.0/1024.0); // encoder RPM
		//shooterToggler = new Toggler(XBox.RTrigger);
		feederStartDelayTimer = new SoftwareTimer();
		rateSamples[0] = 0;
		rateSamples[1] = 0;
		rateSamples[2] = 0;
		rateSamples[3] = 0;
		rateSamples[4] = 0;
	}
*/
	void updateGainsFromDriverInput() {
		/*if (testJoy.getRawButton(7))
			GAIN_PROPORTIONAL = GAIN_PROPORTIONAL - .00001;
		else if (testJoy.getRawButton(8))
			GAIN_PROPORTIONAL = GAIN_PROPORTIONAL + .00001;
		if (testJoy.getRawButton(9))
			GAIN_INTEGRAL = GAIN_INTEGRAL - .00001;
		else if (testJoy.getRawButton(10))
			GAIN_INTEGRAL = GAIN_INTEGRAL + .00001;
		if (testJoy.getRawButton(11))
			GAIN_DERIVATIVE = GAIN_DERIVATIVE - .00001;
		else if (testJoy.getRawButton(12))
			GAIN_DERIVATIVE = GAIN_DERIVATIVE + .00001;
		*/	
		if (Robot.logitechJoystick.getRawButton(6))
			FLYWHEEL_SPEED = FLYWHEEL_SPEED+1;
		else if (Robot.logitechJoystick.getRawButton(5))
			FLYWHEEL_SPEED = FLYWHEEL_SPEED-1;

		SmartDashboard.putNumber("Kp*1000", GAIN_PROPORTIONAL*1000);
		SmartDashboard.putNumber("Kd*1000", GAIN_DERIVATIVE*1000);
		SmartDashboard.putNumber("Ki*1000", GAIN_INTEGRAL*1000);
		SmartDashboard.putNumber("CommandedSpeed", FLYWHEEL_SPEED);
	}

	void feederManualControl(){
		if(Robot.XBoxController.getRawAxis(XBox.RStickY)<-.5)
		{
			feedingWheel.set(FEEDER_POWER);
			//agitator.set(AGITATOR_SPEED);
			
		}
		else if (Robot.XBoxController.getRawAxis(XBox.RStickY)>.5)
		{
			feedingWheel.set(-FEEDER_POWER);
			//agitator.set(-AGITATOR_SPEED);
		}
		else
		{
			feedingWheel.set(0);	
			//agitator.set(0);
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
			
			/*if (!feedforward.get())
				ff_power = FEED_FORWARD_K;
			else
				ff_power=0;
			*/	
			
			timeStep = timeNow - timePrevious;
			errorProportional = FLYWHEEL_SPEED - currentRate;
			errorIntegral = errorIntegral + errorProportional * timeStep;
			errorDerivative = (errorProportional - errorProportionalPrevious) / timeStep;
			assignedPower = GAIN_PROPORTIONAL * errorProportional + GAIN_INTEGRAL * errorIntegral
					+ GAIN_DERIVATIVE * errorDerivative + ff_power;

			if (assignedPower > 1)
				assignedPower = 1;
			else if (assignedPower < -1)
				assignedPower = -1;
		}
		return assignedPower;
	}

	void performMainProcessing() {
		
		
			
		feederManualControl();
		updateGainsFromDriverInput();
		//shooterToggler.updateMechanismState();
		currentRate = shooterEncoder.getRate();
		
		rateSamples[4] = rateSamples[3];
		rateSamples[3] = rateSamples[2];
		rateSamples[2] = rateSamples[1];
		rateSamples[1] = rateSamples[0];
		rateSamples[0] = currentRate;
		
		filteredRate = (rateSamples[0] + rateSamples[1] + rateSamples[2] + rateSamples[3] + rateSamples[4])/5;
		
		if(count%3 == 0)
		{
		SmartDashboard.putNumber("Shooter Speed", filteredRate);
		}
		count++;
		
		//if (shooterToggler.getMechanismState()) {
		if (Robot.XBoxController.getRawAxis(XBox.RTrigger)>0.5) {
			if (initializeShooter) {
				// if initializeShooter is true, then this if statement runs
				feederStartDelayTimer.setTimer(FEEDER_DELAY_TIME);
				initializeShooter = false;
			} else if (feederStartDelayTimer.isExpired()) {
				feedingWheel.set(FEEDER_POWER);
				//agitator.set(AGITATOR_SPEED);
			}
			assignedPower = computeFlyWheelPower();
			shooterWheel.set(assignedPower);
		} else {
			feedingWheel.set(0.0);
			shooterWheel.set(0.0);
			//agitator.set(0.0);
			initializePID = true;
			initializeShooter = true;
		}
		
	}

	void giveFeedback() {
		SmartDashboard.putNumber("Shooter Speed", currentRate);
		SmartDashboard.putNumber("Shooter Motor Power", assignedPower);
		SmartDashboard.putNumber("R Trigger", Robot.XBoxController.getRawAxis(XBox.RTrigger));
	}

	void autoShoot() {
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

currentRate = shooterEncoder.getRate();
		
		rateSamples[4] = rateSamples[3];
		rateSamples[3] = rateSamples[2];
		rateSamples[2] = rateSamples[1];
		rateSamples[1] = rateSamples[0];
		rateSamples[0] = currentRate;
		
		filteredRate = (rateSamples[0] + rateSamples[1] + rateSamples[2] + rateSamples[3] + rateSamples[4])/5;
		
		if(count%3 == 0)
		{
		SmartDashboard.putNumber("Shooter Speed", filteredRate);
		}
		count++;
		
		
		if (initializeShooter) {
			// initializes the Shooter
			feederStartDelayTimer.setTimer(FEEDER_DELAY_TIME);
			initializeShooter = false;
		} else if (feederStartDelayTimer.isExpired()) {
			feedingWheel.set(FEEDER_POWER);
		}
		assignedPower = computeFlyWheelPower();
		shooterWheel.set(assignedPower);
	}
}
