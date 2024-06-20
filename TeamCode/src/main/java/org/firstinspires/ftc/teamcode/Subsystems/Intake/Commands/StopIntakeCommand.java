package org.firstinspires.ftc.teamcode.Subsystems.Intake.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.IntakeSubsystem;

public class StopIntakeCommand extends CommandBase {
    private final IntakeSubsystem intakeSubsystem;
    private final ArmSubsystem armSubsystem;

    private boolean isFinished;

    public StopIntakeCommand(
            @NonNull IntakeSubsystem intakeSubsystem,
            @NonNull ArmSubsystem armSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        this.armSubsystem    = armSubsystem;

        isFinished = false;

        addRequirements(intakeSubsystem);
    }

    @Override public void execute() {
       intakeSubsystem.stop();
       armSubsystem.closeOuttake();
       isFinished = true;
    }

    @Override public boolean isFinished() {
        return isFinished;
    }
}
