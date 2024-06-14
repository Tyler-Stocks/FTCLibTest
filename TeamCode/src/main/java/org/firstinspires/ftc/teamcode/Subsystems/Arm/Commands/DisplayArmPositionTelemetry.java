package org.firstinspires.ftc.teamcode.Subsystems.Arm.Commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmSubsystem;

public class DisplayArmPositionTelemetry extends CommandBase {
    private final ArmSubsystem armSubsystem;
    private final Telemetry telemetry;

    public DisplayArmPositionTelemetry(ArmSubsystem armSubsystem, Telemetry telemetry) {
       this.armSubsystem = armSubsystem;
       this.telemetry    = telemetry;
    }

    @Override public void execute() {
       telemetry.addData(
               "Worm Position",
               armSubsystem.wormPosition());
       telemetry.addData(
               "Elevator Position",
               armSubsystem.elevatorPosition());
       telemetry.addData(
               "Worm Motor Target Position",
               armSubsystem.wormTargetPosition());
       telemetry.addData(
               "Elevator Motor Target Position",
               armSubsystem.elevatorTargetPosition());
       telemetry.addData(
               "Worm Motor Target Position (Local)",
               armSubsystem.localWormTargetPosition());
       telemetry.addData(
              "Elevator Motor Target Position (Local)",
              armSubsystem.localElevatorTargetPosition());
       telemetry.update();
    }
}
