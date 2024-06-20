package org.firstinspires.ftc.teamcode.OpModes.Debug;

import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.*;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers.WormIsAtPositionTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger.HangerSubsystem;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.*;

import java.io.IOException;


@TeleOp(name = "Debug - Hanger", group = "Debug")
public class HangerDebug extends CommandOpMode {
    private HangerSubsystem hangerSubsystem;
    private ArmSubsystem armSubsystem;

    private GamepadEx operatorGamepad;

    @Override public void initialize() {
        loadConstants();

        armSubsystem    = new ArmSubsystem(this);
        hangerSubsystem = new HangerSubsystem(this);

        operatorGamepad = new GamepadEx(gamepad2);

        new GamepadButton(operatorGamepad, OPTIONS)
                .and(new WormIsAtPositionTrigger(armSubsystem, LAUNCHING_WORM_POSITION))
                .whenActive(hangerSubsystem::releaseHanger);

        schedule(
                new InstantCommand(hangerSubsystem::debug),
                new InstantCommand(telemetry::update)
        );
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
