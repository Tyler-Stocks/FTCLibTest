package org.firstinspires.ftc.teamcode.OpModes.Test;

import static org.firstinspires.ftc.teamcode.Constants.Constants.IntakeConstants.INTAKE_POWER;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.ConstantsLoader;

import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.WORM_SAFETY_VOLTAGE;

import java.io.IOException;

@TeleOp(name = "Test - Constants Reader", group = "Test")
public class ConstantsReaderTest extends LinearOpMode {

    @Override public void runOpMode() {
       ConstantsLoader constantsLoader = new ConstantsLoader();

       waitForStart();

       try {
           constantsLoader.loadConstants();
           telemetry.addData("INTAKE_POWER", INTAKE_POWER);
           telemetry.addData("WORM_SAFETY_VOLTAGE", WORM_SAFETY_VOLTAGE);
       } catch (IOException exception) {
           telemetry.addData("Failed to load constants", exception.getMessage());
       }

       telemetry.update();

       sleep(1000);

       terminateOpModeNow();
    }
}
