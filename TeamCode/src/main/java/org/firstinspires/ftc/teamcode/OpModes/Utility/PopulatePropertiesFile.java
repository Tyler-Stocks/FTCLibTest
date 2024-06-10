package org.firstinspires.ftc.teamcode.OpModes.Utility;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Utility - Populate Properties File", group = "Utility")
@Disabled
public class PopulatePropertiesFile extends LinearOpMode {

   @Override public void runOpMode() {
        telemetry.addLine("Warning! On start this opmode will overwrite all values");
        telemetry.addLine("stored in constants file in onbot java (Constants.txt) with");
        telemetry.addLine("the values in Constants.java located in Android Studio. DO");
        telemetry.addLine("NOT run this opmode unless you are 120% certain you know");
        telemetry.addLine("what you are doing as there is NO WAY TO UNDO IT.");
        telemetry.update();

        waitForStart();

        requestOpModeStop();
   }

}
