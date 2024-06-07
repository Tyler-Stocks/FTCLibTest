package org.firstinspires.ftc.teamcode.PlayStationController.Triggers;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import static com.arcrobotics.ftclib.gamepad.GamepadKeys.Trigger.*;

public class LeftGamepadTrigger extends Trigger {
    private final GamepadEx gamepad;
    private final double valueToTrigger;

    public LeftGamepadTrigger(double valueToTrigger, @NonNull GamepadEx gamepad) {
        this.gamepad        = gamepad;
        this.valueToTrigger = valueToTrigger;
    }

    @Override public boolean get() {
        return this.gamepad.getTrigger(LEFT_TRIGGER) > this.valueToTrigger;
    }

}
