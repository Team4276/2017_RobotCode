package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LidarSpin {
	
	private static final int LIDAR_SCAN_MIN_DEGREES = -190;
	private static final int LIDAR_SCAN_MAX_DEGREES = 190;

	private static final double LIDAR_FIXED_DEADZONE_DEGREES = 1.0;
	
	private double minScanDegrees = LIDAR_SCAN_MIN_DEGREES;	
	private double maxScanDegrees = LIDAR_SCAN_MAX_DEGREES;
	
	public enum SpinMode {
		IDLE, SCAN, FIXED_OFFSET_FROM_YAW
	}

	public SpinMode spinMode = SpinMode.IDLE;
	
	public double yawOffsetDegrees = 0.0;	
	public double desiredYawOffsetDegrees = 0.0;	
	

	
	Relay spinner;
	Encoder enc1;
	boolean direction = true; // true = clockwise || false = counterclockwise
	public LidarSpin(int rlay, int enc_A, int enc_B) {
		spinMode = SpinMode.IDLE;
		spinner = new Relay(rlay);
		spinner.setDirection(Relay.Direction.kBoth);
		enc1 = new Encoder(2, 3);
		enc1.reset();
		enc1.setDistancePerPulse(0.724346);
	}
	
	public void setScanLimits(double min, double max) {
		double myMin = min;
		double myMax = max;
		if(myMin < LIDAR_SCAN_MIN_DEGREES) {
			myMin = LIDAR_SCAN_MIN_DEGREES;
		}
		if(myMax < LIDAR_SCAN_MAX_DEGREES) {
			myMax = LIDAR_SCAN_MAX_DEGREES;
		}
		minScanDegrees = myMin;	
		maxScanDegrees = myMax;
	}
	
	public double getTurntableAngle() {
		return enc1.getDistance() - 180;
	}
	public double getMinScanDegrees() {
		return minScanDegrees;
	}
	public double getMaxScanDegrees() {
		return maxScanDegrees;
	}

	void spinnerex() {
		yawOffsetDegrees = enc1.getDistance() - 180;
		SmartDashboard.putNumber("Robot Frame Turntable Angle", yawOffsetDegrees);
		
		if(spinMode == SpinMode.IDLE) {
			spinner.set(Relay.Value.kOff);
			
		} else if(spinMode == SpinMode.SCAN) {
			if (yawOffsetDegrees < 0) {
				if (yawOffsetDegrees < minScanDegrees) {
					direction = true;
				}	
			}
			if (yawOffsetDegrees > 0) {
				if (yawOffsetDegrees > maxScanDegrees) {
					direction = false;
				}
			}
			if (direction) {
				spinner.set(Relay.Value.kReverse);
			} else {
				spinner.set(Relay.Value.kForward);
			}
			
		} else if(spinMode == SpinMode.FIXED_OFFSET_FROM_YAW) {
			double desiredYaw = Robot.imu.getYaw() + desiredYawOffsetDegrees;
			if(Math.abs(yawOffsetDegrees - desiredYaw) < LIDAR_FIXED_DEADZONE_DEGREES) {
				spinner.set(Relay.Value.kOff);
			} else {			
				if(yawOffsetDegrees < desiredYaw) {
					direction = true;							
				} else {
					direction = false;
				}
				if (direction) {
					spinner.set(Relay.Value.kReverse);
				} else {
					spinner.set(Relay.Value.kForward);
				}
			}
		}
	}
}
