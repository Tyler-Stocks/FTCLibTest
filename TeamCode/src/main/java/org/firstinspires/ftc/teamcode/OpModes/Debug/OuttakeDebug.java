package org.firstinspires.ftc.teamcode.OpModes.Debug;

import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.BOTTOM_ELEVATOR_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.BOTTOM_WORM_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.HANGING_WORM_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.HIGH_ELEVATOR_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.HIGH_WORM_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.LAUNCHING_WORM_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.LOW_ELEVATOR_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.LOW_WORM_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.MEDIUM_ELEVATOR_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.MEDIUM_WORM_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.TOP_ELEVATOR_POSITION;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.TOP_WORM_POSITION;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.CIRCLE;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.CROSS;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.OPTIONS;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.SQUARE;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.TRIANGLE;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Commands.SetArmTargetPositionCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers.ArmIsOutsideFrameTrigger;

import java.io.IOException;

public class OuttakeDebug extends CommandOpMode {
    private ArmSubsystem armSubsystem;
    private GamepadEx operatorGamepad;

    @Override public void initialize() {
       loadConstants();

       armSubsystem = new ArmSubsystem(this);

       operatorGamepad = new GamepadEx(gamepad2);

       configureBindings();

       schedule(
               new RunCommand(armSubsystem::debugOuttake),
               new RunCommand(telemetry::update)
       );

       register(armSubsystem);
    }

    private void configureBindings() {
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
    }

    private void loadConstants() {
        try {
            new ConstantsLoader().loadConstants();
        } catch (IOException ioException) {
            telemetry.addData("Failed to read constants file", ioException.getMessage());
        }
    }
}
