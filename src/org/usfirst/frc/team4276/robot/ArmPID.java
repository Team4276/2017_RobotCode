package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

public class ArmPID extends Thread implements Runnable{
	
	static DigitalInput SET_90_OPTICAL;
	static double angle;
	static double startang=110;	
	static double setpoint = startang;
	static double ang;
	
	public ArmPID(int hallswitch)
	{
		SET_90_OPTICAL = new DigitalInput(hallswitch);	
		angle=startang;
	}
	static void armangle(double armangle)
	{
		 ang = armangle;
	}
	public void run()
	{
		double offset;		
		
		double k=.02,deadband=2;
		double power;
		
		try
		{
			while(true)
			{
				if (Arm.joystick.getRawButton(XBox.Start))
				{
					Arm.Pivoter.set(Arm.joystick.getRawAxis(XBox.LStickY));
				}
				else {
				angle=startang-(-1*Arm.enc.getDistance());
				
				offset = setpoint - angle;
				
				if(Math.abs(offset)>deadband)
				{
					power = k*offset;
				}
				else power=0;
				
				power+=Math.cos(angle)*.05; 
				Arm.Pivoter.set(-power);
				if(Arm.joystick.getRawAxis(XBox.LStickY)>0.5)
					setpoint-=3;
				else if(Arm.joystick.getRawAxis(XBox.LStickY)<-0.5)
					setpoint+=3;
				if (!(ang == 0))
				{
					setpoint = ang;
				}
				
				if(setpoint>=120)
					setpoint=120;
				if(setpoint<=-20)
					setpoint=-120;
				if(Arm.joystick.getRawButton(XBox.Y))
					setpoint=80;
				if(Arm.joystick.getRawButton(XBox.B))
					setpoint=5;
				if(Arm.joystick.getRawButton(XBox.X))
					setpoint=-10;
				
				SmartDashboard.putNumber("Arm Offset: ", offset);
				SmartDashboard.putNumber("Setpoint: ", setpoint);
				SmartDashboard.putNumber("Power: ", power);
				SmartDashboard.putNumber("Arm Angle: ", angle);
				SmartDashboard.putNumber("Encoder Value: ", Arm.enc.getDistance());
				SmartDashboard.putBoolean("Switch", !SET_90_OPTICAL.get());
				
				/*if(!SET_90_OPTICAL.get())
				{
					Arm.enc.reset();
					angle=startang-Arm.enc.getDistance();
					setpoint = angle;
				}*/
			
				Timer.delay(0.05);
				
				
				}
			}

			}
		
		catch (Exception e)
		{
			
		}
	}

}