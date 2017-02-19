package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;

public class SoftwareTimer {

	double expirationTime;

	static double robotTime;

	void setTimer(double timerValue) {
		robotTime = Timer.getFPGATimestamp();
		expirationTime = robotTime + timerValue;
		/*
		 * I replaced the Robot.systemTimer.get() in that equation with the
		 * double robotTime
		 * 
		 * @Brian
		 */
	}

	boolean isExpired() {
		return (robotTime > expirationTime);
		// if robotTime is greater than expirationTime, then this boolean is
		// true
	}
}
