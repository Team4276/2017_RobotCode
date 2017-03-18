package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutoSelector extends Thread implements Runnable {

	SendableChooser autoSequenceChooser;
	SendableChooser allianceChooser;
	SendableChooser selectionModeChooser;

	private int alliance;
	private int selectionMode;

	private final int RED = 0;
	private final int BLUE = 1;
	private String[] allianceArray = new String[2];

	private final int COMMIT_MODE = 0;
	private final int EDIT_MODE = 1;
	private String[] selectionModeArray = new String[2];

	public AutoSelector() {
		autoSequenceChooser = new SendableChooser();

		allianceChooser = new SendableChooser();
		allianceChooser.addDefault("Red", RED);
		allianceChooser.addObject("Blue", BLUE);
		SmartDashboard.putData("Alliance Selection", allianceChooser);

		selectionModeChooser = new SendableChooser();
		selectionModeChooser.addDefault("Commit selections", COMMIT_MODE);
		selectionModeChooser.addObject("Edit selections", EDIT_MODE);
		SmartDashboard.putData("Commit Selections", selectionModeChooser);

		allianceArray[RED] = "Red";
		allianceArray[BLUE] = "Blue";

		selectionModeArray[COMMIT_MODE] = "Commit mode";
		selectionModeArray[EDIT_MODE] = "Edit mode";
	}

	public void run() {
		while (true) {
			if (selectionMode == COMMIT_MODE) {
				selectionMode = (int) selectionModeChooser.getSelected();
				SmartDashboard.putString("Selection mode", selectionModeArray[selectionMode]);
			} else {
				alliance = (int) allianceChooser.getSelected();
				selectionMode = (int) selectionModeChooser.getSelected();
				SmartDashboard.putString("Alliance color", allianceArray[alliance]);
				SmartDashboard.putString("Selection mode", selectionModeArray[selectionMode]);

			}
		}
	}

}
