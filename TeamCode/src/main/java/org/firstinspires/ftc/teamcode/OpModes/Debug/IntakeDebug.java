package org.firstinspires.ftc.teamcode.OpModes.Debug;

import static org.firstinspires.ftc.teamcode.Constants.Constants.IntakeConstants.*;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.OPTIONS;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.LeftGamepadTrigger;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.RightGamepadTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers.ArmIsAtHomeTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Commands.OuttakeCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Commands.StopIntakeCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.IntakeSubsystem;

import java.io.IOException;

@TeleOp(name = "Debug - Intake", group = "Debug")
@Disabled
public class IntakeDebug extends CommandOpMode {
    private IntakeSubsystem intakeSubsystem;
    private ArmSubsystem armSubsystem;

    @Override public void initialize() {
        new ConstantsLoader().loadConstants();

        armSubsystem    = new ArmSubsystem(this);
        intakeSubsystem = new IntakeSubsystem(this);

        configureBindings();

        intakeSubsystem.setDefaultCommand(new StopIntakeCommand(intakeSubsystem));

        register(armSubsystem, intakeSubsystem);

        schedule(
                new InstantCommand(this::displayHelpMessage),
                new InstantCommand(intakeSubsystem::debug),
                new InstantCommand(telemetry::update)
        );
    }

    private void configureBindings() {
        GamepadEx driverGamepad   = new GamepadEx(gamepad1);
        GamepadEx operatorGamepad = new GamepadEx(gamepad2);

        // ---------- Intake Triggers (Controlled By Gamepad 2) ---------- //

        new LeftGamepadTrigger(INTAKE_TRIGGER_THRESHOLD, operatorGamepad)
                .and(new ArmIsAtHomeTrigger(armSubsystem))
                .whenActive(new IntakeCommand(intakeSubsystem, armSubsystem));

        new RightGamepadTrigger(OUTTAKE_TRIGGER_THRESHOLD, operatorGamepad)
                .whenActive(new IntakeCommand(intakeSubsystem, armSubsystem));


        // ---------- Debug Triggers (Controlled By Either Gamepad) ---------- //

        new GamepadButton(operatorGamepad, OPTIONS)
                .or(new GamepadButton(driverGamepad, OPTIONS))
                .whenActive(this::displayIntakeControls);
    }

    private void displayHelpMessage() {
        telemetry.addLine("To see intake controls press options (The one on the right)");
    }

    private void displayIntakeControls() {
        telemetry.addLine("Control intake with gamepad 2");
        telemetry.addLine("----- Intake Controls -----");
        telemetry.addLine("Left Trigger  => intake");
        telemetry.addLine("Right Trigger => outtake");
    }
}
