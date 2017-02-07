package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.DigitalInput;
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
	public double yawOffsetDegrees = 0.0;
	public double desiredYawOffsetDegrees = 0.0;

	Relay spinner;

	class EncoderWithNotify extends Encoder {
		
		public double desiredEncoderAngle = 0.0;

		EncoderWithNotify(int dio1, int dio2) {
			super(dio1, dio2);

			DigitalInput interrupt = new DigitalInput(dio1);

			// Register an interrupt handler
			interrupt.requestInterrupts(new InterruptHandlerFunction<Object>() {

				@Override
				public void interruptFired(int interruptAssertedMask, Object param) {
					// When the scan motor is on this interrupt happens about 60 times as often as the vision update
					// We start scanning less often, (based on vision), and use this function to stop as soon 
					// as the encoder count passes the desired angle
					boolean prevDirection = direction;
					yawOffsetDegrees = enc1.getDistance() - 180;
					if (yawOffsetDegrees < desiredEncoderAngle) {
						direction = true;
					} else {
						direction = false;
					}
					if(direction != prevDirection) {
						spinner.set(Relay.Value.kOff);						
					}
				}
			});
			// Listen for a falling edge
			interrupt.setUpSourceEdge(false, true);
			// Enable digital interrupt pin
			interrupt.enableInterrupts();
		}
	}

	EncoderWithNotify enc1;

	public LidarSpin(int rlay, int enc_A, int enc_B) {
		spinMode = SpinMode.IDLE;
		spinner = new Relay(rlay);
		spinner.setDirection(Relay.Direction.kBoth);
		enc1 = new EncoderWithNotify(2, 3);
		enc1.reset();
		enc1.setDistancePerPulse(0.724346);
	}

	public void setScanLimits(double min, double max) {
		double myMin = min;
		double myMax = max;
		if (myMin < LIDAR_SCAN_MIN_DEGREES) {
			myMin = LIDAR_SCAN_MIN_DEGREES;
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
				spinner.set(Relay.Value.kReverse);
			} else {
				spinner.set(Relay.Value.kForward);
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
					spinner.set(Relay.Value.kReverse);
				} else {
					spinner.set(Relay.Value.kForward);
				}
			}
		}
	}
}
