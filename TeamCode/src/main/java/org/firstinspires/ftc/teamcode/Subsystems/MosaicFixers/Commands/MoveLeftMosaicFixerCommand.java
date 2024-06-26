package org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerPosition;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerSubsystem;

public class MoveLeftMosaicFixerCommand extends CommandBase {
    private final MosaicFixerSubsystem mosaicFixerSubsystem;
    private final MosaicFixerPosition mosaicFixerPosition;

    private boolean isFinished;

    /**
     * Constructs a new MoveLeftMosaicFixerCommand
     * @param mosaicFixerSubsystem The mosaic fixer subsystem to use
     * @param mosaicFixerPosition The position to move the mosaic fixer to
     */
    public MoveLeftMosaicFixerCommand(
            @NonNull MosaicFixerSubsystem mosaicFixerSubsystem,
            @NonNull MosaicFixerPosition mosaicFixerPosition
    ) {
       this.mosaicFixerSubsystem = mosaicFixerSubsystem;
       this.mosaicFixerPosition  = mosaicFixerPosition;

       isFinished = false;
    }

    @Override public void execute() {
       mosaicFixerSubsystem.enableLeftMosaicFixer();
       mosaicFixerSubsystem.moveLeftMosaicFixerToPosition(mosaicFixerPosition);
       isFinished = true;
    }

    @Override public boolean isFinished() {
        return isFinished;
    }
}
