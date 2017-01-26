package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class mecanumDrive {

RobotDrive mecanumControl;
	Joystick mecanumJoystick;
	VictorSP forwardRightMotor;
	VictorSP forwardLeftMotor;
	VictorSP backRightMotor;
	VictorSP backLeftMotor;
	boolean robotFrame;
	boolean fieldFrame;
	boolean Xtest;
	boolean Ytest;
	boolean Twisttest;
	
	int mode = 0;
	
public mecanumDrive( int pwm0, int pwm1, int pwm8, int pwm9)
{

	mecanumJoystick = new Joystick(0);
	forwardRightMotor = new VictorSP(pwm0);
	forwardLeftMotor = new VictorSP(pwm1);
	backRightMotor = new VictorSP(pwm8);
	backLeftMotor = new VictorSP(pwm9);
	
	mecanumControl = new RobotDrive(forwardLeftMotor, backLeftMotor, forwardRightMotor, backRightMotor);
}

void robotFrameDrive()
{

	fieldFrame = false;
	robotFrame = true;
	Xtest = false;
	Ytest = false;
	Twisttest = false;
	
	double X = mecanumJoystick.getX();
	double Y = mecanumJoystick.getY();
	double Twist = mecanumJoystick.getTwist();
	double magnitude;
	double direction;
	double rotation;
	
	
	if (Math.abs(X)<.02||Math.abs(Y)<.02)
		magnitude = Math.sqrt((X*X)+(Y*Y));
	else
	magnitude = 0;
	
	
	if(Math.abs(X)<.02||Math.abs(Y)<.02)
		direction = (180/Math.PI)*Math.atan2(Y, X);
	else
		direction = 0;
	
	
	if(Math.abs(Twist)>.02)
		rotation = Twist;
	else
		rotation = 0;
	
	
	mecanumControl.mecanumDrive_Polar(magnitude, direction, rotation);
}

void fieldFrameDrive()
{
	fieldFrame = true;
	robotFrame = false;
	Xtest = false;
	Ytest = false;
	Twisttest = false;	
	
	double yaw = 0.0;
	double X = 0.0;
	double Y = 0.0;
	double Twist = 0.0;
	
	yaw = Robot.imu.getYaw();
	
	if(Math.abs(mecanumJoystick.getX())>.02)
		X = mecanumJoystick.getX();
	else 
		X= 0;
		
	if(Math.abs(mecanumJoystick.getY())>.02)
		Y = mecanumJoystick.getY();
	else 
		Y= 0;
	if(Math.abs(mecanumJoystick.getTwist())>.02)
		Twist = mecanumJoystick.getTwist();
	else 
		Twist= 0;
	mecanumControl.mecanumDrive_Cartesian(X, Y, Twist, yaw);
}

void XTest()
{
	fieldFrame = false;
	robotFrame = false;
	Xtest = true;
	Ytest = false;
	Twisttest = false;
	
	double X;
	
	if(Math.abs(mecanumJoystick.getX())>.02)
		X = mecanumJoystick.getX();
	else 
		X= 0;
	
	mecanumControl.mecanumDrive_Cartesian(X, 0, 0, 0);
}

void YTest()
{
	fieldFrame = false;
	robotFrame = false;
	Xtest = false;
	Ytest = true;
	Twisttest = false;
	
	
	double Y;
	
	if(Math.abs(mecanumJoystick.getY())>.02)
		Y = mecanumJoystick.getY();
	else 
		Y= 0;
	
	mecanumControl.mecanumDrive_Cartesian(0, Y, 0, 0);
}

void TwistTest()
{
	fieldFrame = false;
	robotFrame = false;
	Xtest = false;
	Ytest = false;
	Twisttest = true;
	
	
	double Twist;
	
	if(Math.abs(mecanumJoystick.getX())>.02)
		Twist = mecanumJoystick.getX();
	else 
		Twist= 0;
	
	mecanumControl.mecanumDrive_Cartesian(0, 0, Twist, 0);
}

void drive()
{
	if(mode == 0 && mecanumJoystick.getRawButton(5))
		mode = 1;
	if(mode == 1 && mecanumJoystick.getRawButton(5))
		mode = 0;
	
	if(mode ==0)
	{

		fieldFrameDrive();
		
	}	
	if(mode ==1)
	{

		robotFrameDrive();
		
	}

}

void driveTest()
{
	if(mode < 3 && mecanumJoystick.getRawButton(5))
	{
		mode ++;
	}
	else if(mode >= 3 && mecanumJoystick.getRawButton(5))
	{
		mode = 1;
	}
	 
	if(mode == 1)
	{
		YTest();
	}
	if(mode == 2)
	{
		XTest();
	}
	if(mode == 3)
	{
		TwistTest();
	}
}

void modeReadout()
{
	SmartDashboard.putBoolean("Robot Frame", robotFrame);
	SmartDashboard.putBoolean("Field Frame", fieldFrame);
	SmartDashboard.putBoolean("Twist Test", Twisttest);
	SmartDashboard.putBoolean("Y Test", Ytest);
	SmartDashboard.putBoolean("X Test", Xtest);
}

}
