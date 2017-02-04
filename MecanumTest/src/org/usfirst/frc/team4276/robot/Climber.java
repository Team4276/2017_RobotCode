package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

public class Climber {
DigitalInput climberLimitSwitch;
VictorSP climber;
Timer time;

double limitSwitchDelay = 1;
double time1 = 0;
boolean previousStateLimit , previousStateButton, climbing;
	
public Climber(int pwm8, int dio13)
{
climber = new VictorSP(pwm8);
climberLimitSwitch = new DigitalInput(dio13);
}

void limitSwitchStop()
{
	if(climberLimitSwitch.get() == true)
	{
		if(previousStateLimit == false)
		{
			time1 = time.get();
			previousStateLimit = true;
			joystickToggle();
		}
		else
		{
			if(time.get() - time1 > limitSwitchDelay)
			{
				climber.set(0);
			}
			else
			{
				joystickToggle();
			}
		}
	}
	else
	{
		joystickToggle();
	}
	
}

void joystickToggle(){
	
	if(BallShooter.XBox1.getRawButton(XBox.Back)) {
		if(previousStateButton == false)
		{
			climbing = Utilities.toggle(climbing);
			previousStateButton = true;
		}
		else
		{
			
			previousStateButton = true;
		}
	
	}
	else
	{
		previousStateButton = false;
	}
	
	
}
	/*void toggle ()
	{
		if(climbing == true){
			climbing = false;
		}
		else{
			climbing = true;
			}	
	}
	*/
	void run ()
	{
		joystickToggle();
		if(climbing == true){
			climber.set(1);
		}
		
		else{
			climber.set(0);
		}
		
		SmartDashboard.putBoolean("Climber", climbing);
		
		}
	}
	





