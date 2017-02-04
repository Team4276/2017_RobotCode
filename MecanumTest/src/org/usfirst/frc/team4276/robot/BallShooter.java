package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;



public class BallShooter {

	static Joystick XBox1;
	Joystick testJoy;
	static VictorSP shooterWheel;
	static VictorSP feedingWheel;
	static Counter shooterEncoder;
	static double currentRate;
	static double proportionalError;
	static double proportionalConstant = 0.0;
	static double integralConstant = 0.0;
	static double derivativeConstant = 0.0;
	static double integralError = 0;
	static double derivativeError;
	/*all of these values are just place holders at the moment
	 * will be determined experimentally
	 */
	static double assignedPower;
	static double desiredRate = 3000;
	static double time1;
	static double time2;
	static double timeStep;
	static double proportionalErrorLast;
	static Timer shooterTimer;
	
	public BallShooter(int pwm4,int pwm5, int dio10){
		XBox1 = new Joystick(3);
		testJoy = new Joystick(1);
		shooterWheel = new VictorSP(pwm4);
		feedingWheel = new VictorSP(pwm5);
		shooterEncoder = new Counter(dio10); //place holder for geartooth encoder
		shooterEncoder.setDistancePerPulse(1/12); //rpm
	}
void assignConstants()
{
	if(testJoy.getRawButton(7))
	proportionalConstant = proportionalConstant - .01;
	else if(testJoy.getRawButton(8))
		proportionalConstant = proportionalConstant + .01;
	if(testJoy.getRawButton(9))
	integralConstant = integralConstant - .01;
	else if(testJoy.getRawButton(10))
		integralConstant = integralConstant + .01;
	if(testJoy.getRawButton(11))
		derivativeConstant = derivativeConstant - .01;
		else if(testJoy.getRawButton(12))
			derivativeConstant = derivativeConstant + .01;
}
static void flyWheel(double currentRate){
	time1 = shooterTimer.get();
	currentRate = shooterEncoder.getRate();
	time2 = shooterTimer.get();
	proportionalError = desiredRate - currentRate;
	timeStep = time2 - time1;
	integralError = integralError + proportionalError*timeStep;
	derivativeError = (proportionalError - proportionalErrorLast)/timeStep;
	assignedPower = proportionalConstant*proportionalError + integralConstant*integralError + derivativeConstant*derivativeError;
	shooterWheel.set(assignedPower);
	proportionalErrorLast = proportionalError;

}

void shooter()
{
	assignConstants();
	if(XBox1.getRawButton(XBox.RTrigger))
	{
		flyWheel(assignedPower);
		feedingWheel.set(.5); //.5 = place holder
	}
	else
	{
		flyWheel(0);
		feedingWheel.set(0);
	}
}
	

}
