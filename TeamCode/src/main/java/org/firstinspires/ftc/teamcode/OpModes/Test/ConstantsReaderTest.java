package org.firstinspires.ftc.teamcode.OpModes.Test;

import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.INTAKE_POWER;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;

import java.io.IOException;

@TeleOp(name = "Test - Constants Reader", group = "Test")
public class ConstantsReaderTest extends OpMode {

    @Override public void init() {
        try {
            ConstantsLoader.load(telemetry);
            telemetry.addData("Intake Power", INTAKE_POWER);
        } catch (IOException exception) {
            telemetry.addData("Failed to read from constants file", exception.getMessage());
        }

        telemetry.update();
    }

    @Override public void loop() {
        telemetry.addData("Intake Power", INTAKE_POWER);
    }
}
