package org.firstinspires.ftc.teamcode.Subsystems.Launcher.Commands;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Launcher.LauncherSubsystem;

public class DebugLauncherCommand extends CommandBase {
    private final LauncherSubsystem launcherSubsystem;
    private final Telemetry telemetry;

    public DebugLauncherCommand(@NonNull LauncherSubsystem launcherSubsystem, @NonNull Telemetry telemetry) {
        this.launcherSubsystem = launcherSubsystem;
        this.telemetry         = telemetry;
    }

    @Override public void execute() {
        this.launcherSubsystem.debug(this.telemetry);
    }
}
