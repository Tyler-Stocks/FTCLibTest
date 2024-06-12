package org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import static com.qualcomm.robotcore.hardware.Servo.Direction.*;

public class MosaicFixerSubsystem extends SubsystemBase {
    private final ServoImplEx leftMosaicFixerServo,
                              rightMosaicFixerServo;

    public MosaicFixerSubsystem(@NonNull HardwareMap hardwareMap) {
        leftMosaicFixerServo
                = hardwareMap.get(ServoImplEx.class, "leftMosaicFixerServo");
        rightMosaicFixerServo
                = hardwareMap.get(ServoImplEx.class, "rightMosaicFixerServo");

        leftMosaicFixerServo.setDirection(REVERSE);

        retractMosaicFixerLeft();
        retractMosaicFixerRight();
    }


    public void disableLeftMosaicFixer() {
        leftMosaicFixerServo.setPwmDisable();
    }

    public void disableRightMosaicFixer() {
        rightMosaicFixerServo.setPwmDisable();
    }

    public void enableLeftMosaicFixer() {
        leftMosaicFixerServo.setPwmEnable();
    }

    public void enableRightMosaicFixer() {
        rightMosaicFixerServo.setPwmEnable();
    }

    public void moveLeftMosaicFixerToLowPosition() {
        leftMosaicFixerServo.setPosition(0.62);
    }

    public void moveLeftMosaicFixerToMediumPosition() {
        leftMosaicFixerServo.setPosition(0.59);
    }

    public void moveLeftMosaicFixerToHighPosition() {
        leftMosaicFixerServo.setPosition(0.54);
    }

    public void moveRightMosaicFixerToLowPosition() {
        rightMosaicFixerServo.setPosition(0.61);
    }

    public void moveRightMosaicFixerToMediumPosition() {
        rightMosaicFixerServo.setPosition(0.55);
    }

    public void retractMosaicFixerLeft() {
        leftMosaicFixerServo.setPosition(0.0);
    }

    public void retractMosaicFixerRight() {
        rightMosaicFixerServo.setPosition(0.0);
    }
}
