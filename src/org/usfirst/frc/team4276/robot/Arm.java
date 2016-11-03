package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Encoder; 
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

public class Arm {
	  static VictorSP Pivoter;
	  static Joystick joystick; 
	  static Encoder enc; 
	 static VictorSP relayer;
	 static DigitalInput limiter;
	  boolean spinfor = false;
	  boolean spinaft = false;
	  double speed = 0;
	  ArmPID runArm;
	  Timer balltime = new Timer();
	  static boolean autoRun_In_Progress=false;
	  
	  
	
	public Arm (int power, int rel, int lim,int enc1,int enc2,int hallswitch) 
	{
		Pivoter = new VictorSP ( power);
		enc = new Encoder(enc1,enc2);
		enc.setDistancePerPulse(.362);
		enc.reset();
		joystick = new Joystick (3);
		relayer = new VictorSP(rel);
		limiter = new DigitalInput(lim);
		runArm = new ArmPID(hallswitch);
		runArm.start();
		
		
	}
	
	
	public void collector()
	
	{
		SmartDashboard.putBoolean("LIMIT SWITCH", limiter.get());
		//double dif = Math.abs(speed) - Math.abs(encode.getRate());
		//double k=.03;
		//double deadband = .1;
		
		if(joystick.getRawButton(XBox.LB)&& limiter.get())
		{
			spinfor = true;
			spinaft = false;
			joystick.setRumble(RumbleType.kLeftRumble, 0);
			joystick.setRumble(RumbleType.kLeftRumble, 0);
		}
		else if(joystick.getRawButton(XBox.LB) && !limiter.get())
		{
			spinfor=false;
			spinaft=false;
			joystick.setRumble(RumbleType.kLeftRumble, 1);
			joystick.setRumble(RumbleType.kLeftRumble, 1);
		}
		else if(joystick.getRawButton(XBox.RB))
		{
			spinfor=false;
			spinaft=true;	
			joystick.setRumble(RumbleType.kLeftRumble, 0);
			joystick.setRumble(RumbleType.kLeftRumble, 0);
		}
		else 
		{
			spinfor=false;
			spinaft=false;
			joystick.setRumble(RumbleType.kLeftRumble, 0);
			joystick.setRumble(RumbleType.kLeftRumble, 0);
		}
		
		 
		if(spinfor)
		{
			
			speed=.8;
		}
		else if(spinaft)
		{
		
			speed = -.8;

		}
		else
		{
			speed = 0;
		}
		
		
		/*	if(dif > deadband && speed > 0)
		{
			speed+=dif*k;
		}
			else if(dif > deadband && speed < 0)
			{
				speed-=dif*k;
			}*/
		SmartDashboard.putBoolean("AutoRun In PRog", autoRun_In_Progress);
		if(!autoRun_In_Progress)
			relayer.set(speed);
		
	}


static void autoRun(double speed)
{
	relayer.set(speed);
}
	
	
	
	

	
	
	
}