package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShoot extends Thread implements Runnable {
	
	//Arm arm;
	Shooter shooter;
	
	
	public AutoShoot()
	{

	}
	
	public void run()
	{
		try
		{
			Arm.autoRun_In_Progress=true;
			timedShoot();
			Arm.autoRun_In_Progress=false;
		}
		
		
		catch(Exception e)
		{
			SmartDashboard.putString("Error: ", "Auto Shooter Error!");
		}
	}
	
	public void timedShoot()
	{
		
		Arm.autoRun(-.8); //Run Arm backwards for 1 second
		Timer.delay(.3141);
		Arm.autoRun(0); //Turn off Arm
		
		Shooter.set(true); //Turn on Shooter and wait 2 seconds
		Timer.delay(1);
		
		
		Arm.autoRun(1); //Run arm forwards and wait for 4 seconds
		Timer.delay(2);
		
		Shooter.set(false); //Turn off the shooter and arm
		Arm.autoRun(0);
	}

}
