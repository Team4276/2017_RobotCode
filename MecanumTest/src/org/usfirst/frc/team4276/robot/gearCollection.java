package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;

public class gearCollection {

	VictorSP armMotor;
	static Relay gearIntakeLeft;
	static Relay gearIntakeRight;
	DigitalInput gearLimitSwitch;
	Encoder armAngle;
	
	double noGearArmPowerConstant = .5; //place holder
	double GearArmPowerConstant = .75; //place holder
	
	public gearCollection(int pwm6, int relay1, int relay2, int dio14, int dio11, int dio12)
	{
		try {
			armMotor = new VictorSP(pwm6);
			gearIntakeLeft = new Relay(relay1);
			gearIntakeRight = new Relay(relay2);
			gearLimitSwitch = new DigitalInput(dio14);
			armAngle = new Encoder(dio11,dio12);
			armAngle.setDistancePerPulse(.01); //place holder
		} catch(Exception e) {
			SmartDashboard.putString("debug", "gearCollection constructor failed");
		}
	}
	
	/*
	 * returns the "static power" that must be 
	 *applied to keep the arm at a fixed position
	 */
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
	}
	
	/*
	 * Proportional control to slow the motor
	 * as it gets closer to its desired position
	 * which is controlled by the XBox L-Y-Axis
	 */
	
	double activeArmPower()
	{
		double power = 0;
		double activePowerConstant = .008; //place holder
		double armDeadband = 2; //degrees
		double desiredArmAngle = 0;
		
		if(Robot.XBoxController.getRawAxis(XBox.LStickY) > .1)
		{
			desiredArmAngle++;
		}
			else if(Robot.XBoxController.getRawAxis(XBox.LStickY) < -.1)
			{
				desiredArmAngle--;
			}
		if(desiredArmAngle > 0)
		{
		desiredArmAngle = 0;
		}
		else if(desiredArmAngle < -90)
		{
		desiredArmAngle = -90;
		}
		
		if(Math.abs(armAngle.getDistance() - desiredArmAngle) > armDeadband)
		{
			power = (desiredArmAngle - armAngle.getDistance())*activePowerConstant;
		}
		
		SmartDashboard.putNumber("Arm angle:", armAngle.getDistance());
		
		return power;
	}
	/*
	 * combines the static and active powers
	 * to apply the accurate motor power to
	 * either move the arm, or maintain its
	 * position
	 */
	void runArm()
	{
		double netPower = staticArmPower() + activeArmPower();
		armMotor.set(netPower);
	}
	
	void performMainProcessing()
	{
		try {
			if(Robot.XBoxController.getRawButton(1) == true) // button 1 =place holder
			{
				if(gearLimitSwitch.get() == true)
				{
					Robot.XBoxController.setRumble(GenericHID.RumbleType.kLeftRumble, .5);
					gearIntakeLeft.set(Value.kOff);
					gearIntakeRight.set(Value.kOff);
				}
				else
				{
					Robot.XBoxController.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
					gearIntakeLeft.set(Value.kForward);
					gearIntakeRight.set(Value.kForward);
				}
			}
			
			else if(Robot.XBoxController.getRawButton(2) == true) // button 2 =place holder
			{
				Robot.XBoxController.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
				gearIntakeLeft.set(Value.kReverse);
				gearIntakeRight.set(Value.kReverse);
			}
			
			else
			{
				Robot.XBoxController.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
				gearIntakeLeft.set(Value.kOff);
				gearIntakeRight.set(Value.kOff);
			}
		} catch(Exception e) {
			SmartDashboard.putString("debug", "gearCollection.performMainProcessing failed");
		}
	}

	static void autoGearDeposit(double timeToRun)
	{
		gearIntakeLeft.set(Value.kReverse);
		gearIntakeRight.set(Value.kReverse);
		Timer.delay(timeToRun-.05);
		gearIntakeLeft.set(Value.kOff);
		gearIntakeRight.set(Value.kOff);
	}
}
