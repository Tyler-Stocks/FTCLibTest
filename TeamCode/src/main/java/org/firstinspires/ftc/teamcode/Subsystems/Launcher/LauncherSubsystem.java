package org.firstinspires.ftc.teamcode.Subsystems.Launcher;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import static com.qualcomm.robotcore.hardware.Servo.Direction.*;

import static org.firstinspires.ftc.teamcode.Constants.Constants.LauncherConstants.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LauncherSubsystem extends SubsystemBase {
    private final Servo launcherServo;

    public LauncherSubsystem(@NonNull HardwareMap hardwareMap) {
        launcherServo = hardwareMap.get(Servo.class, LAUNCHER_SERVO_NAME);
        launcherServo.setDirection(REVERSE);
    }

    public void launch() {
        launcherServo.setPosition(LAUNCH_POSITION);
    }

    public void reset() { launcherServo.setPosition(ZERO_POSITION); }

    public void debug(@NonNull Telemetry telemetry) {
        telemetry.addLine("---- Launcher Debug ----");
        telemetry.addData("Servo Position", launcherServo.getPosition());
        telemetry.addData("Servo Direction", launcherServo.getDirection());
    }
}
