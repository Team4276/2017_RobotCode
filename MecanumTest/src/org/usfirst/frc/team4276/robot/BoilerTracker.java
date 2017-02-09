package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BoilerTracker {

	// This class controls movement of a turntable motor upon which is mounted a
	// GRIP vision camera and a LIDAR.
	//
	// If GripVisionThread does not detect the retro-reflective tape on the
	// boiler, the turntable is moved through a scan pattern from -190 to +190
	// degrees.
	//
	// If GripVisionThread provides an X pixel, the current yaw angle is
	// sampled, and an estimate of a desired yaw angle is computed that would
	// place the X pixel
	// in the middle of the frame.

	public boolean isBoilerRangeValid = false;
	public double boilerRangeFeet = 0.0;

	public boolean isRobotPositionValid = false;
	public double fieldFrameAngleBoilerToRobotDegrees = 0.0;

	public void visionUpdate() {

		if (!Robot.isBoilerTrackerEnabled) {
			Robot.turntable1.spinMode = LidarSpin.SpinMode.IDLE;
		}
		if (Robot.turntable1.spinMode != LidarSpin.SpinMode.IDLE) {
			if (!GripVisionThread.isValidGripCameraCenterX) {
				isBoilerRangeValid = false;
				Robot.turntable1.spinMode = LidarSpin.SpinMode.SCAN;
			} else {
				boilerRangeFeet = Robot.boilerLidar.lidarDistanceCentimeters / 2.54;
				isBoilerRangeValid = (boilerRangeFeet != 0.0);

				double offCenter = GripVisionThread.degreesOffCenterX();
				double currentYaw = Robot.imu.getYaw();
				Robot.turntable1.desiredYawOffsetDegrees = currentYaw + offCenter;
				Robot.turntable1.spinMode = LidarSpin.SpinMode.FIXED_OFFSET_FROM_YAW;

				if (isBoilerRangeValid) {
					// publish robot position
					Robot.currentRobotFieldPosition.radius = boilerRangeFeet;
					Robot.currentRobotFieldPosition.hdgToBoiler = Robot.turntable1.desiredYawOffsetDegrees
							+ Robot.imu.m_yawOffsetToFieldFrame;
					Robot.currentRobotFieldPosition.yawOffsetRobot = currentYaw - Robot.imu.m_yawOffsetToFieldFrame;
					Robot.currentRobotFieldPosition.isBlueBoiler = (Robot.currentRobotFieldPosition.hdgToBoiler < 90);

					SmartDashboard.putString("Robot Field Position", Robot.currentRobotFieldPosition.displayText());
				} else {
					// Robot position is not valid
					Robot.isValidCurrentRobotFieldPosition = false;
					SmartDashboard.putString("Robot Field Position", "*** Not valid ***");

				}
			}
		}
	}
}
