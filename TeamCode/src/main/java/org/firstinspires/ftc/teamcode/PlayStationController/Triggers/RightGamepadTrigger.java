package org.firstinspires.ftc.teamcode.PlayStationController.Triggers;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import static com.arcrobotics.ftclib.gamepad.GamepadKeys.Trigger.*;

public class RightGamepadTrigger extends Trigger {
    private final GamepadEx gamepad;
    private final double valueToTrigger;

    /**
     * @param thresholdToTrigger The threshold of trigger depression to trigger the trigger
     * @param gamepad The gamepad to read from
     */
    public RightGamepadTrigger(double thresholdToTrigger, @NonNull GamepadEx gamepad) {
        this.gamepad        = gamepad;
        this.valueToTrigger = thresholdToTrigger;
    }

    @Override public boolean get() {
        return this.gamepad.getTrigger(RIGHT_TRIGGER) > valueToTrigger;
    }
}
