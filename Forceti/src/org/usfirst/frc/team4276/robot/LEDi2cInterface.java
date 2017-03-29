package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.I2C;

public class LEDi2cInterface {

	I2C wire;
	
	public LEDi2cInterface()
	{
		wire = new I2C(I2C.Port.kOnboard ,42);
	}
	
	void testI2C()
	{
		
		wire.write(42, 5);
		
	}
	
}
