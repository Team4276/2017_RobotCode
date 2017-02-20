package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArmPID extends Thread implements Runnable {

	
	final double raisedSetPoint = 0.0;
	final double middleSetPoint = -45.0;
	final double collectingSetPoint = -90.0;
	double estimatedArmAngle;
	double ang;
	final double CHAIN_SLACK_ANGLE = 8.0; // degrees
	static double initialArmAngle = 0;// + CHAIN_SLACK_ANGLE; // degrees
	static double commandedArmAngle = initialArmAngle;
	final double TARGETING_ERROR = 0.0; // degrees
	
	final double upperLimit = 0.0;
	final double lowerLimit = -90.0;

	public void run() {
		double errorProportional;

		final double kUp = .025;
		final double kDown = .04;
		double deadband = 1;
		double power;

		try {
			while (true) {
				if (Robot.XBoxController.getRawButton(XBox.Start)) {
					gearCollection.armMotor.set(Robot.XBoxController.getRawAxis(XBox.LStickY));
				} else {
					double encoderAngle = gearCollection.armAngle.getDistance();
					estimatedArmAngle = initialArmAngle - encoderAngle;

					errorProportional = commandedArmAngle - estimatedArmAngle;

					if (errorProportional < -deadband) {
						power = kDown * errorProportional;
					} else if (errorProportional > deadband) {
						power = kUp * errorProportional;
					} else {
						power = 0;
					}
					gearCollection.armMotor.set(-power);
					if (Robot.XBoxController.getRawAxis(XBox.LStickY) > 0.5)
						commandedArmAngle -= 5;
					else if (Robot.XBoxController.getRawAxis(XBox.LStickY) < -0.5)
						commandedArmAngle += 5;

					if (Robot.XBoxController.getRawButton(XBox.Back) && Robot.XBoxController.getRawButton(XBox.Start)) {
						initialArmAngle++;
					} else if (Robot.XBoxController.getRawButton(XBox.Back)) {
						initialArmAngle--;
					}

					/*
					 * if (setpoint >= 0 + CHAIN_SLACK_ANGLE + TARGETING_ERROR)
					 * setpoint = 0 + CHAIN_SLACK_ANGLE + TARGETING_ERROR; if
					 * (setpoint <= -90 - TARGETING_ERROR) setpoint = -90 -
					 * TARGETING_ERROR; if
					 * (Robot.XBoxController.getRawButton(XBox.Y)) setpoint = 0
					 * + CHAIN_SLACK_ANGLE + TARGETING_ERROR; if
					 * (Robot.XBoxController.getRawButton(XBox.X)) setpoint =
					 * -90 - TARGETING_ERROR;
					 */

					if (commandedArmAngle >= upperLimit)
						commandedArmAngle = upperLimit;
					if (commandedArmAngle <= -lowerLimit)
						commandedArmAngle = -lowerLimit;
					if (Robot.XBoxController.getRawButton(JoystickMappings.gearArmUp))
						commandedArmAngle = raisedSetPoint;
					if (Robot.XBoxController.getRawButton(JoystickMappings.gearArmMiddle))
						commandedArmAngle = middleSetPoint;
					if (Robot.XBoxController.getRawButton(JoystickMappings.gearArmDown))
						commandedArmAngle = collectingSetPoint;

					SmartDashboard.putNumber("Arm Offset: ", errorProportional);
					SmartDashboard.putNumber("Setpoint: ", commandedArmAngle);
					SmartDashboard.putNumber("Power: ", power);
					SmartDashboard.putNumber("Arm Angle: ", estimatedArmAngle);
					SmartDashboard.putNumber("Encoder Value: ", gearCollection.armAngle.getDistance());
					SmartDashboard.putNumber("Arm Start Angle", initialArmAngle);

				}
				Timer.delay(0.05);
			}

		}

		catch (Exception e) {

		}
	}

}