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

		SmartDashboard.putBoolean("isValidGripCameraCenterX:  ", GripVisionThread.isValidGripCameraCenterX);

		if (!GripVisionThread.isValidGripCameraCenterX) {
			isBoilerRangeValid = false;
			Robot.isValidCurrentRobotFieldPosition = false;
			SmartDashboard.putString("Robot Field Position", "*** Not valid ***");
		} else {
			boilerRangeFeet = Robot.boilerLidar.lidarDistanceCentimeters / 2.54;
			isBoilerRangeValid = (boilerRangeFeet != 0.0);
			if(isBoilerRangeValid) {
				SmartDashboard.putNumber("LIDAR Range feet", boilerRangeFeet);
			} else {
				SmartDashboard.putString("LIDAR Range feet", "*** Not valid ***");
			}

			double offCenter = GripVisionThread.degreesOffCenterX();
			double currentYaw = Robot.imu.getYaw() + Robot.turntable1.encoderYawDegrees();
			Robot.turntable1.setDesiredEncoderYawDegrees(currentYaw + offCenter);

			if (isBoilerRangeValid) {
				// publish robot position
				Robot.currentRobotFieldPosition.radius = boilerRangeFeet;

				// rotational orientation of the robot at this position
				Robot.currentRobotFieldPosition.yawOffsetRobot = currentYaw;

				// turntable angle in robot frame
				Robot.currentRobotFieldPosition.hdgToBoiler = Robot.turntable1.encoderYawDegrees();

				// turntable angle wherever the front of the robot is pointing
				Robot.currentRobotFieldPosition.hdgToBoiler += Robot.imu.getYaw();
				
				// turntable angle in field frame
				Robot.currentRobotFieldPosition.hdgToBoiler += Robot.yawOffsetToFieldFrame; 

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
