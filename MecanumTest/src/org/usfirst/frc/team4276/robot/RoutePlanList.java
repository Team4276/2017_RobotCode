package org.usfirst.frc.team4276.robot;

import java.util.ArrayList;

import org.usfirst.frc.team4276.robot.RoutePlan.AllianceColor;

//Field coordinates:
//  origin (0,0) is at the center of the field
//  North (0.0 degrees) is toward the end of the field with the blue alliance boiler
//  Drivers for the blue alliance will face south (180 deg), with positive Y coordinates that decrease to zero as position approaches mid field
//  Drivers for the red alliance face north (0.0 deg), and have negative Y coordinates that increase to zero as position approaches mid field
//
//
//         Blue Drivers       
//  ************************* Blue Boiler
//  *        (0, 12)        *
//  *                       *
//  *                       *
//  *                       *
//  *                       *
//  *                       *    ^  North (0.0 degrees)
//  *                       *   /^\
//  *                       *    |
//  *                       *    +
//  *                       *   +++
//  *(-12, 0) (0,0)  (12, 0)*    +
//  *                       *
//  *                       *
//  *                       *
//  *                       *
//  *                       *
//  *                       *
//  *                       *
//  *                       *
//  *                       *
//  *                       *
//  *       (0, -12)        *
//  ************************* Red Boiler
//        Red Drivers

@SuppressWarnings("serial")
public class RoutePlanList extends ArrayList<RoutePlan> {

	// TODO: Determine the exact coordinates for position constants, which are
	// defined only approximately below

	// Inputs to RobotPosition are (X, Y, HDG)
	public static final RobotPosition posBlueBoiler = new RobotPosition(9.5, 9.5, 30.0);

	// Lift "R" is to drivers right, regardless of alliance
	public static final RobotPosition posBlueLift_R = new RobotPosition(-5.0, 8.0, 100.0);
	public static final RobotPosition posBlueLift_C = new RobotPosition(0.0, 10.0, 180.0);
	public static final RobotPosition posBlueLift_L = new RobotPosition(5.0, 8.0, 260.0);

	// Start position "R" is to drivers right, regardless of alliance
	public static final RobotPosition posBlueStart_R = new RobotPosition(-9.0, 12.0, 180.0);

	// Hopper position "R" is to drivers right, regardless of alliance
	public static final RobotPosition posBlueHopper_R = new RobotPosition(-9.0, 5.0, 90.0);

	public RoutePlanList() {
		RoutePlan bluePlaceGear = new RoutePlan("Blue Place Gear", AllianceColor.BLUE);
		bluePlaceGear.add(new RouteTask(RouteTask.Operation.INIT_POS, posBlueStart_R));
		bluePlaceGear.add(new RouteTask(RouteTask.Operation.DRIVE, posBlueLift_C));
		bluePlaceGear.add(new RouteTask(RouteTask.Operation.STRAFE_ALIGN_GEAR, posBlueLift_C));
		bluePlaceGear.add(new RouteTask(RouteTask.Operation.PLACE_GEAR, posBlueLift_C));
		bluePlaceGear.add(new RouteTask(RouteTask.Operation.STOP));
		add(bluePlaceGear);

		RoutePlan blueShootInitialTen = new RoutePlan("Blue Shoot Initial Ten", AllianceColor.BLUE);
		blueShootInitialTen.add(new RouteTask(RouteTask.Operation.INIT_POS, posBlueStart_R));
		blueShootInitialTen.add(new RouteTask(RouteTask.Operation.DRIVE, posBlueBoiler));
		blueShootInitialTen.add(new RouteTask(RouteTask.Operation.STRAFE_ALIGN_BOILER, posBlueBoiler));
		blueShootInitialTen.add(new RouteTask(RouteTask.Operation.SHOOT_BOILER, posBlueBoiler));
		blueShootInitialTen.add(new RouteTask(RouteTask.Operation.STOP));
		add(blueShootInitialTen);

		RoutePlan bluePlaceGearGetHopperAndShoot = new RoutePlan("Blue Place Gear Get Hopper And Shoot",
				AllianceColor.BLUE);
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.INIT_POS, posBlueStart_R));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.DRIVE, posBlueLift_C));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.STRAFE_ALIGN_GEAR, posBlueLift_C));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.PLACE_GEAR, posBlueLift_C));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.DRIVE, posBlueLift_C));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.DRIVE, posBlueHopper_R));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.WAIT, 1000));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.COLLECT_FUEL));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.DRIVE, posBlueBoiler));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.COLLECT_FUEL_STOP));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.STRAFE_ALIGN_BOILER, posBlueBoiler));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.SHOOT_BOILER, posBlueBoiler));
		bluePlaceGearGetHopperAndShoot.add(new RouteTask(RouteTask.Operation.STOP));
		add(bluePlaceGearGetHopperAndShoot);

	}
}
