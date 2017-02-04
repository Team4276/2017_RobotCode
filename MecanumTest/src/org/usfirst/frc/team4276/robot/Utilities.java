package org.usfirst.frc.team4276.robot;

public class Utilities {

	static boolean toggle(boolean input)
	{
		boolean returnValue;
		if(input == true)
		{
			returnValue = false;
		}
		else
		{
			returnValue = true;
		}
		return returnValue;
	}
}
