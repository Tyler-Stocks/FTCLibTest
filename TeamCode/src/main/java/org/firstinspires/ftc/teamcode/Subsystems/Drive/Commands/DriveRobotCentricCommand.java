package org.firstinspires.ftc.teamcode.Subsystems.Drive.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.DriveSubsystem;

import java.util.function.DoubleSupplier;

public class DriveRobotCentricCommand extends CommandBase {
    private final DriveSubsystem drivebase;
    private final DoubleSupplier driveSupplier,
                                 strafeSupplier,
                                 turnSupplier;

    public DriveRobotCentricCommand(
            @NonNull DriveSubsystem drivebase,
            @NonNull DoubleSupplier driveSupplier,
            @NonNull DoubleSupplier strafeSupplier,
            @NonNull DoubleSupplier turnSupplier) {
        this.drivebase = drivebase;

        this.driveSupplier  = driveSupplier;
        this.strafeSupplier = strafeSupplier;
        this.turnSupplier   = turnSupplier;

        addRequirements(drivebase);
    }

    @Override public void execute() {
        this.drivebase.driveRobotCentric(
                driveSupplier.getAsDouble(),
                strafeSupplier.getAsDouble(),
                turnSupplier.getAsDouble());
    }
}
