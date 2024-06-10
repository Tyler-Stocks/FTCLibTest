package org.firstinspires.ftc.teamcode.OpModes.Test;

import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.INTAKE_POWER;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ConstantsLoader;

import static org.firstinspires.ftc.teamcode.Constants.ArmConstants.WORM_SAFETY_VOLTAGE;

import java.io.IOException;

@TeleOp(name = "Test - Constants Reader", group = "Test")
public class ConstantsReaderTest extends OpMode {

    @Override public void init() {
        ConstantsLoader constantsLoader = new ConstantsLoader(telemetry);

        try {
            constantsLoader.loadConstants();
            telemetry.addData("Intake Power", INTAKE_POWER);
            telemetry.addData("Worm Safety Voltage", WORM_SAFETY_VOLTAGE);
        } catch (IOException exception) {
            telemetry.addData("Failed to read from constants file", exception.getMessage());
        }

        telemetry.update();
    }

    @Override public void loop() {
        telemetry.addData("Intake Power", INTAKE_POWER);
    }
}
