package org.usfirst.frc.team4276.robot;

public class RoboRioPorts {

	//PWMs
		//drive system
	static int forwardRightDrive = 0;
	static int forwardLeftDrive = 1;
	static int backRightDrive = 2;
	static int backLeftDrive = 3;
	
		//shooter
	static int feederControl = 4;
	static int flyWheelControl = 9;
	
		//climber
	static int climberControl = 5;
	
		//gear arm
	static int pivotControl = 6;
	static int gearIntake = 7;
	
		//ball intake
	static int intakeControl = 8;
	
	//DIOs
		//drive system
	static int forwardLeft_channelA = 0;
	static int forwardLeft_channelB = 1;
	static int backLeft_channelA = 2;
	static int backLeft_channelB = 3;
	
		//gear arm
	static int gearArmPivot_channelA = 4;
	static int gearArmPivot_channelB = 5;
	static int gearBumperSwitch = 7;
		
		//shooter
	static int flyWheel_channelA = 8;
	static int flyWheel_channelB = 9;
	
		//sonar
	static int sonar_channelA = 8;
	static int sonar_channelB = 9;
}
