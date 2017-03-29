package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber {

	VictorSP climber;

	final static double LIMIT_SWITCH_DELAY = 1.0; // seconds
	final static double CLIMBER_POWER_FAST = -1.0; // -1.0 to 1.0
	final static double CLIMBER_POWER_SLOW = -.25;
	final static double RELEASE_POWER = .40;


	public Climber(int pwm5) {

		climber = new VictorSP(pwm5);
		
	}

	void driveInReverse() {

		if ((Robot.XBoxController.getRawAxis(XBox.RStickY) > 0.5)) {
			
				climber.set(RELEASE_POWER);
			
		}

		else {
			climber.set(0.0);
		}
		// driveInReverse();
		SmartDashboard.putBoolean("Climber Status:", (Robot.XBoxController.getRawAxis(XBox.RStickY) > 0.5));
	}

	void performMainProcessing() {

		if (Robot.XBoxController.getRawAxis(XBox.RStickY) > 0.5) {

			if (Robot.XBoxController.getRawButton(XBox.RStick) == true) {
				climber.set(CLIMBER_POWER_FAST);
			}
			else
			{
				climber.set(CLIMBER_POWER_SLOW);
			}
		}

		else {
			climber.set(0.0);
		}
		// driveInReverse();
		//SmartDashboard.putBoolean("Climber Status:", climberToggler.getMechanismState());
	}
}
