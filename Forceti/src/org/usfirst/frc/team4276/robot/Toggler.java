package org.usfirst.frc.team4276.robot;

public class Toggler {

	static int button;
	static boolean state = false;

	static boolean currentButtonStatus = false;
	static boolean previousButtonStatus;

	public Toggler(int joystickButton) {
		button = joystickButton;
	}

	void updateMechanismState() {
		previousButtonStatus = currentButtonStatus;
		currentButtonStatus = Robot.XBoxController.getRawButton(button);

		if (currentButtonStatus == true) {
			if (previousButtonStatus == false) {
				if (state == true) {
					state = false;
				} else {
					state = true;
				}
			}
		}
	}

	boolean getMechanismState() {
		return state;
	}
}
