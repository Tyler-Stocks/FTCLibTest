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
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Commands.DriveRobotCentricCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.DriveSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger.HangerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Launcher.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.LeftGamepadTrigger;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.RightGamepadTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands.RetractLeftMosaicFixerCommand;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands.RetractRightMosaicFixerCommand;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerSubsystem;

import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.*;
import static org.firstinspires.ftc.teamcode.Constants.Constants.IntakeConstants.*;

@TeleOp(name = "TeleOpMain", group = "Test")
public class TeleOpMain extends CommandOpMode {

    @Override public void initialize() {
        LauncherSubsystem    launcherSubsystem    = new LauncherSubsystem(hardwareMap);
        HangerSubsystem      hangerSubsystem      = new HangerSubsystem(hardwareMap);
        IntakeSubsystem      intakeSubsystem      = new IntakeSubsystem(hardwareMap);
        ArmSubsystem         armSubsystem         = new ArmSubsystem(hardwareMap);
        DriveSubsystem       driveSubsystem       = new DriveSubsystem(hardwareMap);
        MosaicFixerSubsystem mosaicFixerSubsystem = new MosaicFixerSubsystem(hardwareMap);

        GamepadEx driverGamepad   = new GamepadEx(gamepad1);
        GamepadEx operatorGamepad = new GamepadEx(gamepad2);

        driveSubsystem.setDefaultCommand(
                new DriveRobotCentricCommand(
                        driveSubsystem,
                        driverGamepad::getLeftY,
                        driverGamepad::getLeftX,
                        driverGamepad::getRightX
                )
        );

        // -------------------- Conditional Triggers -------------------- //

        // ---------- Arm Triggers (Gamepad 2) ---------- //

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

        // ---------- Outtake Triggers (Gamepad 2) ---------- //

        new GamepadButton(operatorGamepad, LEFT_BUMPER)
                .and(new ArmIsOutsideFrameTrigger(armSubsystem))
                .toggleWhenActive(armSubsystem::openLeftOuttakeDoor, armSubsystem::closeLeftOuttakeDoor);

        new GamepadButton(operatorGamepad, RIGHT_BUMPER)
                .and(new ArmIsOutsideFrameTrigger(armSubsystem))
                .toggleWhenActive(armSubsystem::openRightOuttakeDoor, armSubsystem::closeRightOuttakeDoor);

        // ---------- Intake Triggers (Gamepad 2) ---------- //

        new LeftGamepadTrigger(INTAKE_TRIGGER_THRESHOLD, operatorGamepad)
                .and(new Trigger(armSubsystem::isAtHome)) // Prevent intaking when we are not homed
                .toggleWhenActive(intakeSubsystem::intake, intakeSubsystem::stop);


        new RightGamepadTrigger(OUTTAKE_TRIGGER_THRESHOLD, operatorGamepad)
                .toggleWhenActive(intakeSubsystem::outtake, intakeSubsystem::stop);

        // ---------- Mosaic Fixer Triggers (Gamepad 1) ---------- //

        new GamepadButton(driverGamepad, LEFT_BUMPER)
                .whenPressed(mosaicFixerSubsystem::disableLeftMosaicFixer);

        new GamepadButton(driverGamepad, RIGHT_BUMPER)
                .whenPressed(mosaicFixerSubsystem::disableRightMosaicFixer);

        new GamepadButton(driverGamepad, CROSS)
                .whenPressed(new RetractRightMosaicFixerCommand(mosaicFixerSubsystem));

        new GamepadButton(driverGamepad, SQUARE)
                .whenPressed(mosaicFixerSubsystem::moveRightMosaicFixerToLowPosition);

        new GamepadButton(driverGamepad, TRIANGLE)
                .whenPressed(mosaicFixerSubsystem::moveRightMosaicFixerToMediumPosition);

        new GamepadButton(driverGamepad, DPAD_DOWN)
                .whenPressed(new RetractLeftMosaicFixerCommand(mosaicFixerSubsystem));

        new GamepadButton(driverGamepad, DPAD_LEFT)
                .whenPressed(mosaicFixerSubsystem::moveLeftMosaicFixerToLowPosition);

        new GamepadButton(driverGamepad, DPAD_RIGHT)
                .whenPressed(mosaicFixerSubsystem::moveLeftMosaicFixerToMediumPosition);

        new GamepadButton(driverGamepad, DPAD_UP)
                .whenPressed(mosaicFixerSubsystem::moveLeftMosaicFixerToHighPosition);

        register(launcherSubsystem,
                 hangerSubsystem,
                 intakeSubsystem,
                 armSubsystem,
                 driveSubsystem,
                 mosaicFixerSubsystem);
    }
}
