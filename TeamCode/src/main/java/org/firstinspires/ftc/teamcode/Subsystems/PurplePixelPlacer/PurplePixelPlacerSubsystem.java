package org.firstinspires.ftc.teamcode.Subsystems.PurplePixelPlacer;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import static org.firstinspires.ftc.teamcode.Constants.Constants.PurplePixelPlacerConstants.*;

public class PurplePixelPlacerSubsystem extends SubsystemBase {
    private final ServoImplEx purplePixelPlacerServo;

    public PurplePixelPlacerSubsystem(@NonNull HardwareMap hardwareMap) {
       purplePixelPlacerServo
               = hardwareMap.get(ServoImplEx.class, "purplePixelPlacerServo");

       moveToNeutralPosition();
    }

    public void moveToNeutralPosition() {
        purplePixelPlacerServo.setPosition(PURPLE_PIXEL_PLACER_NEUTRAL_POSITION);
    }

    public void moveToActivePosition() {
        purplePixelPlacerServo.setPosition(PURPLE_PIXEL_PLACER_ACTIVE_POSITION);
    }
}
