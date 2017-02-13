package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;

public class gearCollection {

	static VictorSP armMotor;
	static VictorSP gearIntake;
	DigitalInput gearLimitSwitch;
	static Encoder armAngle;
	
	double noGearArmPowerConstant = 0; //place holder
	double GearArmPowerConstant = 0; //place holder
	
	static final double activePowerConstant = .008; //place holder
	static final double armDeadband = 2; //degrees
	static double desiredArmAngle = 0;
	
	public gearCollection(int pwm6, int pwm7, int dio14, int dio8, int dio9)
	{
		armMotor = new VictorSP(pwm6);
		gearIntake = new VictorSP(pwm7);
		gearLimitSwitch = new DigitalInput(dio14);
		armAngle = new Encoder(dio8,dio9);
		armAngle.setDistancePerPulse(1/497); //testing needed
	}
	
	/*
	 * returns the "static power" that must be 
	 *applied to keep the arm at a fixed position
	 */
/*
	double staticArmPower() 
	{
		double power;
		double theta = armAngle.getDistance();
		if (gearLimitSwitch.get() == true)
		{
			power = GearArmPowerConstant*Math.cos(theta);
		
		}
		else
		{
			power = noGearArmPowerConstant*Math.cos(theta);
		}
		return power;
	}*/
	
	/*
	 * Proportional control to slow the motor
	 * as it gets closer to its desired position
	 */
	
	void performMainProcessing()
	{
		if(Robot.XBoxController.getRawButton(XBox.LB) == true) 
		{
			if(gearLimitSwitch.get() == true)
			{
				Robot.XBoxController.setRumble(GenericHID.RumbleType.kLeftRumble, .5);
				gearIntake.set(0);

			}
			else
			{
				Robot.XBoxController.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
				gearIntake.set(1);

			}
		}
		
		else if(Robot.XBoxController.getRawButton(XBox.RB) == true) 
		{
			Robot.XBoxController.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
			gearIntake.set(-1);

		}
		
		else
		{
			Robot.XBoxController.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
			gearIntake.set(0);

		}
	}

	static void setArmPosition(double desiredAngle)
	{
		ArmPID.setpoint = desiredAngle;
	}
	
	static void autoGearDeposit(double timeToRun)
	{
		gearIntake.set(-1);
		setArmPosition(-45);
		Timer.delay(timeToRun-.05);
		gearIntake.set(0);

	}
}
