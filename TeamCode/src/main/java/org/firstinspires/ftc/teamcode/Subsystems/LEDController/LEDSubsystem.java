package org.firstinspires.ftc.teamcode.Subsystems.LEDController;

import static com.qualcomm.robotcore.hardware.DigitalChannel.Mode.OUTPUT;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelImpl;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;

import static org.firstinspires.ftc.teamcode.Subsystems.LEDController.LEDState.*;

public class LEDSubsystem extends SubsystemBase {
    private final DigitalChannelImpl leftLEDRedChannel,
                                     leftLEDGreenChannel,
                                     rightLEDRedChannel,
                                     rightLEDGreenChannel;

    private LEDState leftLEDState,
                     rightLEDState;


    public LEDSubsystem(@NonNull HardwareMap hardwareMap) {
        leftLEDRedChannel    = hardwareMap.get(DigitalChannelImpl.class, "leftLEDRedChannel");
        leftLEDGreenChannel  = hardwareMap.get(DigitalChannelImpl.class, "leftLEDGreenChannel");
        rightLEDRedChannel   = hardwareMap.get(DigitalChannelImpl.class, "rightLEDRedChannel");
        rightLEDGreenChannel = hardwareMap.get(DigitalChannelImpl.class, "rightLEDGreenChannel");

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
}
