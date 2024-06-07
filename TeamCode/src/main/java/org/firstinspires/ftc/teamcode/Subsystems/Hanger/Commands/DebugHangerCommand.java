package org.firstinspires.ftc.teamcode.Subsystems.Hanger.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger.HangerSubsystem;

public class DebugHangerCommand extends CommandBase {
    private final HangerSubsystem hangerSubsystem;
    private final Telemetry telemetry;


    public DebugHangerCommand(@NonNull HangerSubsystem hangerSubsystem, @NonNull Telemetry telemetry) {
        this.hangerSubsystem = hangerSubsystem;
        this.telemetry       = telemetry;
    }

    @Override public void execute() {
        this.hangerSubsystem.debug(this.telemetry);
    }
}
