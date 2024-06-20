package org.firstinspires.ftc.teamcode.Subsystems.PurplePixelPlacer;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import static org.firstinspires.ftc.teamcode.Constants.Constants.PurplePixelPlacerConstants.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * <h1>Purple Pixel Placer Subsystem</h1>
 * <br>
 * <p>
 *     Subsystem to encapsulate the servo which controls the purple pixel placement. Contains the
 *     following hardware
 *     <ul>
 *         <li>Purple Pixel Placer Servo</li>
 *     </ul>
 * </p>
 */
public class PurplePixelPlacerSubsystem extends SubsystemBase {
    private final ServoImplEx purplePixelPlacerServo;

    private final Telemetry telemetry;

    /**
     * Constructs a new PurplePixelPlacerSubsystem
     * @param opMode The opMode you are running ; To obtain the hardwareMap and telemetry objects
     */
    public PurplePixelPlacerSubsystem(@NonNull OpMode opMode) {
       telemetry = opMode.telemetry;

        purplePixelPlacerServo
               = opMode.hardwareMap.get(ServoImplEx.class, PURPLE_PIXEL_PLACER_SERVO_NAME);

       moveToNeutralPosition();
    }

    /**
     * Moves the purple pixel placer to the neutral position
     */
    public void moveToNeutralPosition() {
        purplePixelPlacerServo.setPosition(PURPLE_PIXEL_PLACER_NEUTRAL_POSITION);
    }

    /**
     * Moves the purple pixel placer to the active position
     */
    public void moveToActivePosition() {
        purplePixelPlacerServo.setPosition(PURPLE_PIXEL_PLACER_ACTIVE_POSITION);
    }
}
