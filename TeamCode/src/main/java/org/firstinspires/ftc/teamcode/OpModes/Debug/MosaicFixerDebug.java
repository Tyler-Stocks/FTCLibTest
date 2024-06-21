package org.firstinspires.ftc.teamcode.OpModes.Debug;

import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.CROSS;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.DPAD_UP;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.OPTIONS;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.SQUARE;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.TRIANGLE;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerSubsystem;

import java.io.IOException;

@TeleOp(name = "Debug - Mosaic Fixer", group = "Debug")
public class MosaicFixerDebug extends CommandOpMode {
    private MosaicFixerSubsystem mosaicFixerSubsystem;

    @Override public void initialize() {
       loadConstants();

       mosaicFixerSubsystem = new MosaicFixerSubsystem(this);

       configureBindings();

       displayDebugInstructions();

       register(mosaicFixerSubsystem);

       schedule(
               new RunCommand(this::displayHelpMessage),
               new RunCommand(mosaicFixerSubsystem::debugMosaicFixers),
               new RunCommand(telemetry::update)
       );
    }

    private void displayDebugInstructions() {
        telemetry.addLine("Controlled Using Gamepad 1");
        telemetry.addLine("----- Mosaic Fixer Controls -----");
        telemetry.addLine("Left Bumper  => Disable mosaic fixer left");
        telemetry.addLine("Right Bumper => Disable mosaic fixer right");
        telemetry.addLine("Cross        => Retract right mosaic fixer");
        telemetry.addLine("Square       => Move right mosaic fixer to low position");
        telemetry.addLine("Triangle     => Move right mosaic fixer to high position");
        telemetry.addLine("Dpad Down    => Retract left mosaic fixer");
        telemetry.addLine("Dpad Left    => Move left mosaic fixer to low position");
        telemetry.addLine("Dpad Right   => Move left mosaic fixer to medium position");
        telemetry.addLine("Dpad Up      => Move left mosaic fixer to high position");
    }

    private void displayHelpMessage() {
        telemetry.addLine("To display controls press options");
    }

    private void configureBindings() {
        GamepadEx driverGamepad = new GamepadEx(gamepad1);

        // ---------- Mosaic Fixer Bindings (Controlled By Gamepad 1) ---------- //

        new GamepadButton(driverGamepad, LEFT_BUMPER)
                .whenPressed(mosaicFixerSubsystem::disableLeftMosaicFixer);

        new GamepadButton(driverGamepad, RIGHT_BUMPER)
                .whenPressed(mosaicFixerSubsystem::disableRightMosaicFixer);

        new GamepadButton(driverGamepad, CROSS)
                .whenPressed(mosaicFixerSubsystem::retractMosaicFixerRight);

        new GamepadButton(driverGamepad, SQUARE)
                .whenPressed(mosaicFixerSubsystem::moveRightMosaicFixerToLowPosition);

        new GamepadButton(driverGamepad, TRIANGLE)
                .whenPressed(mosaicFixerSubsystem::moveRightMosaicFixerToHighPosition);

        new GamepadButton(driverGamepad, DPAD_DOWN)
                .whenPressed(mosaicFixerSubsystem::retractMosaicFixerLeft);

        new GamepadButton(driverGamepad, DPAD_LEFT)
                .whenPressed(mosaicFixerSubsystem::moveLeftMosaicFixerToLowPosition);

        new GamepadButton(driverGamepad, DPAD_RIGHT)
                .whenPressed(mosaicFixerSubsystem::moveLeftMosaicFixerToMediumPosition);

        new GamepadButton(driverGamepad, DPAD_UP)
                .whenPressed(mosaicFixerSubsystem::moveLeftMosaicFixerToHighPosition);

        // ---------- Debug Instructions (Controlled Gamepad 1) ---------- //

        new GamepadButton(driverGamepad, OPTIONS)
                .whenPressed(this::displayDebugInstructions);
    }

    private void loadConstants() {
        try {
            new ConstantsLoader().loadConstants();
        } catch (IOException ioException) {
            telemetry.addData("Failed to load constants from file", ioException.getMessage());
        }
    }
}
