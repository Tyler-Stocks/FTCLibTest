package org.firstinspires.ftc.teamcode.OpModes.Utility;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;

import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Commands.DisplayArmPositionTelemetry;

@TeleOp(name = "Utility - Arm Measurement", group = "Utility")
public class ArmMeasurement extends CommandOpMode {

    @Override public void initialize() {
        ArmSubsystem armSubsystem = new ArmSubsystem(hardwareMap);

        armSubsystem.setDefaultCommand(new DisplayArmPositionTelemetry(armSubsystem, telemetry));

        register(armSubsystem);
    }
}
