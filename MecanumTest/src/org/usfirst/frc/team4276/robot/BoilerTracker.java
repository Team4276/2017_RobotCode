package org.usfirst.frc.team4276.robot;

import org.usfirst.frc.team4276.robot.RoutePlan.AllianceColor;

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

	public Boolean isBoilerRangeValid = false;
	public double boilerRangeFeet = 0.0;

	public Boolean isRobotPositionValid = false;
	public double fieldFrameAngleBoilerToRobotDegrees = 0.0;

	public void visionUpdate() {

		if (!Robot.isBoilerTrackerEnabled) {
			Robot.turntable1.spinMode = LidarSpin.SpinMode.IDLE;
		}		
		if(Robot.turntable1.spinMode != LidarSpin.SpinMode.IDLE) {
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
				
				if(isBoilerRangeValid) {
					// Convert polar to rectangular coords and publish robot position
					double boiler_X = -12.5;
					double boiler_Y = -12.5;					
					if(Robot.planForThisMatch.allianceColor == AllianceColor.BLUE) {
						boiler_X = 12.5;
						boiler_Y = 12.5;					
					}
					
					double fieldRelativeTurntableHeadingDegrees = Robot.turntable1.desiredYawOffsetDegrees + Robot.imu.m_yawOffsetToFieldFrame;
					if(fieldRelativeTurntableHeadingDegrees < -180.0) {
						fieldRelativeTurntableHeadingDegrees += 360;
					} else if(fieldRelativeTurntableHeadingDegrees > 180.0) {
						fieldRelativeTurntableHeadingDegrees -= 360;
					}
					
					double fieldRelativeHeadingBoilerToRobot = fieldRelativeTurntableHeadingDegrees - 180;
					if(fieldRelativeHeadingBoilerToRobot < -180.0) {
						fieldRelativeHeadingBoilerToRobot += 360;
					} else if(fieldRelativeHeadingBoilerToRobot > 180.0) {
						fieldRelativeHeadingBoilerToRobot -= 360;
					}

					Robot.currentRobotFieldPosition.posX = boiler_X + ( boilerRangeFeet * Math.cos(fieldRelativeHeadingBoilerToRobot));
					Robot.currentRobotFieldPosition.posY = boiler_Y + ( boilerRangeFeet * Math.sin(fieldRelativeHeadingBoilerToRobot));
					Robot.currentRobotFieldPosition.hdg = currentYaw + Robot.imu.m_yawOffsetToFieldFrame;
					Robot.isValidCurrentRobotFieldPosition = true;
					
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
