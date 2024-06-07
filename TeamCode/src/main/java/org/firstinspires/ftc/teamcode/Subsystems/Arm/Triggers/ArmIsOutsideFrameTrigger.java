package org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;

public class ArmIsOutsideFrameTrigger extends Trigger {
    private final ArmSubsystem armSubsystem;

    public ArmIsOutsideFrameTrigger(@NonNull ArmSubsystem armSubsystem) {
        this.armSubsystem = armSubsystem;
    }

    @Override public boolean get() {
        return !this.armSubsystem.isWithinFrame();
    }
}
