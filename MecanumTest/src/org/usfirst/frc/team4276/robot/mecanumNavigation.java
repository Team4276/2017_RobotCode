package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;

public class mecanumNavigation extends Thread implements Runnable  {

	double robotDeltaX;
	double robotDeltaY;
	
static Encoder frontLeftWheel;
static Encoder backLeftWheel;
static Encoder frontRightWheel;
static Encoder backRightWheel;

static double Kx = 1; //place holder
static double Ky = 1; //place holder

static double frontLeftWheelDelta = 0;
static double backLeftWheelDelta = 0;
static double frontRightWheelDelta = 0;
static double backRightWheelDelta = 0;

double totalFLWheelDistance = 0;
double totalBLWheelDistance = 0;
double totalFRWheelDistance = 0;
double totalBRWheelDistance = 0;

double deltaX_FieldFrame;
double deltaY_FieldFrame;

static double currentFieldX;
static double currentFieldY;
static double oldX_FieldFrame;
static double oldY_FieldFrame;

static double theta;
static double thetaStartingOffset;

boolean ERROR;

public mecanumNavigation(int dio1, int dio2, int dio3, int dio4, int dio5, int dio6, int dio7, int dio8)
{
	frontLeftWheel = new Encoder(dio1, dio2);
	backLeftWheel = new Encoder(dio3, dio4);
	frontRightWheel = new Encoder(dio5, dio6);
	backRightWheel =new Encoder(dio7, dio8);
	frontLeftWheel.setDistancePerPulse(1); //place holder
	backLeftWheel.setDistancePerPulse(1); //place holder
	frontRightWheel.setDistancePerPulse(1); //place holder
	backRightWheel.setDistancePerPulse(1); //place holder
}

double findDeltaX_RobotFrame(double FL,double BL,double FR,double BR)
{
	double leftWheelsX = FL-BL;
	double rightWheelsX = BR-FR;
	double Xnet = .5*Kx*((leftWheelsX)+(rightWheelsX));
	return Xnet;
}

double findDeltaY_RobotFrame(double FL,double BL,double FR,double BR)
{
	double leftWheelsY = FL+BL;
	double rightWheelsY = BR+FR;
	double Ynet = .5*Kx*((leftWheelsY)+(rightWheelsY));
	return Ynet;
}

void findDeltaMovement_RobotFrame()
{
	
	theta = Math.toRadians(-Robot.imu.getYaw() + thetaStartingOffset);
	
	frontLeftWheelDelta = frontLeftWheel.getDistance() - totalFLWheelDistance; // finds delta distance of FrontLeft Wheel
	backLeftWheelDelta = backLeftWheel.getDistance() - totalBLWheelDistance; // finds delta distance of BackLeft Wheel
	frontRightWheelDelta = frontRightWheel.getDistance() - totalFRWheelDistance; // finds delta distance of FrontRight Wheel
	backRightWheelDelta = backRightWheel.getDistance() - totalBRWheelDistance; // finds delta distance of BackRight Wheel
	
	robotDeltaY = findDeltaY_RobotFrame(frontLeftWheelDelta, backLeftWheelDelta, frontRightWheelDelta, backRightWheelDelta); //returns the value of the robot's Delta Y
	robotDeltaX = findDeltaX_RobotFrame(frontLeftWheelDelta, backLeftWheelDelta, frontRightWheelDelta, backRightWheelDelta); //returns the value of the robot's Delta X
	
	totalFLWheelDistance = totalFLWheelDistance + frontLeftWheelDelta; //adds recorded delta of the Front Left wheel to update the total distance value
	totalBLWheelDistance = totalBLWheelDistance + backLeftWheelDelta; //adds recorded delta of the Back Left wheel to update the total distance value
	totalFRWheelDistance = totalFRWheelDistance + frontRightWheelDelta; //adds recorded delta of the Front Right wheel to update the total distance value
	totalBRWheelDistance = totalBRWheelDistance + backRightWheelDelta; //adds recorded delta of the Back Right wheel to update the total distance value
}

static void setStartingPostion(double X, double Y)
{
	/*
	 * Records the starting position and
	 * heading of the robot in order to later 
	 * calculate the robot's position after
	 * moving
	 */
	
	
	oldX_FieldFrame = X;
	oldY_FieldFrame = Y;
	
	if(X > 0) //blue alliance
	{
		thetaStartingOffset = 180; //sets offset of the robot from field coordinate system
	}
	else if(X < 0) //red alliance
	{
		thetaStartingOffset = 0; //sets offset of the robot from field coordinate system
	}
}

void findAbsoluteLocation_FieldFrame() 
{
	/*
	 * Utilizes the Direction Cosine Matrix to find the 
	 * movement of the robot in the Field Frame
	 * 
	 * Complete Matrix:
	 * 
	 * Qfx = ( Cos + Sin + 0)*Qrx
	 * Qfy = (-Sin + Cos + 0)*Qry
	 * Qfz = (  0  +  0  + 1)*Qrz
	 * 
	 *Used (more practical) Matrix:
	 * 
	 * Qfx = ( Cos + Sin)*Qrx
	 * Qfy = (-Sin + Cos)*Qry
	 * 
	 * Qr_ = movement in robot's frame in given axis
	 * Qf_ = movement in field's frame in given axis
	 */
	
	double C11 = Math.cos(theta); //x solved from X
	double C12 = Math.sin(theta); //x solved from Y
	double C21 = -1*Math.sin(theta); //y solved from X
	double C22 = Math.cos(theta); //y solved from Y
	
	deltaX_FieldFrame = robotDeltaX*(C11+C12);
	deltaY_FieldFrame = robotDeltaY*(C21+C22);
	
	currentFieldX = deltaX_FieldFrame + oldX_FieldFrame;
	currentFieldY = deltaY_FieldFrame + oldY_FieldFrame;
	
	SmartDashboard.putNumber("Current X Location:", currentFieldX);
	SmartDashboard.putNumber("Current Y Location:", currentFieldY);
	
	oldX_FieldFrame = currentFieldX;
	oldY_FieldFrame = currentFieldY;
	
}

public void run()
{
	
	try
	{
		while(true)
		{
		ERROR = false;
		
		findDeltaMovement_RobotFrame();
		findAbsoluteLocation_FieldFrame();
		
		}
	}
		catch (Exception e)
		{
		ERROR = true;
		}
	SmartDashboard.putBoolean("Mecanum Location Error", ERROR);
}

}
