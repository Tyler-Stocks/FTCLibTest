package org.firstinspires.ftc.teamcode.Subsystems.LEDController;

import static com.qualcomm.robotcore.hardware.DigitalChannel.Mode.OUTPUT;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelImpl;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;

import static org.firstinspires.ftc.teamcode.Subsystems.LEDController.LEDState.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * <h1>LED Subsystem</h1>
 * <br>
 * <p>
 *     Subsystem to encapsulate the four digital channels that control the LED's on the robot.
 *     Contains the following hardware
 *     <ul>
 *         <li>Red Channel Left</li>
 *         <li>Green Channel Left</li>
 *         <li>Red Channel Right</li>
 *         <li>Green Channel Right</li>
 *     </ul>
 * </p>
 */
public class LEDSubsystem extends SubsystemBase {
    private final DigitalChannelImpl leftLEDRedChannel,
                                     leftLEDGreenChannel,
                                     rightLEDRedChannel,
                                     rightLEDGreenChannel;

    private final Telemetry telemetry;

    private LEDState leftLEDState,
                     rightLEDState;

    /**
     * Constructs a new LEDSubsystem
     * @param opMode The opMode you are running ; To obtain the hardwareMap and telemetry
     */
    public LEDSubsystem(@NonNull OpMode opMode) {
        telemetry = opMode.telemetry;

        leftLEDRedChannel
                = opMode.hardwareMap.get(DigitalChannelImpl.class, "leftLEDRedChannel");
        leftLEDGreenChannel
                = opMode.hardwareMap.get(DigitalChannelImpl.class, "leftLEDGreenChannel");
        rightLEDRedChannel
                = opMode.hardwareMap.get(DigitalChannelImpl.class, "rightLEDRedChannel");
        rightLEDGreenChannel
                = opMode.hardwareMap.get(DigitalChannelImpl.class, "rightLEDGreenChannel");

        leftLEDRedChannel.setMode(OUTPUT);
        leftLEDGreenChannel.setMode(OUTPUT);
        rightLEDRedChannel.setMode(OUTPUT);
        rightLEDGreenChannel.setMode(OUTPUT);

        leftLEDRedChannel.setState(false);
        leftLEDGreenChannel.setState(false);
        rightLEDRedChannel.setState(false);
        rightLEDGreenChannel.setState(false);

        leftLEDState  = OFF;
        rightLEDState = OFF;
    }

    @Override public void periodic() {
        switch (leftLEDState) {
            case RED:
                leftLEDRedChannel.setState(true);
                leftLEDGreenChannel.setState(false);
                break;
            case GREEN:
                leftLEDRedChannel.setState(false);
                leftLEDGreenChannel.setState(true);
                break;
            case AMBER:
                leftLEDRedChannel.setState(true);
                leftLEDGreenChannel.setState(true);
                break;
            case OFF:
                leftLEDRedChannel.setState(false);
                leftLEDGreenChannel.setState(false);
                break;
        }

        switch (rightLEDState) {
            case RED:
                rightLEDRedChannel.setState(true);
                rightLEDGreenChannel.setState(false);
                break;
            case GREEN:
                rightLEDRedChannel.setState(false);
                rightLEDGreenChannel.setState(true);
                break;
            case AMBER:
                rightLEDRedChannel.setState(true);
                rightLEDGreenChannel.setState(true);
                break;
            case OFF:
                rightLEDRedChannel.setState(false);
                rightLEDGreenChannel.setState(false);
                break;
        }
    }

    /**
     * Sets the left LED to output green
     */
    public void setLeftLEDGreen() {
        leftLEDState = GREEN;
    }

    /**
     * Sets the left LED to output red
     */
    public void setLeftLEDRed() {
        leftLEDState = RED;
    }

    /**
     * Sets the left LED to output amber
     */
    public void setLeftLEDAmber() {
        leftLEDState = AMBER;
    }

    /**
     * Turns the left LED off
     */
    public void turnLeftLEDOff() {
        leftLEDState = OFF;
    }

    /**
     * Sets the left LED to green
     */
    public void setRightLEDGreen() {
        rightLEDState = GREEN;
    }

    /**
     * Sets the right led to green
     */
    public void setRightLEDRed() {
        rightLEDState = RED;
    }

    public void setRightLEDAmber() {
        rightLEDState = AMBER;
    }

    /**
     * Turns the right LED off
     */
    public void setRightLEDOff() {
        rightLEDState = OFF;
    }

    /**
     * Displays debug information about the LED Subsystem
     */
    public void debug() {
        telemetry.addLine("----- LED Subsystem Debug -----");
        telemetry.addData("Left LED State", leftLEDState.toString());
        telemetry.addData("Right LED State", rightLEDState.toString());
        telemetry.addData("Left LED Green Channel State", leftLEDGreenChannel.getState());
        telemetry.addData("Left LED Red Channel State", leftLEDRedChannel.getState());
        telemetry.addData("Right LED Green Channel State", rightLEDGreenChannel.getState());
        telemetry.addData("Right LED Red Channel State", rightLEDRedChannel.getState());
    }
}
