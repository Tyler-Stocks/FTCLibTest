package org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import static com.qualcomm.robotcore.hardware.Servo.Direction.*;

import static org.firstinspires.ftc.teamcode.Constants.Constants.MosaicFixerConstants.*;

public class MosaicFixerSubsystem extends SubsystemBase {
    private final ServoImplEx leftMosaicFixerServo,
                              rightMosaicFixerServo;

    public MosaicFixerSubsystem(@NonNull HardwareMap hardwareMap) {
        leftMosaicFixerServo
                = hardwareMap.get(ServoImplEx.class, MOSAIC_FIXER_SERVO_LEFT_NAME);
        rightMosaicFixerServo
                = hardwareMap.get(ServoImplEx.class, MOSAIC_FIXER_SERVO_RIGHT_NAME);

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
        enableLeftMosaicFixer();
        leftMosaicFixerServo.setPosition(MOSAIC_FIXER_LEFT_LOW_POSITION);
    }

    public void moveLeftMosaicFixerToMediumPosition() {
        enableLeftMosaicFixer();
        leftMosaicFixerServo.setPosition(MOSAIC_FIXER_LEFT_MEDIUM_POSITION);
    }

    public void moveLeftMosaicFixerToHighPosition() {
        enableLeftMosaicFixer();
        leftMosaicFixerServo.setPosition(MOSAIC_FIXER_LEFT_HIGH_POSITION);
    }

    public void moveRightMosaicFixerToLowPosition() {
        enableRightMosaicFixer();
        rightMosaicFixerServo.setPosition(MOSAIC_FIXER_RIGHT_LOW_POSITION);
    }

    public void moveRightMosaicFixerToHighPosition() {
        enableRightMosaicFixer();
        rightMosaicFixerServo.setPosition(MOSAIC_FIXER_RIGHT_HIGH_POSITION);
    }

    public void retractMosaicFixerLeft() {
        enableLeftMosaicFixer();
        leftMosaicFixerServo.setPosition(MOSAIC_FIXER_LEFT_HOME_POSITION);
    }

    public void retractMosaicFixerRight() {
        enableRightMosaicFixer();
        rightMosaicFixerServo.setPosition(MOSAIC_FIXER_RIGHT_HOME_POSITION);
    }
}
