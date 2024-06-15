package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.*;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Commands.SetArmTargetPositionCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers.ArmIsOutsideFrameTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Commands.DriveRobotCentricCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.DriveSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger.HangerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Triggers.BackBeamBreakTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.Triggers.FrontBeamBreakTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.LEDController.LEDSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Launcher.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.LeftGamepadTrigger;
import org.firstinspires.ftc.teamcode.PlayStationController.Triggers.RightGamepadTrigger;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands.RetractLeftMosaicFixerCommand;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands.RetractRightMosaicFixerCommand;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.PurplePixelPlacer.PurplePixelPlacerSubsystem;

import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.*;
import static org.firstinspires.ftc.teamcode.Constants.Constants.IntakeConstants.*;

import static org.firstinspires.ftc.teamcode.OpModes.TeleOpMain.GameState.*;

import java.io.IOException;

@TeleOp(name = "TeleOpMain", group = "Test")
public class TeleOpMain extends CommandOpMode {

    protected enum GameState {
        TELEOP,
        ENDGAME
    }

    private GameState gameState;

    private LauncherSubsystem          launcherSubsystem;
    private HangerSubsystem            hangerSubsystem;
    private IntakeSubsystem            intakeSubsystem;
    private ArmSubsystem               armSubsystem;
    private DriveSubsystem             driveSubsystem;
    private MosaicFixerSubsystem       mosaicFixerSubsystem;
    private LEDSubsystem               ledSubsystem;
    private PurplePixelPlacerSubsystem purplePixelPlacerSubsystem;

    private GamepadEx driverGamepad, operatorGamepad;

    @Override public void initialize() {
        gameState = TELEOP;

        loadConstants();

        initializeSubsystems();
        initializeGamepads();

        setDefaultCommands();

        configureBindings();
        configureTriggers();

        register(launcherSubsystem,
                 hangerSubsystem,
                 intakeSubsystem,
                 armSubsystem,
                 driveSubsystem,
                 mosaicFixerSubsystem,
                 purplePixelPlacerSubsystem);
    }

    /**
     * Tries to load the constants files from onbot java
     */
    private void loadConstants() {
        ConstantsLoader constantsLoader = new ConstantsLoader();

        try {
            constantsLoader.loadConstants();
        } catch (IOException ioException) {
            telemetry.addData("Failed to load constants file", ioException.getCause());
            telemetry.update();
        }
    }

    /**
     * Creates all of the subsystems. Using any of them before this function is called will
     * produce a null pointer exception
     */
    private void initializeSubsystems() {
        launcherSubsystem          = new LauncherSubsystem(hardwareMap);
        hangerSubsystem            = new HangerSubsystem(hardwareMap);
        intakeSubsystem            = new IntakeSubsystem(hardwareMap);
        armSubsystem               = new ArmSubsystem(hardwareMap);
        driveSubsystem             = new DriveSubsystem(hardwareMap);
        mosaicFixerSubsystem       = new MosaicFixerSubsystem(hardwareMap);
        ledSubsystem               = new LEDSubsystem(hardwareMap);
        purplePixelPlacerSubsystem = new PurplePixelPlacerSubsystem(hardwareMap);
    }

