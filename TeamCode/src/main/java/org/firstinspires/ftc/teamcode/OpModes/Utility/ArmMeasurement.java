package org.firstinspires.ftc.teamcode.OpModes.Utility;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;

@TeleOp(name = "Utility - Arm Measurement", group = "Utility")
public class ArmMeasurement extends OpMode {
    private static DcMotorImplEx wormMotor, elevatorMotor;

    private static boolean brake = false;

    @Override public void init() {
        wormMotor     = hardwareMap.get(DcMotorImplEx.class, "WormMotor");
        elevatorMotor = hardwareMap.get(DcMotorImplEx.class, "ElevatorMotor");

        elevatorMotor.setDirection(REVERSE);
    }

    @Override public void loop() {
        if (brake) {
            wormMotor.setZeroPowerBehavior(BRAKE);
            elevatorMotor.setZeroPowerBehavior(BRAKE);
        } else {
            wormMotor.setZeroPowerBehavior(FLOAT);
            elevatorMotor.setZeroPowerBehavior(FLOAT);
        }

        telemetry.addLine("Press Options to toggle between brake and float");
        telemetry.addData("Elevator Position", elevatorMotor.getCurrentPosition());
        telemetry.addData("Worm Position", wormMotor.getCurrentPosition());

        if (gamepad1.options || gamepad2.options) brake = !brake;
    }
}
