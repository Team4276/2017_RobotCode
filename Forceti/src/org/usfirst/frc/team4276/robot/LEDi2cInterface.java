package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LEDi2cInterface extends Thread implements Runnable  {

	
	static boolean enabled = false;
	static boolean climbing = false;
	static boolean gearCollected = false;
	static boolean shooting = false;
	static boolean victourios = false;
	Relay wire1;
	Relay wire2;
	Relay wire3;
	Relay wire4;
	Relay.Value wireEnabledVal = Relay.Value.kOff;
	Relay.Value wireClimbingVal = Relay.Value.kOff;
	Relay.Value wireShootingVal = Relay.Value.kOff;
	Relay.Value wireGearVal = Relay.Value.kOff;	
	
	public LEDi2cInterface()
	{
		wire1 = new Relay(1);
		wire2 = new Relay(2);
		wire3 = new Relay(3);
		wire4 = new Relay(4);
	}
	
	void updateLEDValues()
	{
		
		if(enabled){
			wireEnabledVal = Relay.Value.kOn;
		} else{
			wireEnabledVal = Relay.Value.kOff;			
		}
		
		if(climbing){
			wireClimbingVal = Relay.Value.kOn;
		} else{
			wireClimbingVal = Relay.Value.kOff;			
		}
		
		if(shooting){
			wireShootingVal = Relay.Value.kOn;
		} else{
			wireShootingVal = Relay.Value.kOff;			
		}
		
		if(gearCollected){
			wireGearVal = Relay.Value.kOn;
		} else{
			wireGearVal = Relay.Value.kOff;			
		}
		
		wire1.set(wireEnabledVal);
		wire2.set(wireClimbingVal);
		wire3.set(wireShootingVal);
		wire4.set(wireGearVal);
		
		SmartDashboard.putBoolean("enabled", enabled);
		SmartDashboard.putBoolean("climbing", climbing);
		SmartDashboard.putBoolean("shooting", shooting);
		SmartDashboard.putBoolean("geared", gearCollected);
		
	}
	
	public void run(){
		try{
			while(true){
			SmartDashboard.putBoolean("LED problem:", false);
			updateLEDValues();
			}
		}
		catch(Exception x)
		{
			SmartDashboard.putBoolean("LED problem:", true);
		}
	}
	
}
