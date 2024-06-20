package org.firstinspires.ftc.teamcode.OpModes.Debug;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Commands.DriveRobotCentricCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.DriveSubsystem;

import java.io.IOException;

public class DriveDebug extends CommandOpMode {
    private DriveSubsystem driveSubsystem;
    private GamepadEx driverGamepad;

    @Override public void initialize() {
        loadConstants();

        driveSubsystem = new DriveSubsystem(this);

        driverGamepad = new GamepadEx(gamepad1);

        schedule(
                new DriveRobotCentricCommand(
                        driveSubsystem,
                        driverGamepad::getLeftY,
                        driverGamepad::getLeftX,
                        driverGamepad::getRightX
                ),
                new RunCommand(driveSubsystem::debugDrive),
                new RunCommand(telemetry::update)
        );

       register(driveSubsystem);
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
