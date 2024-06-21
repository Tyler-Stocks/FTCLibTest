package org.firstinspires.ftc.teamcode.Subsystems.Drive.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.DriveSubsystem;

public class ResetIMUCommand extends CommandBase {
    private final DriveSubsystem driveSubsystem;

    private boolean isFinished;

    /**
     * Constructs a new ResetIMUCommand
     * @param driveSubsystem The drive subsystem to reset the imu of
     */
    public ResetIMUCommand(@NonNull DriveSubsystem driveSubsystem) {
       this.driveSubsystem = driveSubsystem;
       this.isFinished     = false;
    }

    @Override public void execute() {
       driveSubsystem.resetIMU();
       isFinished = true;
    }

    @Override public boolean isFinished() {
        return isFinished;
    }
}
