package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;

public class SHOOTER_THREAD extends Thread implements Runnable
{

	Encoder SHOOTER_SPEED; //will give us the speed of the shooter
	Boolean ERROR = false;
	Talon ASSIGNED_SPEED;
	Joystick Xbox;
	AutoShoot autoshooter;
	boolean started=false;
	
	public SHOOTER_THREAD()
{
		
	SHOOTER_SPEED = new Encoder(6,7); //temporarilly asigned to ports 6 &7
	ASSIGNED_SPEED = new Talon(9); //temporarilly asigned to port 9
	Xbox = new Joystick(3);
	autoshooter = new AutoShoot();
	
}
	public void DESIRED_SPEED(double speed)
	{
		
		 double ACTUAL_SPEED = SHOOTER_SPEED.getRate(); //speed the shooter is running at *MATH REQUIRED!!!!!*
		 double SET_SPEED = speed; //desired speed for the arm
		 double DIFFERENCE = SET_SPEED - ACTUAL_SPEED; //how far are we from the desired speed?
		 double K = .02; //how much we want to multiply the change in motor speed by
		 double CHANGED_SPEED = DIFFERENCE * K; //How much we change the motor speed by
		 
		ASSIGNED_SPEED.set(CHANGED_SPEED + ACTUAL_SPEED); //runs motor at old speed plus changed speed
		 
		
	}
	public void run()
	{
		
		try
		{
			
			
			ERROR = false;
			
			while(true)
			{
			 
				if(!autoshooter.isAlive())
					started=false;
			if(Xbox.getRawButton(XBox.A))
			{
				if(autoshooter.isAlive())
					autoshooter.interrupt();
				DESIRED_SPEED(1);
				started=false;
				
			}
			else if(Xbox.getRawButton(XBox.X))
			{
				if(autoshooter.isAlive())
					autoshooter.interrupt();
				DESIRED_SPEED(.8);
				started=false;
			}
			else if(Xbox.getRawButton(XBox.Y))
			{
				if(autoshooter.isAlive())
					autoshooter.interrupt();
				DESIRED_SPEED(.5);
				started=false;
			}
			else if(Xbox.getRawButton(XBox.B))
			{
				if(autoshooter.isAlive())
					autoshooter.interrupt();
				DESIRED_SPEED(0);
				started=false;
				
			}
			else if(Xbox.getRawAxis(XBox.LTrigger)>0.5&&!started)
			{
				SmartDashboard.putBoolean("Exception Thrown: ", false);
				started=true;
				autoshooter=new AutoShoot();
				autoshooter.start();
				
			}
				
			}
			
		}
		
		catch (Exception e)
		{
			
			ERROR = true;
			
		}
		
		SmartDashboard.putBoolean(" Shooter Speed Error", ERROR);
		
	}
	
}
