package org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerSubsystem;

public class DisableRightMosaicFixer extends CommandBase {
    private final MosaicFixerSubsystem mosaicFixerSubsystem;

    private boolean isFinished;

    public DisableRightMosaicFixer(@NonNull MosaicFixerSubsystem mosaicFixerSubsystem) {
        this.mosaicFixerSubsystem = mosaicFixerSubsystem;
        this.isFinished           = false;
    }

    @Override public void execute() {
        mosaicFixerSubsystem.disableRightMosaicFixer();
        isFinished = true;
    }

    @Override public boolean isFinished() {
        return isFinished;
    }
}
