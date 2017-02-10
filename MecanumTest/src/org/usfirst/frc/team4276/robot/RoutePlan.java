package org.usfirst.frc.team4276.robot;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class RoutePlan extends ArrayList<RouteTask> {
	public enum AllianceColor {
		UNKNOWN, BLUE, RED
	}

	public String name;
	public AllianceColor allianceColor;

	RoutePlan(String planName, AllianceColor col) {
		name = planName;
		allianceColor = col;
	}

	public String displayText() {
		String sRet = name + "\n";
		for (int i = 0; i < size(); i++) {
			RouteTask myTask = get(i);
			sRet += myTask.displayText();
		}
		return sRet;
	}
}
