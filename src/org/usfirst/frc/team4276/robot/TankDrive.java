package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TankDrive {
	
	static Talon FR;
	static Talon MR;
	static Talon BR;
	static Talon FL;
	static Talon ML;
	static Talon BL;
	Joystick JR;
	Joystick JL;
	Timer time;
	double tick = Timer.getFPGATimestamp();
	double rightpower;
	double leftpower;
	double mode = 1;
	static Encoder driveenc;

	
	
	public TankDrive(int fr, int mr, int br, int fl, int ml, int bl,int enc1,int enc2)
	{
		FR = new Talon(fr);
		MR = new Talon(mr);
		BR = new Talon(br);
		FL = new Talon(fl);
		ML = new Talon(ml);
		BL = new Talon(bl);
		driveenc=new Encoder(enc1,enc2);
		JL = new Joystick(0);
		JR = new Joystick(1);
		time = new Timer();
		time.reset();
		time.start();
		tick=time.get();
		driveenc.setDistancePerPulse(1);
		driveenc.reset();

	}
	
	public void run()
	{
		
		
		SmartDashboard.putNumber("Drive Encoder", driveenc.getDistance());
		SmartDashboard.putNumber("X", IMU.imu.getAccelX());
    	SmartDashboard.putNumber("Y", IMU.imu.getAccelY());
    	SmartDashboard.putNumber("Z", IMU.imu.getAccelZ());
    	SmartDashboard.putNumber("Yaw: ", IMU.imu.getYaw());
    	SmartDashboard.putNumber("Yaw Rate: ", IMU.imu.getRateZ());

		if(Math.abs(JR.getY()) > .2 || Math.abs(JL.getY()) > .2)
		{
			rightpower = JR.getY();
			leftpower = JL.getY();
			
		}
		else
		{
			rightpower=0;
			leftpower=0;
			
		}
		
		fullpower();
		
	}
	
	public void Powermode()
	{
		 
		 
		 if(time.get() - tick >= 10)
		 {
			 time.reset();
			 tick=0;
			 if(mode >= 3)
			 {
				mode = 1;
			
			 }
			 else
				 {
				 
				 
				 mode = mode + 1;
				 }
			 
		 }
		 if(mode >= 3)
		 {
			
			FR.set(rightpower);
			MR.set(rightpower);
			BR.set(0);
			FL.set(leftpower);
			ML.set(leftpower);
			BL.set(0);
		 }
		 if(mode == 2)
	 	 {
		 	FR.set(rightpower);
			MR.set(0);
			BR.set(rightpower);
			FL.set(leftpower);
			ML.set(0);
			BL.set(leftpower);
	 	 }
		 if(mode == 1)
		 {
			 	FR.set(0);
				MR.set(rightpower);
				BR.set(rightpower);
				FL.set(0);
				ML.set(leftpower);
				BL.set(leftpower);
		 }
		 	
	}
	public void fullpower()
	{
		FR.set(rightpower);
		MR.set(rightpower);
		BR.set(rightpower);
		FL.set(-leftpower);
		ML.set(-leftpower);
		BL.set(-leftpower);
		
		SmartDashboard.putNumber("Drive Encoder", driveenc.getDistance());
	}
	static boolean autodrive(double dist, double power, double desiredangle)
	{
		double deadband = .05;
		double biaz = .1;
		double dif = IMU.imu.getAngleZ() - desiredangle;
		double k=0.004;
		SmartDashboard.putNumber("Drive Encoder", driveenc.getDistance());
		SmartDashboard.putNumber("Yaw", IMU.imu.getAngleZ());
		
		
		
		double Rpower = power;
		double Lpower = power;
		
		
		if(dif> deadband) // Driving to the right
		{
			Lpower = Lpower - dif*k;
		}
		if(dif< -1*deadband) // Driving to the left
		{
			Rpower = Rpower - Math.abs(dif)*k;
		}
		SmartDashboard.putNumber("LPower: ", Lpower);
		SmartDashboard.putNumber("RPower: ", Rpower);
		
		if(driveenc.getDistance()<dist)
		{
			FR.set(-Rpower);			
			MR.set(-Rpower);
			BR.set(-Rpower);
			FL.set(Lpower);
			ML.set(Lpower);
			BL.set(Lpower);
			return false;
		} else if(dist == 0 && dif > deadband)
		{
			FR.set(dif/180);			
			MR.set(dif/180);
			BR.set(dif/180);
			FL.set(dif/180);
			ML.set(dif/180);
			BL.set(dif/180);
			return false;
		}
		else
		{
			FR.set(0);			
			MR.set(0);
			BR.set(0);
			FL.set(0);
			ML.set(0);
			BL.set(0);
			return true;
		}
		
		
	}
	public void drive()
	{
		if(!JR.getRawButton(1))
		{
			fullpower();
		}
		else
		{
			Powermode();
		}
	}
	
}
