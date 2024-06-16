package org.firstinspires.ftc.teamcode.Subsystems.Intake.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Intake.IntakeSubsystem;

public class OuttakeCommand extends CommandBase {
   private final IntakeSubsystem intakeSubsystem;
   private final ArmSubsystem armSubsystem;

    /**
     * Constructs a new outtake command
     * @param intakeSubsystem The intake subsystem to rely on
     * @param armSubsystem The arm subsystem to rely on
     */
   public OuttakeCommand(
           @NonNull IntakeSubsystem intakeSubsystem,
           @NonNull ArmSubsystem armSubsystem) {
       this.intakeSubsystem = intakeSubsystem;
       this.armSubsystem    = armSubsystem;

       addRequirements(intakeSubsystem, armSubsystem);
   }

   @Override public void execute() {
       armSubsystem.openOuttake();
       intakeSubsystem.outtake();
   }
}
