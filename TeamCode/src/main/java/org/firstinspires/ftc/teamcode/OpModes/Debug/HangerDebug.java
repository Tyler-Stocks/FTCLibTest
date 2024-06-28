package org.firstinspires.ftc.teamcode.OpModes.Debug;

import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.*;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers.WormIsAtPositionTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger.HangerSubsystem;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.*;

import java.io.IOException;


@TeleOp(name = "Debug - Hanger", group = "Debug")
@Disabled
public class HangerDebug extends CommandOpMode {
    private HangerSubsystem hangerSubsystem;
    private ArmSubsystem armSubsystem;

    @Override public void initialize() {
        new ConstantsLoader().loadConstants();

        armSubsystem    = new ArmSubsystem(this);
        hangerSubsystem = new HangerSubsystem(this);

        configureBindings();

        register(armSubsystem, hangerSubsystem);

        schedule(
                new RunCommand(this::displayHelpMessage),
                new RunCommand(hangerSubsystem::debug),
                new RunCommand(telemetry::update)
        );
    }

    private void displayHelpMessage() {
        telemetry.addLine("Press share to see hanger controls (The one on the left)");
    }

    private void displayHangerControls() {
        telemetry.addLine("Controlled with gamepad 2");
        telemetry.addLine("----- Hanger Controls -----");
        telemetry.addLine("Options => Release hanger");
    }

    private void configureBindings() {
        GamepadEx operatorGamepad = new GamepadEx(gamepad2);

        new GamepadButton(operatorGamepad, OPTIONS)
                .and(new WormIsAtPositionTrigger(armSubsystem, LAUNCHING_WORM_POSITION))
                .whenActive(hangerSubsystem::releaseHanger);

        new GamepadButton(operatorGamepad, SHARE)
                .whenPressed(this::displayHangerControls);
    }
}
