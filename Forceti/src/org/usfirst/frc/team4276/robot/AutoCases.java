package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/*
 * 	
 * Field Coordinate System
 *                             ^
 *    _________________________|__________________________
 *    |                        |                         |
 *    |                        |+Y                       |
 *    | 3                      |                      3  |
 *    |                        |                         |
 *    |     -X                 |              +X         |
 *  <-|-2----------------------|-----------------------2-|->
 *    |                        |                         |
 *    |                        |                         |
 *    | 1   Red Alliance       |      Blue Alliance    1 | 
 *    |                        |-Y                       |
 *    |________________________|_________________________|
 *    Boiler                   |                    Boiler
 *                             V
 *                             
 *   @author Avery                         
*/

public class AutoCases {

	BallShooter autoShooter;

	public AutoCases(BallShooter a) {
		autoShooter = a;
		// Shooter = new BallShooter(9, 4, 8, 9);// pwm ports 4 & 9, dio ports 8
		// & 9

	}

	void autoModes() {
		// int automode = autoModeSelector.autonomousModeNumber;

		final double RED_STARTING_X = -25.8;// feet
		final double BLUE_STARTING_X = 25.8;// feet
		// these starting X coordinates are true for all mode numbers
		// each side only has one starting X coordinate

		final double MODE_1_STARTING_Y = -6.7;// feet
		final double MODE_2_STARTING_Y = 0;// feet
		final double MODE_3_STARTING_Y = 6.7;// feet
		// these starting Y coordinates are true for both alliance colors
		// each mode only has one starting Y coordinate
		/*
		 * order of modes: 1, 2, 3, with 1 being the positions on both sides
		 * that are closest to the boilers while 3 positions are on sides that
		 * are closest to the retrieval zones 2 positions are the positions on
		 * both sides that are in the middle between the boilers and the
		 * retrieval zones
		 */

		final double FRONT_LIFT_DISTANCE = 6.00;
		final double SIDE_LIFT_PREP_DISTANCE = 8.0;

		final double ARM_DEPOSIT_ANGLE = -20.0;

		final double BLUE_HOPPER_X = 18.9;
		final double BLUE_HOPPER_Y = -12.0;
		final double RED_HOPPER_X = -18.9;
		final double RED_HOPPER_Y = -12.0;

		final double RED_SIDE_GEARLIFT_X = -15.0;
		final double RED_MIDDLE_GEARLIFT_X = -18.2;

		final double BLUE_SIDE_GEARLIFT_X = 15.0;
		final double BLUE_MIDDLE_GEARLIFT_X = 18.2;

		final double GEARLIFT_1_Y = -3.3;
		final double GEARLIFT_2_Y = 0.0;
		final double GEARLIFT_3_Y = 3.3;

		final double TIME_TO_COLLECT_HOPPER = 3.0;

		final int nothing = 0;
		final int redAuto2_ScoreGear = 1;
		final int redAuto2_GearandZone = 2;
		final int redAuto3_GearandZone = 3;
		final int redAuto1_GearandZone = 4;
		final int redAuto1_ShootFromHopper = 5;
		final int redAuto2_ShootFromHopper = 6;
		final int redAuto1_GearandShootFromHopper = 7;
		final int redAuto1_HopperandShootFromBoiler = 8;

		final int blueAuto2_ScoreGear = 21;
		final int blueAuto2_GearandZone = 22;
		final int blueAuto3_GearandZone = 23;
		final int blueAuto1_GearandZone = 24;
		final int blueAuto1_ShootFromHopper = 25;
		final int blueAuto2_ShootFromHopper = 26;
		final int blueAuto1_GearandShootFromHopper = 27;
		final int blueAuto1_HopperandShootFromBoiler = 28;

		final int justShootBlue = 35;
		final int justShootRed = 15;

		final int testGearDeposit = 40;
		final int testAutoRotate = 41;
		final int testFwd = 42;
		final int testCoordinateDriveSide = 43;

		int autoMode = redAuto3_GearandZone;

		switch (autoMode) {
		case nothing:
			break;
		case redAuto2_ScoreGear:

			mecanumNavigation.setStartingPosition(RED_STARTING_X, MODE_2_STARTING_Y, 0);
			gearCollection.setArmPosition(ARM_DEPOSIT_ANGLE);
			Robot.systemTimer.reset();

			while (!mecanumDrive.driveStraight(FRONT_LIFT_DISTANCE) && (Robot.systemTimer.get() < 3))
				;

			mecanumDrive.driveInit = true;

			gearCollection.autoGearDeposit(.5);

			Robot.systemTimer.reset();
			while (!mecanumDrive.driveStraight(-2.0) && (Robot.systemTimer.get() < 3))
				;

			// mecanumDrive.driveInit = true;

			Robot.systemTimer.reset();

			break;

		case redAuto3_GearandZone:
			mecanumNavigation.setStartingPosition(RED_STARTING_X, MODE_3_STARTING_Y, 0);
			Robot.systemTimer.reset();

			while (!mecanumDrive.driveStraight(SIDE_LIFT_PREP_DISTANCE) && (Robot.systemTimer.get() < 3))
				;// drive forward

			mecanumDrive.driveInit = true;

			gearCollection.setArmPosition(ARM_DEPOSIT_ANGLE);// lower arm

			Robot.systemTimer.reset();
			while (!mecanumDrive.rotateToHeading(-60) && (Robot.systemTimer.get() < 1.5))
				;// rotate to face lift

			Robot.systemTimer.reset();
			while (!mecanumDrive.driveStraight(2.5) && (Robot.systemTimer.get() < 2))
				;// drive to lift

			mecanumDrive.driveInit = true;

			gearCollection.autoGearDeposit(.5);// deposit gear

			while (!mecanumDrive.driveStraight(-2.0) && (Robot.systemTimer.get() < 2))
				;// drive from lift

			mecanumDrive.driveInit = true;

			Robot.systemTimer.reset();
			break;

		case redAuto1_GearandZone:

			mecanumNavigation.setStartingPosition(RED_STARTING_X, MODE_1_STARTING_Y, 0);
			Robot.systemTimer.reset();

			while (!mecanumDrive.driveStraight(SIDE_LIFT_PREP_DISTANCE) && (Robot.systemTimer.get() < 3))
				;// drive forward

			mecanumDrive.driveInit = true;

			gearCollection.setArmPosition(ARM_DEPOSIT_ANGLE);// lower arm

			Robot.systemTimer.reset();
			while (!mecanumDrive.rotateToHeading(60) && (Robot.systemTimer.get() < 1.5))
				;// rotate to face lift

			Robot.systemTimer.reset();
			while (!mecanumDrive.driveStraight(2.5) && (Robot.systemTimer.get() < 2))
				;// drive to lift

			mecanumDrive.driveInit = true;

			gearCollection.autoGearDeposit(.5);// deposit gear

			while (!mecanumDrive.driveStraight(-2.0) && (Robot.systemTimer.get() < 2))
				;// drive from lift

			mecanumDrive.driveInit = true;

			Robot.systemTimer.reset();
			break;

		case justShootRed:

			mecanumNavigation.setStartingPosition(RED_STARTING_X, 0, -135);

			while (Robot.systemTimer.get() < 10) {
				autoShooter.autoShoot();
			}

			break;
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// blue
		case blueAuto2_ScoreGear:
			mecanumNavigation.setStartingPosition(RED_STARTING_X, MODE_2_STARTING_Y, 180);
			gearCollection.setArmPosition(ARM_DEPOSIT_ANGLE);
			Robot.systemTimer.reset();

			while (!mecanumDrive.driveStraight(FRONT_LIFT_DISTANCE) && (Robot.systemTimer.get() < 3))
				;

			mecanumDrive.driveInit = true;

			gearCollection.autoGearDeposit(.5);

			Robot.systemTimer.reset();
			while (!mecanumDrive.driveStraight(-2.0) && (Robot.systemTimer.get() < 3))
				;

			// mecanumDrive.driveInit = true;

			Robot.systemTimer.reset();

			break;

		case blueAuto3_GearandZone:
			mecanumNavigation.setStartingPosition(BLUE_STARTING_X, MODE_3_STARTING_Y, 180);
			Robot.systemTimer.reset();

			while (!mecanumDrive.driveStraight(SIDE_LIFT_PREP_DISTANCE) && (Robot.systemTimer.get() < 3))
				;// drive forward

			mecanumDrive.driveInit = true;

			gearCollection.setArmPosition(ARM_DEPOSIT_ANGLE);// lower arm

			Robot.systemTimer.reset();
			while (!mecanumDrive.rotateToHeading(-120) && (Robot.systemTimer.get() < 1.5))
				;// rotate to face lift

			Robot.systemTimer.reset();
			while (!mecanumDrive.driveStraight(2.5) && (Robot.systemTimer.get() < 2))
				;// drive to lift

			mecanumDrive.driveInit = true;

			gearCollection.autoGearDeposit(.5);// deposit gear

			while (!mecanumDrive.driveStraight(-2.0) && (Robot.systemTimer.get() < 2))
				;// drive from lift

			mecanumDrive.driveInit = true;

			Robot.systemTimer.reset();
			break;

		case blueAuto1_GearandZone:
			mecanumNavigation.setStartingPosition(BLUE_STARTING_X, MODE_1_STARTING_Y, 180);
			Robot.systemTimer.reset();

			while (!mecanumDrive.driveStraight(SIDE_LIFT_PREP_DISTANCE) && (Robot.systemTimer.get() < 3))
				;// drive forward

			mecanumDrive.driveInit = true;

			gearCollection.setArmPosition(ARM_DEPOSIT_ANGLE);// lower arm

			Robot.systemTimer.reset();
			while (!mecanumDrive.rotateToHeading(120) && (Robot.systemTimer.get() < 1.5))
				;// rotate to face lift

			Robot.systemTimer.reset();
			while (!mecanumDrive.driveStraight(2.5) && (Robot.systemTimer.get() < 2))
				;// drive to lift

			mecanumDrive.driveInit = true;

			gearCollection.autoGearDeposit(.5);// deposit gear

			while (!mecanumDrive.driveStraight(-2.0) && (Robot.systemTimer.get() < 2))
				;// drive from lift

			mecanumDrive.driveInit = true;

			Robot.systemTimer.reset();
			break;
			
		case justShootBlue:

			mecanumNavigation.setStartingPosition(RED_STARTING_X, 0, -135);

			while (Robot.systemTimer.get() < 10) {
				autoShooter.autoShoot();
			}

			break;
		/*
		 * case redAuto2_GearandZone:
		 * mecanumNavigation.setStartingPosition(RED_STARTING_X,
		 * MODE_2_STARTING_Y, 0); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveStraight(3) && (Robot.systemTimer.get() > 3)) ;
		 * 
		 * Robot.systemTimer.reset(); // Robot.systemTimer.reset(); //
		 * while(!mecanumDrive.gearAlign(Robot.gearPegOffset) && //
		 * (Robot.systemTimer.get()>3));
		 * 
		 * gearCollection.autoGearDeposit(.5); gearCollection.setArmPosition(0);
		 * Robot.systemTimer.reset(); while (!mecanumDrive.driveStraight(-2) &&
		 * (Robot.systemTimer.get() > 3)) ;
		 * 
		 * while (!mecanumDrive.rotateToHeading(-60)) ;
		 * 
		 * Robot.systemTimer.reset(); while (!mecanumDrive.driveStraight(5) &&
		 * (Robot.systemTimer.get() > 3)) ; Robot.systemTimer.reset(); while
		 * (!mecanumDrive.rotateToHeading(0)) ;
		 * 
		 * Robot.systemTimer.reset(); while (!mecanumDrive.driveStraight(2) &&
		 * (Robot.systemTimer.get() > 3)) ; Robot.systemTimer.reset(); break;
		 * 
		 * 
		 * case redAuto1_ShootFromHopper:
		 * 
		 * mecanumNavigation.setStartingPosition(RED_STARTING_X,
		 * MODE_1_STARTING_Y, 180); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(RED_HOPPER_X, RED_HOPPER_Y, 180)) ;
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.visionBoilerAlignment(Robot.boilerOffset)) ;
		 * Robot.systemTimer.reset(); autoShooter.autoShoot();
		 * 
		 * break;
		 * 
		 * case redAuto2_ShootFromHopper:
		 * mecanumNavigation.setStartingPosition(RED_STARTING_X,
		 * MODE_2_STARTING_Y, 180); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(RED_HOPPER_X, RED_HOPPER_Y, 180)) ;
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.visionBoilerAlignment(Robot.boilerOffset)) ;
		 * Robot.systemTimer.reset(); autoShooter.autoShoot();
		 * 
		 * break;
		 * 
		 * case redAuto1_GearandShootFromHopper:
		 * mecanumNavigation.setStartingPosition(RED_STARTING_X,
		 * MODE_1_STARTING_Y, 0); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(-16.3, -6.7, 0)) ;
		 * Robot.systemTimer.reset(); while (!mecanumDrive.rotateToHeading(60))
		 * ;
		 * 
		 * // while(!mecanumDrive.gearAlign(Robot.gearPegOffset));//place //
		 * holder Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(RED_SIDE_GEARLIFT_X, GEARLIFT_1_Y,
		 * 60)) ;
		 * 
		 * gearCollection.autoGearDeposit(1); gearCollection.setArmPosition(0);
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(RED_HOPPER_X, RED_HOPPER_Y, 180)) ;
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.visionBoilerAlignment(Robot.boilerOffset)) ;
		 * Robot.systemTimer.reset(); autoShooter.autoShoot(); break;
		 * 
		 * case redAuto1_HopperandShootFromBoiler:
		 * mecanumNavigation.setStartingPosition(RED_STARTING_X,
		 * MODE_1_STARTING_Y, 180); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(RED_HOPPER_X, RED_HOPPER_Y, 180)) ;
		 * 
		 * Timer.delay(TIME_TO_COLLECT_HOPPER); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(-22.9, -9.2, -135)) ;
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.visionBoilerAlignment(Robot.boilerOffset)) ;
		 * Robot.systemTimer.reset(); autoShooter.autoShoot();
		 * 
		 * break;
		 * /////////////////////////////////////////////////////////////////////
		 * //////////////////////////////////////////// // blue
		 * 
		 * 
		 * 
		 * case blueAuto1_ShootFromHopper:
		 * mecanumNavigation.setStartingPosition(BLUE_STARTING_X,
		 * MODE_1_STARTING_Y, 0); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(BLUE_HOPPER_X, BLUE_HOPPER_Y, 0)) ;
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.visionBoilerAlignment(Robot.boilerOffset)) ;
		 * Robot.systemTimer.reset(); autoShooter.autoShoot();
		 * 
		 * break;
		 * 
		 * case blueAuto2_ShootFromHopper:
		 * mecanumNavigation.setStartingPosition(BLUE_STARTING_X,
		 * MODE_2_STARTING_Y, 0); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(BLUE_HOPPER_X, BLUE_HOPPER_Y, 0)) ;
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.visionBoilerAlignment(Robot.boilerOffset)) ;
		 * Robot.systemTimer.reset(); autoShooter.autoShoot(); break;
		 * 
		 * case blueAuto1_GearandShootFromHopper:
		 * mecanumNavigation.setStartingPosition(BLUE_STARTING_X,
		 * MODE_1_STARTING_Y, 180); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(16.3, -6.7, -120)) ;
		 * 
		 * while (!mecanumDrive.rotateToHeading(-120)) ;
		 * Robot.systemTimer.reset(); //
		 * while(!mecanumDrive.gearAlign(Robot.gearPegOffset));
		 * 
		 * gearCollection.autoGearDeposit(.5); gearCollection.setArmPosition(0);
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(BLUE_HOPPER_X, BLUE_HOPPER_Y, 0)) ;
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.visionBoilerAlignment(Robot.boilerOffset)) ;
		 * 
		 * autoShooter.autoShoot(); break;
		 * 
		 * case blueAuto1_HopperandShootFromBoiler:
		 * mecanumNavigation.setStartingPosition(BLUE_STARTING_X,
		 * MODE_1_STARTING_Y, 0); while
		 * (!mecanumDrive.driveToCoordinate(BLUE_HOPPER_X, BLUE_HOPPER_Y, 0)) ;
		 * 
		 * Timer.delay(TIME_TO_COLLECT_HOPPER); Robot.systemTimer.reset(); while
		 * (!mecanumDrive.driveToCoordinate(22.9, -9.2, -45)) ;
		 * Robot.systemTimer.reset(); while
		 * (!mecanumDrive.visionBoilerAlignment(Robot.boilerOffset)) ;
		 * 
		 * autoShooter.autoShoot(); break;
		 */
		case testGearDeposit:
			mecanumNavigation.setStartingPosition(0, 0, 0);
			Robot.systemTimer.reset();
			while (!mecanumDrive.driveToCoordinate(0, 10, 0))
				;

			gearCollection.autoGearDeposit(1);
			gearCollection.setArmPosition(0);

			break;

		case testAutoRotate:
			SmartDashboard.putString("auto", "switch");
			Robot.systemTimer.reset();
			while (!mecanumDrive.driveStraight(10) && (Robot.systemTimer.get() < 5))
				;
			Robot.systemTimer.delay(2);
			Robot.systemTimer.reset();
			while (!mecanumDrive.rotateToHeading(90) && (Robot.systemTimer.get() < 5))
				;

			/*
			 * Robot.systemTimer.delay(1); Robot.systemTimer.reset();
			 * while(!mecanumDrive.rotateToHeading(0)&&
			 * (Robot.systemTimer.get()>2)); Robot.systemTimer.delay(1);
			 * Robot.systemTimer.reset();
			 * while(!mecanumDrive.rotateToHeading(180) &&
			 * (Robot.systemTimer.get()>2)); Robot.systemTimer.delay(1);
			 * Robot.systemTimer.reset();
			 * while(!mecanumDrive.rotateToHeading(-180) &&
			 * (Robot.systemTimer.get()>2));
			 */ SmartDashboard.putString("auto", "done");
			break;

		case testFwd:
			SmartDashboard.putString("auto", "switch");
			Robot.systemTimer.reset();
			while ((!mecanumDrive.driveStraight(5)) && (Robot.systemTimer.get() < 5))
				;

			mecanumDrive.driveInit = true;

			Robot.systemTimer.reset();
			while ((!mecanumDrive.driveStraight(5)) && (Robot.systemTimer.get() < 5))
				;

			mecanumDrive.driveInit = true;

			SmartDashboard.putString("auto", "done");

			break;

		case testCoordinateDriveSide:
			Robot.systemTimer.reset();
			while (!mecanumDrive.driveToCoordinate(5, 0, 0) && (Robot.systemTimer.get() > 3))

				break;

		default:
			break;
		}
	}
}
