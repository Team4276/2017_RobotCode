package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.VictorSP;

public class Shooter {
	
	static VictorSP shootermotor1,shootermotor2;
	Joystick joy;
	static AutoShoot autoshooter;
	static Encoder shooterenc;
	boolean started=false;
	
	public Shooter(int shooterl,int shooterr,int shooterenc1,int shooterenc2)
	{
		shootermotor1 = new VictorSP(shooterl);
		shootermotor2 = new VictorSP(shooterr);
		shooterenc = new Encoder(shooterenc1,shooterenc2);
		joy=new Joystick(3);
		autoshooter = new AutoShoot();
		shooterenc.setDistancePerPulse(-.001);
	}
	
	void run()
	{
		SmartDashboard.putBoolean("Auto Shooter Started: ", autoshooter.isAlive());
		SmartDashboard.putNumber("Shooter speed: ", shooterenc.getRate());
		
		
		try{
			
		if(!autoshooter.isAlive())
				started=false;
		if(joy.getRawAxis(XBox.RTrigger)>0.5)
		{
			if(autoshooter.isAlive())
				autoshooter.interrupt();
			set(true);
			started=false;
			
		}
		else if(joy.getRawAxis(XBox.LTrigger)>0.5&&!started)
		{
			SmartDashboard.putBoolean("Exception Thrown: ", false);
			started=true;
			autoshooter=new AutoShoot();
			autoshooter.start();
			
		}
		else if(joy.getRawAxis(XBox.RTrigger)<0.5&&!autoshooter.isAlive())
		{
			
			set(false);
			started=false;
			
		}
		}
		
		catch(Exception e){
			SmartDashboard.putBoolean("Exception Thrown: ", true);
		}
	}

	static void set(boolean shooteron)
	{
		double k=0.01;
		double nominalrate = 120; //rpm?
		double currRate = shooterenc.getRate();
		double diff = nominalrate - currRate;
		
		double speed = 0.8+diff*k;
		if(speed>1)
			speed=1;
		SmartDashboard.putNumber("Shooter speed: ", shooterenc.getRate());
		SmartDashboard.putNumber("SETSPEED: ", speed);

		if(shooteron)
		{
			shootermotor1.set(speed);
			shootermotor2.set(-speed);
			}
		else{
			shootermotor1.set(0);
			shootermotor2.set(0);
		}
	}

}
