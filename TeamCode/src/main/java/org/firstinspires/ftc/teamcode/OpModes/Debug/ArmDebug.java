package org.firstinspires.ftc.teamcode.OpModes.Debug;

import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.*;
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
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Commands.SetArmTargetPositionCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers.ArmIsOutsideFrameTrigger;

import java.io.IOException;

@TeleOp(name = "Debug - Arm", group = "Debug")
@Disabled
public class ArmDebug extends CommandOpMode {
    private ArmSubsystem armSubsystem;

    @Override public void initialize() {
       loadConstants();

       armSubsystem = new ArmSubsystem(this);

       configureBindings();

       displayArmInstructions();

       register(armSubsystem);

       schedule(
               new RunCommand(this::displayHelpMessage),
               new RunCommand(armSubsystem::debugArm),
               new RunCommand(telemetry::update)
       );
    }

    private void displayHelpMessage() {
        telemetry.addLine("To view arm controls press share (The one on the left)");
    }

    private void displayArmInstructions() {
        telemetry.addLine("Controlled Using Gamepad 2");
        telemetry.addLine("----- Arm Controls -----");
        telemetry.addLine("Dpad Down    => Home arm");
        telemetry.addLine("Cross        => Move arm to bottom position");
        telemetry.addLine("Square       => Move arm to low position");
        telemetry.addLine("Circle       => Move arm to medium position");
        telemetry.addLine("Triangle     => Move arm to high position");
        telemetry.addLine("Options      => Move arm to top position");
        telemetry.addLine("Left Bumper  => Open left outtake arm");
        telemetry.addLine("Right Bumper => Open right outtake arm");
    }

    private void loadConstants() {
        try {
            new ConstantsLoader().loadConstants();
        } catch (IOException ioException) {
            telemetry.addData("Failed to load constants file", ioException.getMessage());
            telemetry.update();
        }
    }

    private void configureBindings() {
        GamepadEx operatorGamepad = new GamepadEx(gamepad2);

        // ----- Arm Triggers ----- //

        new GamepadButton(operatorGamepad, DPAD_DOWN)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, 0, 0));

        new GamepadButton(operatorGamepad, CROSS)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, BOTTOM_WORM_POSITION, BOTTOM_ELEVATOR_POSITION));

        new GamepadButton(operatorGamepad, SQUARE)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, LOW_WORM_POSITION, LOW_ELEVATOR_POSITION));

        new GamepadButton(operatorGamepad, CIRCLE)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, MEDIUM_WORM_POSITION, MEDIUM_ELEVATOR_POSITION));

        new GamepadButton(operatorGamepad, TRIANGLE)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, HIGH_WORM_POSITION, HIGH_ELEVATOR_POSITION));

        new GamepadButton(operatorGamepad, OPTIONS)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, TOP_WORM_POSITION, TOP_ELEVATOR_POSITION));

        new GamepadButton(operatorGamepad, LEFT_BUMPER)
                .whenActive(new SetArmTargetPositionCommand(
                        armSubsystem, HANGING_WORM_POSITION, 0));

        new GamepadButton(operatorGamepad, RIGHT_BUMPER)
                .whenActive(new SetArmTargetPositionCommand(
                        armSubsystem, LAUNCHING_WORM_POSITION, 0));

        // ---------- Outtake Triggers (Controlled By Operator) ---------- //

        new GamepadButton(operatorGamepad, LEFT_BUMPER)
                .and(new ArmIsOutsideFrameTrigger(armSubsystem))
                .toggleWhenActive(armSubsystem::openLeftOuttakeDoor, armSubsystem::closeLeftOuttakeDoor);

        new GamepadButton(operatorGamepad, RIGHT_BUMPER)
                .and(new ArmIsOutsideFrameTrigger(armSubsystem))
                .toggleWhenActive(armSubsystem::openRightOuttakeDoor, armSubsystem::closeRightOuttakeDoor);

        new GamepadButton(operatorGamepad, SHARE)
                .whenPressed(this::displayArmInstructions);
    }
}
