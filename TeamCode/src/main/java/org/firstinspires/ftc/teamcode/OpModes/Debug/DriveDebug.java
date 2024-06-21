package org.firstinspires.ftc.teamcode.OpModes.Debug;

import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.OPTIONS;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Commands.DriveRobotCentricCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.DriveSubsystem;

import java.io.IOException;

@TeleOp(name = "Debug - Drive", group = "Debug")
@Disabled
public class DriveDebug extends CommandOpMode {
    private DriveSubsystem driveSubsystem;
    private GamepadEx driverGamepad;

    @Override public void initialize() {
        loadConstants();

        driveSubsystem = new DriveSubsystem(this);

        configureBindings();

        displayDriveControls();

        register(driveSubsystem);

        schedule(
                new DriveRobotCentricCommand(
                        driveSubsystem,
                        driverGamepad::getLeftY,
                        driverGamepad::getLeftX,
                        driverGamepad::getRightX
                ),
                new RunCommand(this::displayHelpMessage),
                new RunCommand(driveSubsystem::debug),
                new RunCommand(telemetry::update)
        );
    }

    private void displayHelpMessage() {
        telemetry.addLine("To see drive controls, press options (The one on the right)");
    }

    private void displayDriveControls() {
        telemetry.addLine("Controlled Using Gamepad 1");
        telemetry.addLine("----- Drive Controls -----");
        telemetry.addLine("Left Stick Y  => Drive");
        telemetry.addLine("Left Stick X  => Strafe");
        telemetry.addLine("Right Stick X => Turn");
    }

    private void configureBindings() {
       driverGamepad = new GamepadEx(gamepad1);

       new GamepadButton(driverGamepad, OPTIONS)
               .whenPressed(this::displayDriveControls);
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
