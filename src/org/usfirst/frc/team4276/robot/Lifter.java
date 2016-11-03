package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class Lifter {
	
Talon Pivoter9000;
Relay TheArouser;
Encoder Pivot_Measurement;
Joystick KyleIsNub;

public Lifter(int pivettherivet, int extender)
{

	Pivoter9000 = new Talon(pivettherivet);
	TheArouser = new Relay(extender);
	Pivot_Measurement = new Encoder(10,11);
	KyleIsNub = new Joystick(3);
	
}

void ScaleMode()
{
	if (KyleIsNub.getRawButton(XBox.Y))
	{
		TheArouser.set(Value.kForward);
	}
	else if (KyleIsNub.getRawButton(XBox.A))
	{
		TheArouser.set(Value.kReverse);
	}	
	else
	{
		TheArouser.set(Value.kOff);
	}
}
void tapePivoter()
{
	double deadband = .2;
	double value = KyleIsNub.getRawAxis(XBox.RStickY);
	
}

}
