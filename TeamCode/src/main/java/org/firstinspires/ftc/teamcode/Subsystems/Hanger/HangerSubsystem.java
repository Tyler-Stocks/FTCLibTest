package org.firstinspires.ftc.teamcode.Subsystems.Hanger;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.Constants.HangerConstants.*;

public class HangerSubsystem extends SubsystemBase {
    private final Servo hangerServo;

    public HangerSubsystem(@NonNull HardwareMap hardwareMap) {
        hangerServo = hardwareMap.get(Servo.class, "hangerServo");
    }

    public void releaseHanger() {
        hangerServo.setPosition(HANG_POSITION);
    }

    public void reset() {
        hangerServo.setPosition(ZERO_POSITION);
    }

    public void debug(@NonNull Telemetry telemetry) {
        telemetry.addLine("---- Hanger Debug ----");
        telemetry.addData("Position", hangerServo.getPosition());
        telemetry.addData("Direction", hangerServo.getDirection());
    }
}
