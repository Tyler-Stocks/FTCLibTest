package org.firstinspires.ftc.teamcode.Subsystems.Arm.Triggers;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;

public class WormIsAtPositionTrigger extends Trigger {
    private final ArmSubsystem armSubsystem;
    private final int wormPosition;

    /**
     * Constructs a new WormIsAtPositionTrigger
     * @param armSubsystem The arm subsystem to read from
     * @param wormPosition The worm position to listen for
     */
    public WormIsAtPositionTrigger(@NonNull ArmSubsystem armSubsystem, int wormPosition) {
        this.armSubsystem = armSubsystem;
        this.wormPosition = wormPosition;
    }

    @Override public boolean get() {
        return armSubsystem.wormPosition() == wormPosition;
    }
}
