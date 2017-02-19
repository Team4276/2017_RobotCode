package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;

public class SoftwareTimer {

	private double expirationTime;
	

	static Long robotTimeLong;
	static double robotTimeDouble;

	void setTimer(double timerValue) {
		robotTimeLong = System.currentTimeMillis();
		robotTimeDouble = robotTimeLong.doubleValue();
		expirationTime = robotTimeDouble + timerValue;
	}

	boolean isExpired() {
		return (robotTimeDouble > expirationTime);
		// if robotTime is greater than expirationTime, then this boolean is
		// true
	}
}
