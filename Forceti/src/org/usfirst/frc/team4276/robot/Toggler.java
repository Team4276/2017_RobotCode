package org.usfirst.frc.team4276.robot;

public class Toggler {

	private int controlPoint;
	private String controlType;
	private int controlPointTarget;
	private boolean state = false;

	private boolean currentControlPointStatus = false;
	private boolean previousControlPointStatus;

	public Toggler(int controlInputName, String inputType, int desiredValue) {
		controlPoint = controlInputName;
		controlType = inputType;
		controlPointTarget = desiredValue;
	}

	void updateMechanismState() {

		if (controlType == XBox.BUTTON) {
			previousControlPointStatus = currentControlPointStatus;
			currentControlPointStatus = Robot.XBoxController.getRawButton(controlPoint);

			if (currentControlPointStatus == true) {
				if (previousControlPointStatus == false) {
					if (state == true) {
						state = false;
					} else {
						state = true;
					}
				}
			}
		}
		else if (controlType == XBox.POV) {
			previousControlPointStatus = currentControlPointStatus;
			currentControlPointStatus = (controlPointTarget == Robot.XBoxController.getPOV(controlPoint));

			if (currentControlPointStatus == true) {
				if (previousControlPointStatus == false) {
					if (state == true) {
						state = false;
					} else {
						state = true;
					}
				}
			}
		}
	}

	boolean getMechanismState() {
		return state;
	}
}
