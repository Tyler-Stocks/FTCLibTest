package org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerPosition;
import org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers.MosaicFixerSubsystem;

public class MoveLeftMosaicFixerToPosition extends CommandBase {
    private final MosaicFixerSubsystem mosaicFixerSubsystem;
    private final MosaicFixerPosition mosaicFixerPosition;

    private boolean isFinished;

    /**
     * Constructs a new MoveLeftMosaicFixerToPosition command
     * @param mosaicFixerSubsystem The mosaic fixer subsystem to move
     * @param mosaicFixerPosition The position to move the fixer to
     */
    public MoveLeftMosaicFixerToPosition(
            @NonNull MosaicFixerSubsystem mosaicFixerSubsystem,
            @NonNull MosaicFixerPosition  mosaicFixerPosition) {
        this.mosaicFixerSubsystem = mosaicFixerSubsystem;
        this.mosaicFixerPosition  = mosaicFixerPosition;

        isFinished = false;
    }

    @Override public void execute() {
       switch (mosaicFixerPosition) {
           case RETRACTED:
               mosaicFixerSubsystem.retractMosaicFixerLeft();
               break;
           case LOW:
               mosaicFixerSubsystem.moveLeftMosaicFixerToLowPosition();
               break;
           case MEDIUM:
               mosaicFixerSubsystem.moveLeftMosaicFixerToMediumPosition();
               break;
           case HIGH:
               mosaicFixerSubsystem.moveLeftMosaicFixerToHighPosition();
               break;
       }

       isFinished = true;
    }

    @Override public boolean isFinished() {
        return isFinished;
    }
}
