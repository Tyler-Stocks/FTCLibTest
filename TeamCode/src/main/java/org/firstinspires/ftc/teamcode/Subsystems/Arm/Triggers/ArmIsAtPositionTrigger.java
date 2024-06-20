package org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;

public class ArmIsAtPositionTrigger extends Trigger {
    private final ArmSubsystem armSubsystem;
    private final int elevatorPosition;
    private final int wormPosition;

    /**
     * Constructs a new ArmIsAtPosition trigger
     * @param armSubsystem The arm subsystem to monitor
     * @param wormPosition The worm position to monitor for
     * @param elevatorPosition The elevator position to monitor for
     */
    public ArmIsAtPositionTrigger(
            @NonNull ArmSubsystem armSubsystem,
            int wormPosition,
            int elevatorPosition) {
        this.armSubsystem     = armSubsystem;
        this.elevatorPosition = elevatorPosition;
        this.wormPosition     = wormPosition;
    }

    @Override public boolean get() {
        return armSubsystem.elevatorTargetPosition() == elevatorPosition
                && armSubsystem.wormTargetPosition() == wormPosition;
    }
}
