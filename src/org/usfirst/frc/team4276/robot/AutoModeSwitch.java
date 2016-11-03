package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Joystick;

public class AutoModeSwitch {
	
	static Joystick switchbox;
	static int autoversion;
	
	public AutoModeSwitch()
	{
		switchbox = new Joystick(4);
		autoversion=0;
	}
	
	public static int getswitchvalue()
	{
		autoversion=0;
		if(switchbox.getRawButton(1))
    		autoversion+=1;
    	if(switchbox.getRawButton(2))
    		autoversion+=2;
    	if(switchbox.getRawButton(3))
    		autoversion+=4;
    	if(switchbox.getRawButton(4))
    		autoversion+=8;
    	
    	return autoversion;
	}

}
