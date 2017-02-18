package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArmPID extends Thread implements Runnable {

	static double angle;
	static double ang;
	static final double CHAIN_SLACK_ANGLE = 8.0; // degrees
	static double startang = 0;// + CHAIN_SLACK_ANGLE; // degrees
	static double setpoint = startang;
	static final double TARGETING_ERROR = 0.0; // degrees

	public void run() {
		double offset;

		double kUp = .025;
		double kDown = .04;
		double deadband = 1;
		double power;

		try {
			while (true) {
				if (Robot.XBoxController.getRawButton(XBox.Start)) {
					gearCollection.armMotor.set(Robot.XBoxController.getRawAxis(XBox.LStickY));
				} else {
					angle = startang - (gearCollection.armAngle.getDistance());

					offset = setpoint - angle;

					if (offset < -deadband) {
						power = kDown * offset;
					} else if (offset > deadband) {
						power = kUp * offset;
					} else {
						power = 0;
					}
					gearCollection.armMotor.set(-power);
					if (Robot.XBoxController.getRawAxis(XBox.LStickY) > 0.5)
						setpoint -= 5;
					else if (Robot.XBoxController.getRawAxis(XBox.LStickY) < -0.5)
						setpoint += 5;

					if (Robot.XBoxController.getRawButton(XBox.Back) && Robot.XBoxController.getRawButton(XBox.Start)) {
						startang++;
					} else if (Robot.XBoxController.getRawButton(XBox.Back)) {
						startang--;
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

					if (setpoint >= 0)
						setpoint = 0;
					if (setpoint <= -95)
						setpoint = -95;
					if (Robot.XBoxController.getRawButton(XBox.Y))
						setpoint = 0;
					if (Robot.XBoxController.getRawButton(XBox.X))
						setpoint = -95;

					SmartDashboard.putNumber("Arm Offset: ", offset);
					SmartDashboard.putNumber("Setpoint: ", setpoint);
					SmartDashboard.putNumber("Power: ", power);
					SmartDashboard.putNumber("Arm Angle: ", angle);
					SmartDashboard.putNumber("Encoder Value: ", gearCollection.armAngle.getDistance());
					SmartDashboard.putNumber("Arm Start Angle", startang);

				}
				Timer.delay(0.05);
			}

		}

		catch (Exception e) {

		}
	}

}