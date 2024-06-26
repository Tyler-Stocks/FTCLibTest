package org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerSubsystem;

public class DisableLeftMosaicFixer extends CommandBase {
    private final MosaicFixerSubsystem mosaicFixerSubsystem;

    private boolean isFinished;

    public DisableLeftMosaicFixer(@NonNull MosaicFixerSubsystem mosaicFixerSubsystem) {
        this.mosaicFixerSubsystem = mosaicFixerSubsystem;
        this.isFinished           = false;
    }

    @Override public void execute() {
        mosaicFixerSubsystem.disableLeftMosaicFixer();
        isFinished = true;
    }

    @Override public boolean isFinished() {
       return isFinished;
    }

}
