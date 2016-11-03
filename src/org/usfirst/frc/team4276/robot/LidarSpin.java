package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class LidarSpin {

	
	VictorSP spinner;
	Encoder enc1;
	boolean direction = true;   //true = clockwise || false = counterclockwise
	boolean slow = false;
	boolean slower = false;
	
	public LidarSpin(int one)
	{
		spinner = new VictorSP(one);
		enc1 = new Encoder(2,3);
		enc1.reset();
		enc1.setDistancePerPulse(0.724346);
	}
	void spinnerex()
	{
	double degree = enc1.getDistance()-135;	
	if (degree < 0)
	{
		if(degree < -100)
		{
			if(degree < -115)
			{
				
			slower = true;
			slow = false;
			if (degree < -130)
			{
				direction = true;
			}
			}
			else 
			{
				slow = true;
				slow = false;
			}
		}
		else 
		{
			slow = false;
		}
		
	}
	if (degree > 0)
	{
		if(degree > 100)
		{
			if(degree > 115)
			{
				
			slower = true;
			slow = false;
			if (degree > 130)
			{
				direction = false;
			}
			}
			else 
			{
				slow = true;
				slow = false;
			}
		}
		else
		{
			slow = false;
		}
		
		
	}
	SmartDashboard.putNumber("Degree", degree);
	
	
	if (direction)
	{
		if (slow)
			{
			spinner.set(-.5);
			}
		else 
		{
			spinner.set(-1);
		}
	}
	if (!direction)
	{
		if(slow)
		{
			spinner.set(.5);
		}
		else
		{
			spinner.set(1);
		}	
	}
	
	
	}
}
