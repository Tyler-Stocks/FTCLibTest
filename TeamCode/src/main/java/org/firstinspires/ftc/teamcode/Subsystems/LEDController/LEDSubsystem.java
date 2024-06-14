package org.firstinspires.ftc.teamcode.Subsystems.LEDController;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;

import static org.firstinspires.ftc.teamcode.Subsystems.LEDController.LEDState.*;

public class LEDSubsystem extends SubsystemBase {
    private final LED leftLEDRedChannel,
                      leftLEDGreenChannel,
                      rightLEDRedChannel,
                      rightLEDGreenChannel;

    private LEDState leftLEDState,
                     rightLEDState;


    public LEDSubsystem(@NonNull HardwareMap hardwareMap) {
        leftLEDRedChannel    = hardwareMap.get(LED.class, "leftLEDRedChannel");
        leftLEDGreenChannel  = hardwareMap.get(LED.class, "leftLEDGreenChannel");
        rightLEDRedChannel   = hardwareMap.get(LED.class, "rightLEDRedChannel");
        rightLEDGreenChannel = hardwareMap.get(LED.class, "rightLEDGreenChannel");

        leftLEDRedChannel.enable(false);
        leftLEDGreenChannel.enable(false);
        rightLEDRedChannel.enable(false);
        rightLEDGreenChannel.enable(false);

        leftLEDState  = OFF;
        rightLEDState = OFF;
    }

    @Override public void periodic() {
        switch (leftLEDState) {
            case RED:
                leftLEDRedChannel.enable(true);
                leftLEDGreenChannel.enable(false);
                break;
            case GREEN:
                leftLEDRedChannel.enable(false);
                leftLEDGreenChannel.enable(true);
                break;
            case AMBER:
                leftLEDRedChannel.enable(true);
                leftLEDGreenChannel.enable(true);
                break;
            case OFF:
                leftLEDRedChannel.enable(false);
                leftLEDGreenChannel.enable(false);
                break;
        }

        switch (rightLEDState) {
            case RED:
                rightLEDRedChannel.enable(true);
                rightLEDGreenChannel.enable(false);
                break;
            case GREEN:
                rightLEDRedChannel.enable(false);
                rightLEDGreenChannel.enable(true);
                break;
            case AMBER:
                rightLEDRedChannel.enable(true);
                rightLEDGreenChannel.enable(true);
                break;
            case OFF:
                rightLEDRedChannel.enable(false);
                rightLEDGreenChannel.enable(false);
                break;
        }
    }

    public void setLeftLEDGreen() {
        leftLEDState = GREEN;
    }

    public void setLeftLEDRed() {
        leftLEDState = RED;
    }

    public void turnLeftLEDOff() {
        leftLEDState = OFF;
    }

    public void setRightLEDGreen() {
        rightLEDState = GREEN;
    }

    public void setRightLEDRed() {
        rightLEDState = RED;
    }

    public void setRightLEDOff() {
        rightLEDState = OFF;
    }
}