    /**
     * Creates the driver and operator gamepads. Using them before this function is called will
     * produce a null pointer exception
     */
    private void initializeGamepads() {
        driverGamepad   = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);
    }

    /**
     * Sets the default command of each subsystem when it is applicable
     */
    private void setDefaultCommands() {
        driveSubsystem.setDefaultCommand(
                new DriveRobotCentricCommand(
                        driveSubsystem,
                        driverGamepad::getLeftY,
                        driverGamepad::getLeftX,
                        driverGamepad::getRightX
                )
        );

        intakeSubsystem.setDefaultCommand(new InstantCommand(intakeSubsystem::stop));
    }

    /**
     * Configures all of the button bindings
     */
    private void configureBindings() {
        // ---------- Arm Triggers (Controlled by operator) ---------- //

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
                .and(new Trigger(this::isInEndgame))
                .whenActive(new SetArmTargetPositionCommand(
                        armSubsystem, HANGING_WORM_POSITION, 0));

        new GamepadButton(operatorGamepad, RIGHT_BUMPER)
                .and(new Trigger(this::isInEndgame))
                .whenActive(new SetArmTargetPositionCommand(
                        armSubsystem, LAUNCHING_WORM_POSITION, 0));

        // ---------- Outtake Triggers (Controlled By Operator) ---------- //

        new GamepadButton(operatorGamepad, LEFT_BUMPER)
                .and(new ArmIsOutsideFrameTrigger(armSubsystem))
                .toggleWhenActive(armSubsystem::openLeftOuttakeDoor, armSubsystem::closeLeftOuttakeDoor);

        new GamepadButton(operatorGamepad, RIGHT_BUMPER)
                .and(new ArmIsOutsideFrameTrigger(armSubsystem))
                .toggleWhenActive(armSubsystem::openRightOuttakeDoor, armSubsystem::closeRightOuttakeDoor);

        // ---------- Intake Triggers (Controlled By Operator) ---------- //

        new LeftGamepadTrigger(INTAKE_TRIGGER_THRESHOLD, operatorGamepad)
                .and(new Trigger(armSubsystem::isAtHome)) // Prevent intaking when we are not homed
                .whenActive(intakeSubsystem::intake);

        new RightGamepadTrigger(OUTTAKE_TRIGGER_THRESHOLD, operatorGamepad)
                .whenActive(intakeSubsystem::outtake);

        // ---------- Mosaic Fixer Triggers (Controlled By Driver) ---------- //

        new GamepadButton(driverGamepad, LEFT_BUMPER)
                .whenPressed(mosaicFixerSubsystem::disableLeftMosaicFixer);

        new GamepadButton(driverGamepad, RIGHT_BUMPER)
                .whenPressed(mosaicFixerSubsystem::disableRightMosaicFixer);

        new GamepadButton(driverGamepad, CROSS)
                .whenPressed(new RetractRightMosaicFixerCommand(mosaicFixerSubsystem));

        new GamepadButton(driverGamepad, SQUARE)
                .whenPressed(mosaicFixerSubsystem::moveRightMosaicFixerToLowPosition);

        new GamepadButton(driverGamepad, TRIANGLE)
                .whenPressed(mosaicFixerSubsystem::moveRightMosaicFixerToHighPosition);

        new GamepadButton(driverGamepad, DPAD_DOWN)
                .whenPressed(new RetractLeftMosaicFixerCommand(mosaicFixerSubsystem));

        new GamepadButton(driverGamepad, DPAD_LEFT)
                .whenPressed(mosaicFixerSubsystem::moveLeftMosaicFixerToLowPosition);

        new GamepadButton(driverGamepad, DPAD_RIGHT)
                .whenPressed(mosaicFixerSubsystem::moveLeftMosaicFixerToMediumPosition);

        new GamepadButton(driverGamepad, DPAD_UP)
                .whenPressed(mosaicFixerSubsystem::moveLeftMosaicFixerToHighPosition);

        // ---------- Drive Commands (Controlled By Driver) ---------- //

        new GamepadButton(driverGamepad, OPTIONS)
                .whenPressed(driveSubsystem::resetIMU);

        // ---------- End Game Trigger (Controlled By Operator) ---------- //

        new GamepadButton(operatorGamepad, OPTIONS)
                .whenPressed(this::toggleGameState);
    }

    /**
     * Configures the automatic triggers, such as LED's
     */
    private void configureTriggers() {
        // ---------- LED Triggers ---------- //

        new FrontBeamBreakTrigger(intakeSubsystem)
                .toggleWhenActive(ledSubsystem::setLeftLEDRed, ledSubsystem::setLeftLEDGreen);

        new BackBeamBreakTrigger(intakeSubsystem)
                .toggleWhenActive(ledSubsystem::setRightLEDRed, ledSubsystem::setRightLEDGreen);
    }

    /**
     * Toggles the game state between TELEOP and ENDGAME
     */
    private void toggleGameState() {
       switch (gameState) {
           case TELEOP:
               gameState = ENDGAME;
               break;
           case ENDGAME:
               gameState = TELEOP;
               break;
       }
    }

    /**
     * @return Whether the teleop is currently in endgame
     */
    private boolean isInEndgame() {
        return gameState == ENDGAME;
    }

    /**
     * @return Whether the teleop is currently in teleop
     */
    private boolean isInTeleOp() {
        return gameState == TELEOP;
    }
}
