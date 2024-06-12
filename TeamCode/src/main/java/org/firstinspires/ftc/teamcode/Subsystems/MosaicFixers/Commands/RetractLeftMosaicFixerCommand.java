package org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerSubsystem;

public class RetractLeftMosaicFixerCommand extends CommandBase {
    private final MosaicFixerSubsystem mosaicFixerSubsystem;

    private boolean isFinished = false;

    public RetractLeftMosaicFixerCommand(@NonNull MosaicFixerSubsystem mosaicFixerSubsystem) {
        this.mosaicFixerSubsystem = mosaicFixerSubsystem;
    }

    @Override public void execute() {
        mosaicFixerSubsystem.enableLeftMosaicFixer();
        mosaicFixerSubsystem.retractMosaicFixerLeft();
        isFinished = true;
    }

    @Override public boolean isFinished() {
        return isFinished;
    }
}
