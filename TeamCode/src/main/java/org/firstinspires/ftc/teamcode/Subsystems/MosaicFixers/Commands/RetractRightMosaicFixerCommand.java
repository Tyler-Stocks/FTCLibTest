package org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerSubsystem;

public class RetractRightMosaicFixerCommand extends CommandBase {
    private final MosaicFixerSubsystem mosaicFixerSubsystem;

    private boolean isFinished = false;

    public RetractRightMosaicFixerCommand(@NonNull MosaicFixerSubsystem mosaicFixerSubsystem) {
        this.mosaicFixerSubsystem = mosaicFixerSubsystem;
    }

    @Override public void execute() {
        mosaicFixerSubsystem.enableRightMosaicFixer();
        mosaicFixerSubsystem.retractMosaicFixerRight();
        isFinished = true;
    }

    @Override public boolean isFinished() {
        return isFinished;
    }
}
