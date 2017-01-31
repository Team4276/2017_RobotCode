package org.usfirst.frc.team4276.robot;

public class RobotPosition {
	public double posX;
	public double posY;
	public double hdg;
	
	RobotPosition(double Xpos, double Ypos, double heading) {
		posX = Xpos;
		posY = Ypos;
		hdg = heading;
	}
	
	public String displayText() {
		return "(" + posX + ", " + posY + "), HDG: " + hdg;
	}

}
