package org.firstinspires.ftc.teamcode.Subsystems.Arm.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;

public class SetArmTargetPositionCommand extends CommandBase {
    private final ArmSubsystem armSubsystem;
    private final int wormPosition;
    private final int elevatorPosition;

    private boolean isFinished = false;

    public SetArmTargetPositionCommand(@NonNull ArmSubsystem armSubsystem, int wormPosition, int elevatorPosition) {
       this.armSubsystem     = armSubsystem;
       this.wormPosition     = wormPosition;
       this.elevatorPosition = elevatorPosition;
    }

    @Override public void execute() {
        this.armSubsystem.setTargetPos(this.elevatorPosition, this.wormPosition);
        isFinished = true;
    }

    @Override public boolean isFinished() {
        return this.isFinished;
    }
}
