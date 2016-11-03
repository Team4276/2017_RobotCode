package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoNode {
	static double Ycoordcur = 0;
	static double Xcoordcur = 0;
	
	
	
	
	
	public boolean Node(double Y , double X, double armang, double power)
	{
		double Xdif = X-Xcoordcur;
		double Ydif = Y-Ycoordcur;
		double heading = Math.atan(Ydif/Xdif);
		double distance = Math.sqrt((Xdif*Xdif) + (Ydif*Ydif));
		//ArmPID.armangle(armang);
		if(TankDrive.autodrive(distance, power, heading))
		{
			Ycoordcur = Y;
			Xcoordcur = X;
			return true;
		}
		else 
			{
			SmartDashboard.putNumber("Auto", Xcoordcur);
			return false;
			}
		
	}
	
	static boolean shooterAligner()
	{
		double desireddist = 175.26;
		double deadpoolband = 2;
		if(Math.abs(Robot.g_lidarDistanceCentimeters - desireddist) > deadpoolband)
				{
			TankDrive.autodrive( Robot.g_lidarDistanceCentimeters - desireddist, .3, 0);
			return false;
				}
		else return true;
	}
	
	void NodeEnd()
	{
		TankDrive.autodrive(0, 0, 0);
		//ArmPID.armangle(0);
	}
	
}
