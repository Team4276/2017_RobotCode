package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
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

	boolean direction = true; // true = clockwise || false = counterclockwise
	public double yawOffsetDegrees = 0.0; // Current encoder angle expressed as
											// an offset from straight ahead
											// robot frame
	public double desiredYawOffsetDegrees = 0.0; // Angle to boiler vision
													// target, from
													// BoilerTracker

	Relay spinner;

	class EncoderWithNotify extends Encoder {

		public double desiredEncoderAngle = 0.0;

		EncoderWithNotify(int enc_A, int enc_B) {
			super(enc_A, enc_B, false);

			SmartDashboard.putString("debug", "enc_A, enc_B " + enc_A + "    " + enc_B);

			// Register an interrupt handler
			m_aSource.requestInterrupts(new InterruptHandlerFunction<Object>() {

				@Override
				public void interruptFired(int interruptAssertedMask, Object param) {
					// When the scan motor is turning this interrupt happens
					// about 60 times as often as the vision update
					// We begin scanning less often, (based on vision),
					// and use this function to stop as soon as the
					// encoder count passes the desired angle
					boolean prevDirection = direction;
					yawOffsetDegrees = enc1.getDistance() - 180;
					if (yawOffsetDegrees < desiredEncoderAngle) {
						direction = true;
					} else {
						direction = false;
					}
					if (direction != prevDirection) {
						spinner.set(Relay.Value.kOff);
					}
				}
			});
			// Listen for a falling edge
			m_aSource.setUpSourceEdge(false, true);
			// Enable digital interrupt pin
			m_aSource.enableInterrupts();
		}
	}

	EncoderWithNotify enc1;

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
			myMin = -1.0; // Change direction requires min angle less than zero
		}
		if (myMax < LIDAR_SCAN_MAX_DEGREES) {
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
		// This is called once per vision camera frame, after the BoilerTracker
		// has updated the desired turntable angle
		yawOffsetDegrees = enc1.getDistance() - 180;
		SmartDashboard.putNumber("Robot Frame Turntable Angle", yawOffsetDegrees);

		if (spinMode == SpinMode.IDLE) {
			spinner.set(Relay.Value.kOff);

		} else if (spinMode == SpinMode.SCAN) {
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
				spinner.set(Relay.Value.kForward);
			} else {
				spinner.set(Relay.Value.kReverse);
			}

		} else if (spinMode == SpinMode.FIXED_OFFSET_FROM_YAW) {
			enc1.desiredEncoderAngle = Robot.imu.getYaw() + desiredYawOffsetDegrees;
			if (Math.abs(yawOffsetDegrees - enc1.desiredEncoderAngle) < LIDAR_FIXED_DEADZONE_DEGREES) {
				spinner.set(Relay.Value.kOff);
			} else {
				if (yawOffsetDegrees < enc1.desiredEncoderAngle) {
					direction = true;
				} else {
					direction = false;
				}
				if (direction) {
					spinner.set(Relay.Value.kForward);
				} else {
					spinner.set(Relay.Value.kReverse);
				}
			}
		}
	}
}
