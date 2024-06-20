package org.firstinspires.ftc.teamcode.Subsystems.MosaicFixers;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import static com.qualcomm.robotcore.hardware.Servo.Direction.*;

import static org.firstinspires.ftc.teamcode.Constants.Constants.MosaicFixerConstants.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * <h1>Mosaic Fixer Subsystem</h1>
 * <br>
 * <p>
 *     Subsystem to encapsulate the mosaic fixer servos. Contains the following hardware
 *     <ul>
 *         <li>Left Mosaic Fixer Servo</li>
 *         <li>Right Mosaic Fixer Servo</li>
 *     </ul>
 * </p>
 */
public class MosaicFixerSubsystem extends SubsystemBase {
    private final ServoImplEx leftMosaicFixerServo,
                              rightMosaicFixerServo;

    private final Telemetry telemetry;


    /**
     * Constructs a new MosaicFixerSubsystem
     * @param opMode The opMode you are running ; To obtain the hardwareMap and telemetry objects
     */
    public MosaicFixerSubsystem(@NonNull OpMode opMode) {
        telemetry = opMode.telemetry;

        leftMosaicFixerServo
                = opMode.hardwareMap.get(ServoImplEx.class, MOSAIC_FIXER_SERVO_LEFT_NAME);
        rightMosaicFixerServo
                = opMode.hardwareMap.get(ServoImplEx.class, MOSAIC_FIXER_SERVO_RIGHT_NAME);

        leftMosaicFixerServo.setDirection(REVERSE);

        retractMosaicFixerLeft();
        retractMosaicFixerRight();
    }


    /**
     * Disables the left mosaic fixer
     */
    public void disableLeftMosaicFixer() {
        leftMosaicFixerServo.setPwmDisable();
    }

    /**
     * Disables the right mosaic fixer
     */
    public void disableRightMosaicFixer() {
        rightMosaicFixerServo.setPwmDisable();
    }

    /**
     * Enables the left mosaic fixer
     */
    public void enableLeftMosaicFixer() {
        leftMosaicFixerServo.setPwmEnable();
    }

    /**
     * Enables the right mosaic fixer
     */
    public void enableRightMosaicFixer() {
        rightMosaicFixerServo.setPwmEnable();
    }

    /**
     * Moves the left mosaic fixer to the low position
     */
    public void moveLeftMosaicFixerToLowPosition() {
        enableLeftMosaicFixer();
        leftMosaicFixerServo.setPosition(MOSAIC_FIXER_LEFT_LOW_POSITION);
    }

    /**
     * Moves the left mosaic fixer to the medium position
     */
    public void moveLeftMosaicFixerToMediumPosition() {
        enableLeftMosaicFixer();
        leftMosaicFixerServo.setPosition(MOSAIC_FIXER_LEFT_MEDIUM_POSITION);
    }

    /**
     * Moves the left mosaic fixer to the high position
     */
    public void moveLeftMosaicFixerToHighPosition() {
        enableLeftMosaicFixer();
        leftMosaicFixerServo.setPosition(MOSAIC_FIXER_LEFT_HIGH_POSITION);
    }

    /**
     * Moves the right mosaic fixer to the low position
     */
    public void moveRightMosaicFixerToLowPosition() {
        enableRightMosaicFixer();
        rightMosaicFixerServo.setPosition(MOSAIC_FIXER_RIGHT_LOW_POSITION);
    }

    /**
     * Moves the right mosaic fixer to the high position
     */
    public void moveRightMosaicFixerToHighPosition() {
        enableRightMosaicFixer();
        rightMosaicFixerServo.setPosition(MOSAIC_FIXER_RIGHT_HIGH_POSITION);
    }

    /**
     * Retracts the left mosaic fixer
     */
    public void retractMosaicFixerLeft() {
        enableLeftMosaicFixer();
        leftMosaicFixerServo.setPosition(MOSAIC_FIXER_LEFT_HOME_POSITION);
    }

    /**
     * Retracts the right mosaic fixer
     */
    public void retractMosaicFixerRight() {
        enableRightMosaicFixer();
        rightMosaicFixerServo.setPosition(MOSAIC_FIXER_RIGHT_HOME_POSITION);
    }

    /**
     * Displays debug information about the mosaic fixers
     */
    public void debugMosaicFixers() {
        telemetry.addLine("----- Mosaic Fixer Debug -----");
        telemetry.addData("Left Mosaic Fixer Position", leftMosaicFixerServo.getPosition());
        telemetry.addData("Right Mosaic Fixer Position", rightMosaicFixerServo.getPosition());
        telemetry.addData("Left Mosaic Fixer Direction", leftMosaicFixerServo.getDirection());
        telemetry.addData("Right Mosaic Fixer Direction", rightMosaicFixerServo.getDirection());
        telemetry.addData("Left Mosaic Fixer PWM Enabled", leftMosaicFixerServo.isPwmEnabled());
        telemetry.addData("Right Mosaic Fixer PWM Enabled", rightMosaicFixerServo.isPwmEnabled());
    }
}
