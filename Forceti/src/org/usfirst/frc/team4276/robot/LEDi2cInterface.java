package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Relay;

public class LEDi2cInterface {

	
	static boolean enabled = false;
	static boolean climbing = false;
	static boolean gearCollected = false;
	static boolean shooting = false;
	static boolean victourios = false;
	Relay wire1;
	Relay wire2;
	Relay wire3;
	Relay wire4;
	Relay.Value wire1Val = Relay.Value.kOff;
	Relay.Value wire2Val = Relay.Value.kOff;
	Relay.Value wire3Val = Relay.Value.kOff;
	Relay.Value wire4Val = Relay.Value.kOff;	
	
	public LEDi2cInterface()
	{
		wire1 = new Relay(1);
		wire1 = new Relay(1);
		wire1 = new Relay(1);
		wire1 = new Relay(1);
	}
	
	void testI2C()
	{
		
		if(enabled){
			if(climbing){
				if(gearCollected){
					if(shooting){
						if(victourios){
							
						}
						else{
							
						}
					}
					else{
						
					}
				}
				else{
					
				}
			}
			else{
				
			}
		}
		else{
			
		}
		
		wire1.set(wire1Val);
		wire2.set(wire2Val);
		wire3.set(wire3Val);
		wire4.set(wire4Val);
		
	}
	
}
