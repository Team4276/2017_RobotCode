package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashboardOutput extends Thread implements Runnable{
	
	public DashboardOutput()
	{
		
	}
	
	
	
	public void run()
	{
		
		while(true)
		{
			try{
			Timer.delay(.05);
			SmartDashboard.putNumber("X", IMU.imu.getAccelX());
        	SmartDashboard.putNumber("Y", IMU.imu.getAccelY());
        	SmartDashboard.putNumber("Z", IMU.imu.getAccelZ());
        	SmartDashboard.putNumber("Roll: ", IMU.imu.getRoll());
        	SmartDashboard.putNumber("Pitch: ", IMU.imu.getPitch());
        	SmartDashboard.putNumber("Yaw: ", IMU.imu.getYaw());
        	SmartDashboard.putNumber("Yaw Rate: ", IMU.imu.getRateZ());
        	
        	SmartDashboard.putNumber("Encoder Value: ", Arm.enc.getDistance());
        	SmartDashboard.putBoolean("Switch", !ArmPID.SET_90_OPTICAL.get());
        	SmartDashboard.putNumber("Arm Angle: ", ArmPID.angle);
        	SmartDashboard.putNumber("Arm Setpoint: ", ArmPID.setpoint);
        	
        	SmartDashboard.putBoolean("Intake Limit Switch: ", Arm.limiter.get());
        	SmartDashboard.putBoolean("Auto Shoot In Progress: ", Arm.autoRun_In_Progress);
        	
        	SmartDashboard.putNumber("Drive Encoder: ", TankDrive.driveenc.getDistance());
        	
        	SmartDashboard.putNumber("Shooter Encoder: ", Shooter.shooterenc.getRate());
        	
        	SmartDashboard.putNumber("Auto Mode: ", AutoModeSwitch.getswitchvalue());
        	SmartDashboard.putString("Output Error: ", "None");
			}
			catch(Exception e)
			{
				SmartDashboard.putString("Output Error: ", "Error!");
			}

		}
		
	}

}
