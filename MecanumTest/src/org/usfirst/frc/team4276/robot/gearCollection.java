package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;

public class gearCollection {

	VictorSP armRotation;
	static Relay gearIntakeLeft;
	static Relay gearIntakeRight;
	DigitalInput gearIn;
	Encoder armAngle;
	Joystick XBox1;
	
	double noGearArmPowerConstant = .5; //place holder
	double GearArmPowerConstant = .75; //place holder
	
	public gearCollection(int pwm6, int relay1, int relay2, int dio14, int dio8, int dio9)
	{
		armRotation = new VictorSP(pwm6);
		gearIntakeLeft = new Relay(relay1);
		gearIntakeRight = new Relay(relay2);
		gearIn = new DigitalInput(dio14);
		armAngle = new Encoder(dio8,dio9);
		armAngle.setDistancePerPulse(.01); //place holder
	}
	
	/*
	 * returns the "static power" that must be 
	 *applied to keep the arm at a fixed position
	 */
	double staticArmPower() 
	{
		double power;
		double theta = armAngle.getDistance();
		if (gearIn.get() == true)
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
		
		if(XBox1.getRawAxis(XBox.LStickY) > .1)
		{
			desiredArmAngle++;
		}
			else if(XBox1.getRawAxis(XBox.LStickY) < -.1)
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
		armRotation.set(netPower);
	}
	
	void gearCollection()
	{
		if(XBox1.getRawButton(XBox.LB) == true)
		{
			if(gearIn.get() == true)
			{
				XBox1.setRumble(GenericHID.RumbleType.kLeftRumble, .5);
				gearIntakeLeft.set(Value.kOff);
				gearIntakeRight.set(Value.kOff);
			}
			else
			{
				gearIntakeLeft.set(Value.kForward);
				gearIntakeRight.set(Value.kForward);
			}
		}
		
		else if(XBox1.getRawButton(XBox.RB) == true)
		{
			gearIntakeLeft.set(Value.kReverse);
			gearIntakeRight.set(Value.kReverse);
		}
		
		else
		{
			gearIntakeLeft.set(Value.kOff);
			gearIntakeRight.set(Value.kOff);
		}
		
		SmartDashboard.putBoolean("Gear Collected?", gearIn.get());
		
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
