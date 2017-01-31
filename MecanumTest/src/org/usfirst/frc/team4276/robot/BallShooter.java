package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Joystick;



public class BallShooter {

	static Joystick XBox1;
	static VictorSP shooterWheel;
	static VictorSP feedingWheel;
	static Counter shooterEncoder;
	static double currentRate;
	static double difference;
	static double shooterConstant = 0.9;
	//this value is a placeholder; the value of this double will be adjusted later
	static double regularPower = 0.85;
	//and so will the value of this double
	static double assignedPower;
	
	public BallShooter(int pwm4,int pwm5, int dio10){
		XBox1 = new Joystick(3);
		shooterWheel = new VictorSP(pwm4);
		feedingWheel = new VictorSP(pwm5);
		shooterEncoder = new Counter(dio10); //place holder for geartooth encoder
		shooterEncoder.setDistancePerPulse(.0625);
	}
	
static void flyWheel(double desiredRate){
	currentRate = shooterEncoder.getRate();
	difference = currentRate - desiredRate;
	assignedPower = regularPower + shooterConstant*difference;
	shooterWheel.set(assignedPower);
		
}

void shooter()
{
	if(XBox1.getRawButton(XBox.RTrigger))
	{
		flyWheel(3000);
		feedingWheel.set(.5); //.5 = place holder
	}
	else
	{
		flyWheel(0);
		feedingWheel.set(0);
	}
}
	
//static void function for the feeding
/*static void feeding(){
 * 
 * }
 */

}
