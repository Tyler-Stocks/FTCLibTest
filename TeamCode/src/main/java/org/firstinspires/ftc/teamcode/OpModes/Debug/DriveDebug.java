package org.firstinspires.ftc.teamcode.OpModes.Debug;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Commands.DriveRobotCentricCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.DriveSubsystem;

public class DriveDebug extends CommandOpMode {
    private DriveSubsystem driveSubsystem;
    private GamepadEx driverGamepad;

    @Override public void initialize() {
       driveSubsystem = new DriveSubsystem(hardwareMap);

       driveSubsystem.setTelemetry(telemetry);

       driverGamepad = new GamepadEx(gamepad1);

       schedule(
               new DriveRobotCentricCommand(
                       driveSubsystem,
                       this::getLeftY, // We need a wrapper function to negate it
                       driverGamepad::getLeftX,
                       driverGamepad::getRightX
               ),
               new RunCommand(driveSubsystem::debugDrive),
               new RunCommand(telemetry::update)
       );

       register(driveSubsystem);
    }

    private double getLeftY() {
        return driverGamepad.getLeftY() * -1.0;
    }
}
