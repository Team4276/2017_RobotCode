package org.usfirst.frc.team4276.robot;

public class IMU {
	
	static ADIS16448_IMU imu;
	
	public IMU()
	{
		imu = new ADIS16448_IMU();
	}

}
