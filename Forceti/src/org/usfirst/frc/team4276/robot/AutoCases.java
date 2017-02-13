package org.usfirst.frc.team4276.robot;

/*
 * 	
 * Field Coordinate System
 *                             ^
 *    _________________________|__________________________
 *    |                        |                         |
 *    |                        |+Y                       |
 *    |                        |                         |
 *    |                        |                         |
 *    |     -X                 |              +X         |
 *  <-|------------------------|-------------------------|->
 *    |                        |                         |
 *    |                        |                         |
 *    |	Red Alliance           |        Blue Alliance    | 
 *    |                        |-Y                       |
 *    |________________________|_________________________|
 *    Boiler                   |                    Boiler
 *                             V
 *                             
 *   @author Avery                         
*/
 


public class AutoCases {

	void autoModes() {
		//int automode = autoModeSelector.autonomousModeNumber;
		
		
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
		
		final int testGearCollection = 40;
		
		int automode = testGearCollection;
		
		switch (automode) {
		case nothing:
			break;
		case redAuto2_ScoreGear:
			mecanumNavigation.setStartingPosition(-25.8, 0);
			while(!mecanumDrive.driveToCoordinate(-15, 0, 0));
			
			while(!mecanumDrive.gearAlign(0));//place holder, still need to determine the
										// targetXOffset
			while(!mecanumDrive.driveToCoordinate(-16.3, 0, 0));
			
			gearCollection.autoGearDeposit(1);
			gearCollection.setArmPosition(0);
			break;

		case redAuto2_GearandZone:
			mecanumNavigation.setStartingPosition(-25.8, 0);
			while(!mecanumDrive.driveToCoordinate(-15, 0, 0));
			
			while(!mecanumDrive.gearAlign(0));//place holder
			
			while(!mecanumDrive.driveToCoordinate(-16.3, 0, 0));
			
			gearCollection.autoGearDeposit(.5);
			gearCollection.setArmPosition(0);
			
			while(!mecanumDrive.driveToCoordinate(-17.5, 0, 0));
			
			while(mecanumDrive.driveToCoordinate(-15,6.7,0));
			break;

		case redAuto3_GearandZone:
			mecanumNavigation.setStartingPosition(-25.8,6.7);
			while(!mecanumDrive.driveToCoordinate(-17.5,6.7,0));
			
			while(!mecanumDrive.rotateToHeading(-60));
			
			while(!mecanumDrive.gearAlign(0));//place holder, still need to determine the
										// targetXOffset
			while(!mecanumDrive.driveToCoordinate(-15,3.3,-60));
			
			gearCollection.autoGearDeposit(1);
			gearCollection.setArmPosition(0);
			break;

		case redAuto1_GearandZone:

			mecanumNavigation.setStartingPosition(-25.8,-6.7);
			while(!mecanumDrive.driveToCoordinate(-16.3,-6.7,0));
			
			while(!mecanumDrive.rotateToHeading(60));
			
			while(!mecanumDrive.gearAlign(0));//place holder
			
			while(!mecanumDrive.driveToCoordinate(-15,-3.3,60));
			
			gearCollection.autoGearDeposit(1);
			gearCollection.setArmPosition(0);
			
			break;

		case redAuto1_ShootFromHopper:

			break;

		case redAuto2_ShootFromHopper:

			break;
		case testGearCollection:
			
			gearCollection.autoGearDeposit(1);
			gearCollection.setArmPosition(0);
			
			break;
		default:
		}
	}
}
