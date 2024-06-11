package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.*;

import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Commands.SetArmTargetPositionCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers.ArmIsOutsideFrameTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger.HangerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Launcher.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.LeftGamepadTrigger;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.RightGamepadTrigger;

import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.*;

@TeleOp(name = "TeleOpMain", group = "Test")
public class TeleOpMain extends CommandOpMode {

    @Override public void initialize() {
        LauncherSubsystem launcherSubsystem = new LauncherSubsystem(hardwareMap);
        HangerSubsystem   hangerSubsystem   = new HangerSubsystem(hardwareMap);
        IntakeSubsystem   intakeSubsystem   = new IntakeSubsystem(hardwareMap);
        ArmSubsystem      armSubsystem      = new ArmSubsystem(hardwareMap);

        GamepadEx operatorGamepad = new GamepadEx(gamepad1);

        // Arm Triggers

        new GamepadButton(operatorGamepad, DPAD_DOWN)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, 0, 0), true);

        new GamepadButton(operatorGamepad, CROSS)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, BOTTOM_WORM_POSITION, BOTTOM_ELEVATOR_POSITION), true);

        new GamepadButton(operatorGamepad, SQUARE)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, LOW_WORM_POSITION, LOW_ELEVATOR_POSITION), true);

        new GamepadButton(operatorGamepad, CIRCLE)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, MEDIUM_WORM_POSITION, MEDIUM_ELEVATOR_POSITION), true);

        new GamepadButton(operatorGamepad, TRIANGLE)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, HIGH_WORM_POSITION, HIGH_ELEVATOR_POSITION), true);

        new GamepadButton(operatorGamepad, OPTIONS)
                .whenPressed(new SetArmTargetPositionCommand(
                        armSubsystem, TOP_WORM_POSITION, TOP_ELEVATOR_POSITION), true);

        // Left outtake door trigger
        new GamepadButton(operatorGamepad, LEFT_BUMPER)
                .and(new ArmIsOutsideFrameTrigger(armSubsystem))
                .toggleWhenActive(armSubsystem::openLeftOuttakeDoor, armSubsystem::closeLeftOuttakeDoor);

        // Right outtake door trigger
        new GamepadButton(operatorGamepad, RIGHT_BUMPER)
                .and(new ArmIsOutsideFrameTrigger(armSubsystem))
                .toggleWhenActive(armSubsystem::openRightOuttakeDoor, armSubsystem::closeRightOuttakeDoor);

        // Intake Trigger
        new LeftGamepadTrigger(0.2, operatorGamepad)
                .and(new Trigger(armSubsystem::isAtHome))
                .toggleWhenActive(intakeSubsystem::intake, intakeSubsystem::stop);

        // Outtake Trigger
        new RightGamepadTrigger(0.2, operatorGamepad)
                .toggleWhenActive(intakeSubsystem::outtake, intakeSubsystem::stop);

        register(launcherSubsystem, hangerSubsystem, intakeSubsystem, armSubsystem);
    }
}
