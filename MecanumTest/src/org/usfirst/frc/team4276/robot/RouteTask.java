package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RouteTask {
	public enum Operation {
		STOP, WAIT, DRIVE, STRAFE_ALIGN_BOILER, SHOOT_BOILER, PLACE_GEAR, COLLECT_FUEL, COLLECT_FUEL_STOP, COLLECT_GEAR_DEPLOY, COLLECT_GEAR_PICKUP
	}

	public enum DrivingSpeed {
		STOPPED, SLOW_SPEED, SLOWER_SPEED, FULL_SPEED
	}

	public enum ReturnValue {
		SUCCESS, FAILED
	}

	public Operation op = Operation.STOP;
	public RobotPositionPolar endPos = new RobotPositionPolar(true, 0.0, 0.0, 0.0);
	public long delayMillisecs = 0;

	public RouteTask(Operation oper, int param) {
		op = oper;
		delayMillisecs = param;
	}

	public RouteTask(Operation oper) {
	}

	public RouteTask(Operation oper, RobotPositionPolar pos) {
		op = oper;

		endPos.isBlueBoiler = pos.isBlueBoiler;
		endPos.radius = pos.radius;
		endPos.hdgToBoiler = pos.hdgToBoiler;
		endPos.yawOffsetRobot = pos.yawOffsetRobot;
	}

	public String displayText() {
		String sRet = opToText(op);
		if (op == Operation.DRIVE) {
			sRet += " to " + endPos.displayText();
		} else if (op == Operation.WAIT) {
			sRet += " ";
			sRet += delayMillisecs;
			sRet += " ms.";
		}
		return sRet;
	}

	public ReturnValue exec() {
		SmartDashboard.putString("Auto Status", displayText());

		switch (op) {
		case STOP:
			// TODO:
			// All motors except for vision system off, including drive, collectors, gear collector, etc.
			return ReturnValue.FAILED;

		case WAIT:
			try {
				Thread.sleep(delayMillisecs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return ReturnValue.SUCCESS;

		case DRIVE:
			// TODO:  
			return ReturnValue.FAILED;

		case STRAFE_ALIGN_BOILER:
			// TODO:
			return ReturnValue.FAILED;

		case SHOOT_BOILER:
			// TODO:
			return ReturnValue.FAILED;

		case PLACE_GEAR:
			// TODO:
			return ReturnValue.FAILED;

		case COLLECT_FUEL:
			// TODO:
			return ReturnValue.FAILED;

		case COLLECT_FUEL_STOP:
			// TODO:
			return ReturnValue.FAILED;

		case COLLECT_GEAR_DEPLOY:
			// TODO:
			return ReturnValue.FAILED;

		case COLLECT_GEAR_PICKUP:
			// TODO:
			return ReturnValue.FAILED;

		default:
			break;
		}
		return ReturnValue.FAILED;
	}

	public static final String opToText(Operation opr) {
		switch (opr) {
		case STOP:
			return "STOP";

		case WAIT:
			return "WAIT"; // first arg is milliseconds to wait

		case DRIVE:
			return "DRIVE";

		case STRAFE_ALIGN_BOILER:
			return "STRAFE_ALIGN_BOILER";

		case SHOOT_BOILER:
			return "SHOOT_BOILER";

		case PLACE_GEAR:
			return "PLACE_GEAR";

		case COLLECT_FUEL:
			return "COLLECT_FUEL";

		case COLLECT_FUEL_STOP:
			return "COLLECT_FUEL_STOP";

		case COLLECT_GEAR_DEPLOY:
			return "COLLECT_GEAR_DEPLOY";

		case COLLECT_GEAR_PICKUP:
			return "COLLECT_GEAR_PICKUP";

		default:
			break;
		}
		return "????";
	}

}
