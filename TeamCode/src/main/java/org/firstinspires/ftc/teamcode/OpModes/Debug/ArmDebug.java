package org.firstinspires.ftc.teamcode.OpModes.Debug;

import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.*;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.*;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Commands.SetArmTargetPositionCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers.ArmIsOutsideFrameTrigger;

import java.io.IOException;

public class ArmDebug extends CommandOpMode {
    private ArmSubsystem armSubsystem;
    private GamepadEx operatorGamepad;

    @Override public void initialize() {
       loadConstants();

       armSubsystem = new ArmSubsystem(hardwareMap);
       operatorGamepad = new GamepadEx(gamepad2);

       armSubsystem.setTelemetry(telemetry);

       configureBindings();

       register(armSubsystem);

       schedule(new RunCommand(armSubsystem::debugArm),
                new RunCommand(telemetry::update)
       );
    }

    private void loadConstants() {
        try {
            ConstantsLoader constantsLoader = new ConstantsLoader();
            constantsLoader.loadConstants();
        } catch (IOException ioException) {
            telemetry.addData("Failed to load constants file", ioException.getMessage());
            telemetry.update();
        }
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
}
