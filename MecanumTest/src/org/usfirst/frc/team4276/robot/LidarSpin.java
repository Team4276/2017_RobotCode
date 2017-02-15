package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LidarSpin {

	// We may want to navigate radially away from the boiler and if we drive
	// directly away robot frame 180.0 will be right at the boiler so a small
	// change in course would require the scanner to change sides
	// We need a hard stop at this limit for initial calibration of encoder with
	// robot frame
	// (A limit switch would be better if we can find a DIO)
	private static final double LIDAR_SCAN_MIN_DEGREES = -134.9;
	private static final double LIDAR_SCAN_MAX_DEGREES = 224.9;

	private static final double LIDAR_FIXED_DEADZONE_DEGREES = 1.0;

	private double minScanDegrees = LIDAR_SCAN_MIN_DEGREES;
	private double maxScanDegrees = LIDAR_SCAN_MAX_DEGREES;

	// Destination angle for interrupt processing
	private double desiredEncoderYaw = 0.0;

	private EncoderWithNotify enc1;
	private Relay spinner;

	public enum SpinMode {
		IDLE, SCAN, FIXED_OFFSET_FROM_YAW
	}

	public String spinModeToText(LidarSpin.SpinMode val) {
		switch (val) {
		case IDLE:
			return "IDLE";

		case SCAN:
			return "SCAN";

		case FIXED_OFFSET_FROM_YAW:
			return "FIXED_OFFSET_FROM_YAW";

		default:
			break;
		}
		return "???";
	}

	public SpinMode spinMode = SpinMode.IDLE;

	// We need to calibrate the encoder to robot frame, but we don't have a
	// DIO to dedicate for a calibration limit switch.
	// We therefore rely on the vision system to find the boiler so we can
	// set the encoder to a known +90 or -90 relative to robot frame
	// depending on blue or red alliance.
	//
	// The underlying Encoder class only allows us to reset the encoder to zero,
	// So we need to keep track of the offset between zero on the encoder and
	// robot frame
	// and use it to provide a turntable angle in robot frame
	public static double yawOffsetTurntableToRobotFrame = 0.0;

	public void resetEncoderAtOffsetDegrees(double deg) {
		enc1.reset();
		yawOffsetTurntableToRobotFrame = deg;
	}

	// Current encoder angle expressed as an offset from straight ahead robot
	// frame
	public double encoderYawDegrees() {
		double retVal = enc1.getDistance() + minScanDegrees + yawOffsetTurntableToRobotFrame;
		if (retVal > 180.0) {
			retVal -= 360.0;
		}
		else if (retVal < -180.0) {
			retVal += 360.0;
		}
		return retVal;
	}

	// Input from Vision System: Desired yaw offset from robot frame
	public void setDesiredEncoderYawDegrees(double val) {

		// Get robot frame encoder turntable yaw 
		double myVal = encoderYawDegrees();

		// Add desired offset
		myVal += (val % 360.0);
		if (myVal > 180.0) {
			myVal -= 360.0;
		}
		else if (myVal < -180.0) {
			myVal += 360.0;
		}

		// Apply scan limit
		if (myVal < minScanDegrees) {
			myVal = minScanDegrees;
		}
		if (myVal > maxScanDegrees) {
			myVal = maxScanDegrees;
		}

		desiredEncoderYaw = myVal;
		SmartDashboard.putNumber("desiredEncoderYaw", desiredEncoderYaw);
	}

	public double desiredEncoderYawDegrees() {
		return desiredEncoderYaw;
	}

	// true = clockwise || false = counterclockwise
	boolean direction = true;

	public LidarSpin(int rlay, int enc_A, int enc_B) {
		spinMode = SpinMode.IDLE;
		spinner = new Relay(rlay);
		spinner.setDirection(Relay.Direction.kBoth);
		enc1 = new EncoderWithNotify(enc_A, enc_B);
		enc1.reset();
		enc1.setDistancePerPulse(0.724346);
	}

	public void setScanLimits(double min, double max) {
		double myMin = min;
		double myMax = max;
		if (myMin < LIDAR_SCAN_MIN_DEGREES) {
			myMin = LIDAR_SCAN_MIN_DEGREES;
		}
		if (myMin >= 0.0) {
			myMin = -1.0; // Change direction requires min scan angle < zero
		}
		if (myMax > LIDAR_SCAN_MAX_DEGREES) {
			myMax = LIDAR_SCAN_MAX_DEGREES;
		}
		if (myMax < 0.0) {
			myMax = 1.0; // Change direction requires max scan angle > 0
		}
		minScanDegrees = myMin;
		maxScanDegrees = myMax;
		SmartDashboard.putString("debug", "scan limits = " + minScanDegrees + "    " + maxScanDegrees);
	}

	public double getTurntableAngle() {
		return enc1.getDistance() + minScanDegrees;
	}

	public double getMinScanDegrees() {
		return minScanDegrees;
	}

	public double getMaxScanDegrees() {
		return maxScanDegrees;
	}

	public void encoderUpdate() {
		// When the scan motor is turning this interrupt happens
		// about 60 times as often as the vision update
		// We begin scanning less often, (based on vision),
		// and use this function to stop as soon as the
		// encoder count passes the desired angle

		boolean prevDirection = direction;

		if (encoderYawDegrees() < desiredEncoderYaw) {
			direction = true;
		} else {
			direction = false;
		}
		if (direction != prevDirection) {
			spinner.set(Relay.Value.kOff);
			SmartDashboard.putString("spinner", "Interrupt Off");
		}
		SmartDashboard.putString("debug", "enc interrupt, new yaw = " + encoderYawDegrees());
	}

	void spinnerex() {
		// This is called once per vision camera frame, after the BoilerTracker
		// has updated the desired turntable angle
		double myEncoderYaw = encoderYawDegrees();
		SmartDashboard.putString("encoderYawDegrees", "yaw: " + myEncoderYaw + "   desYaw: " + desiredEncoderYaw);

		if (spinMode == SpinMode.IDLE) {
			spinner.set(Relay.Value.kOff);
			SmartDashboard.putString("spinner", "IDLE Off");

		} else if (spinMode == SpinMode.SCAN) {
			if (myEncoderYaw < minScanDegrees) {
				direction = true;
				desiredEncoderYaw = maxScanDegrees;
			} else if (myEncoderYaw > maxScanDegrees) {
				direction = false;
				desiredEncoderYaw = minScanDegrees;
			}
			if (direction) {
				spinner.set(Relay.Value.kForward);
				SmartDashboard.putString("spinner", "SCAN FWD");
			} else {
				spinner.set(Relay.Value.kReverse);
				SmartDashboard.putString("spinner", "SCAN REV");
			}
		} else if (spinMode == SpinMode.FIXED_OFFSET_FROM_YAW) {
			if (Math.abs(myEncoderYaw - desiredEncoderYaw) < LIDAR_FIXED_DEADZONE_DEGREES) {
				spinner.set(Relay.Value.kOff);
				SmartDashboard.putString("spinner", "FIX Off");
			} else {
				if (myEncoderYaw < desiredEncoderYaw) {
					direction = true;
				} else {
					direction = false;
				}
				if (direction) {
					spinner.set(Relay.Value.kForward);
					SmartDashboard.putString("spinner", "FIX FWD  yaw: " + myEncoderYaw + "   desYaw: " + desiredEncoderYaw);
				} else {
					spinner.set(Relay.Value.kReverse);
					SmartDashboard.putString("spinner", "FIX REV  yaw: " + myEncoderYaw + "   desYaw: " + desiredEncoderYaw);
				}
			}
		}
	}
}
