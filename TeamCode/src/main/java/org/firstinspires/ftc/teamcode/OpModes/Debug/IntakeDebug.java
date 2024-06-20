package org.firstinspires.ftc.teamcode.OpModes.Debug;

import static org.firstinspires.ftc.teamcode.Constants.Constants.IntakeConstants.*;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.LeftGamepadTrigger;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.RightGamepadTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Commands.OuttakeCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Commands.StopIntakeCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.IntakeSubsystem;

import java.io.IOException;

public class IntakeDebug extends CommandOpMode {
    private IntakeSubsystem intakeSubsystem;
    private ArmSubsystem armSubsystem;

    private GamepadEx operatorGamepad;

    @Override public void initialize() {
        loadConstants();

        armSubsystem    = new ArmSubsystem(this);
        intakeSubsystem = new IntakeSubsystem(this);

        operatorGamepad = new GamepadEx(gamepad2);

        new LeftGamepadTrigger(INTAKE_TRIGGER_THRESHOLD, operatorGamepad)
                .whenActive(new IntakeCommand(intakeSubsystem, armSubsystem));

        new RightGamepadTrigger(OUTTAKE_TRIGGER_THRESHOLD, operatorGamepad)
                .whenActive(new OuttakeCommand(intakeSubsystem, armSubsystem));

        intakeSubsystem.setDefaultCommand(new StopIntakeCommand(intakeSubsystem));

        register(armSubsystem, intakeSubsystem);
    }

    private void loadConstants() {
        try {
            new ConstantsLoader().loadConstants();
        } catch (IOException ioException) {
            telemetry.addLine("Failed to load constants " + ioException.getMessage());
            telemetry.update();
        }
    }
}
