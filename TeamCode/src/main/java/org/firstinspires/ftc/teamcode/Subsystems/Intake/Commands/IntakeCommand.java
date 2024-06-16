package org.firstinspires.ftc.teamcode.Subsystems.Intake.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.IntakeSubsystem;

/**
 * Command to spin the intake in the intake direction. Aside from spinning the intake, the command
 * also triggers the opening of the outtake
 */
public class IntakeCommand extends CommandBase {
    private final IntakeSubsystem intakeSubsystem;
    private final ArmSubsystem armSubsystem;

    public IntakeCommand(
            @NonNull IntakeSubsystem intakeSubsystem,
            @NonNull ArmSubsystem armSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        this.armSubsystem    = armSubsystem;

        addRequirements(intakeSubsystem, armSubsystem);
    }

    @Override public void execute() {
       armSubsystem.openOuttake();
       intakeSubsystem.intake();
    }
}
