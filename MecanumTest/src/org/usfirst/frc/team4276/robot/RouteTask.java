package org.usfirst.frc.team4276.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RouteTask {
	public enum Operation {
		INIT_POS, DRIVE, STRAFE_ALIGN_BOILER, STRAFE_ALIGN_GEAR, SHOOT_BOILER, PLACE_GEAR, COLLECT_FUEL, COLLECT_FUEL_STOP, COLLECT_GEAR_DEPLOY, COLLECT_GEAR_PICKUP, WAIT, STOP
	}

	public enum ReturnValue {
		SUCCESS, FAILED
	}

	public Operation op = Operation.INIT_POS;
	public RobotPosition endPos = new RobotPosition(0.0, 0.0, 0.0);
	public long delayMillisecs = 0;

	public RouteTask(Operation oper, int param) {
		op = oper;
		delayMillisecs = param;
	}

	public RouteTask(Operation oper) {
	}

	public RouteTask(Operation oper, RobotPosition pos) {
		op = oper;

		endPos.posX = pos.posX;
		endPos.posY = pos.posY;
		endPos.hdg = pos.hdg;
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
		case INIT_POS:
			// TODO:
			return ReturnValue.FAILED;

		case DRIVE:
			// TODO:
			return ReturnValue.FAILED;

		case STRAFE_ALIGN_BOILER:
			// TODO:
			return ReturnValue.FAILED;

		case STRAFE_ALIGN_GEAR:
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

		case STOP:
			// TODO:
			return ReturnValue.FAILED;

		case WAIT:
			try {
				Thread.sleep(delayMillisecs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return ReturnValue.SUCCESS;

		default:
			break;
		}
		return ReturnValue.FAILED;
	}

	public static final String opToText(Operation opr) {
		switch (opr) {
		case INIT_POS:
			return "INIT_POS";

		case DRIVE:
			return "DRIVE";

		case STRAFE_ALIGN_BOILER:
			return "STRAFE_ALIGN_BOILER";

		case STRAFE_ALIGN_GEAR:
			return "STRAFE_ALIGN_GEAR";

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

		case STOP:
			return "STOP";

		case WAIT:
			return "WAIT"; // first arg is milliseconds to wait

		default:
			break;
		}
		return "????";
	}

}
