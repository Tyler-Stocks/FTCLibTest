package org.firstinspires.ftc.teamcode.Subsystems.Intake.Triggers;

import com.arcrobotics.ftclib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.Subsystems.Intake.IntakeSubsystem;

public class FrontBeamBreakTrigger extends Trigger {
    private final IntakeSubsystem intakeSubsystem;

    public FrontBeamBreakTrigger(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
    }

    @Override public boolean get() {
        return intakeSubsystem.frontBeamBreakIsPressed();
    }
}
