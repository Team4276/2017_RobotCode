
package org.usfirst.frc.team4276.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {

	mecanumDrive driveSystem;
	Joystick driveJoy;
    public Robot() {
       // driveJoy = new Joystick(1);
        driveSystem = new mecanumDrive(0,1,8,9);
    }

    /**
     * Drive left & right motors for 2 seconds then stop
     */
    public void autonomous() {
       
    }

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {
       
        while (isOperatorControl() && isEnabled()) {
           // SmartDashboard.putNumber("Y", driveJoy.getY());
            //SmartDashboard.putNumber("X", driveJoy.getX());
           //SmartDashboard.putNumber("Twist", driveJoy.getTwist());
        	driveSystem.drive();
        	driveSystem.modeReadout();
            Timer.delay(.05);
        }
    }

    /**
     * Runs during test mode
     */
    public void test() {
    	driveSystem.YTest();
    	driveSystem.XTest();
    	driveSystem.TwistTest();
    	
    }
}
