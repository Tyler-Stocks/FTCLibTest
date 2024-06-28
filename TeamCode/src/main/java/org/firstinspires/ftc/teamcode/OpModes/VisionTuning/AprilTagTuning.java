package org.firstinspires.ftc.teamcode.OpModes.VisionTuning;

import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import static org.firstinspires.ftc.teamcode.Constants.Constants.AprilTagConstants.*;
import static org.firstinspires.ftc.teamcode.PlayStationController.PlayStationController.OPTIONS;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.annotation.SuppressLint;
import android.util.Size;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.List;

@TeleOp(name = "Utility - April Tag Tuning", group = "Utility")
public class AprilTagTuning extends CommandOpMode {
    private ArrayList<String> allAprilTagDetections;

    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;

    private String aprilTagLogFileName;

    private ElapsedTime elapsedTime;

    private final int DESIRED_TAG_ID = 2;

    @Override public void initialize() {
        initAprilTagDetection();
        configureBindings();

        elapsedTime = new ElapsedTime();

        schedule(
                new RunCommand(this::detectAprilTags),
                new RunCommand(telemetry::update)
        );
    }

    private void configureBindings() {
        GamepadEx driverGamepad   = new GamepadEx(gamepad1);
        GamepadEx operatorGamepad = new GamepadEx(gamepad2);

        new GamepadButton(driverGamepad, OPTIONS)
                .whenPressed(this::saveAprilTagData);

        new GamepadButton(operatorGamepad, OPTIONS)
                .whenPressed(this::saveAprilTagData);
    }

    private void initAprilTagDetection() {
        aprilTagProcessor = new AprilTagProcessor.Builder()
            .setDrawTagOutline(true)
            .setLensIntrinsics(660.750, 660.75, 323.034, 230.681)
            .build();

        aprilTagProcessor.setDecimation(DECIMATION);

        visionPortal = new VisionPortal.Builder()
            .addProcessor(aprilTagProcessor)
            .setCamera(hardwareMap.get(WebcamName.class, "Webcam 2"))
            .setCameraResolution(new Size(640, 480))
            .setAutoStopLiveView(true)
            .build();

        setCameraProperties();

        allAprilTagDetections = new ArrayList<String>();

        aprilTagLogFileName = createAprilTagLogFileName();
    }

    private void setCameraProperties() {
        setExposure();
        setGain();
        setWhiteBalanceTemperature();
    }

    private void setExposure() {
        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);

        exposureControl.setMode(ExposureControl.Mode.Manual);

        int minExposure = (int) exposureControl.getMinExposure(MILLISECONDS);
        int maxExposure = (int) exposureControl.getMaxExposure(MILLISECONDS);

        if (EXPOSURE_MS < minExposure + 1) {
            exposureControl.setExposure(minExposure + 1, MILLISECONDS);
        } else if (EXPOSURE_MS > maxExposure) {
            exposureControl.setExposure(maxExposure, MILLISECONDS);
        } else {
            exposureControl.setExposure(EXPOSURE_MS, MILLISECONDS);
        }
    }

    private void setGain() {
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);

        int minGain = gainControl.getMinGain();
        int maxGain = gainControl.getMaxGain();

        if (GAIN > maxGain) {
            gainControl.setGain(maxGain);
        } else if (GAIN < minGain) {
            gainControl.setGain(minGain);
        } else {
            gainControl.setGain(GAIN);
        }
    }

    private void setWhiteBalanceTemperature() {
        WhiteBalanceControl whiteBalanceControl
                = visionPortal.getCameraControl(WhiteBalanceControl.class);

        whiteBalanceControl.setMode(WhiteBalanceControl.Mode.MANUAL);

        int minWhiteBalanceTemperature = whiteBalanceControl.getMinWhiteBalanceTemperature();
        int maxWhiteBalanceTemperature = whiteBalanceControl.getMaxWhiteBalanceTemperature();

        if (WHITE_BALANCE_TEMPERATURE > maxWhiteBalanceTemperature) {
            whiteBalanceControl.setWhiteBalanceTemperature(maxWhiteBalanceTemperature);
        } else if (WHITE_BALANCE_TEMPERATURE < minWhiteBalanceTemperature) {
            whiteBalanceControl.setWhiteBalanceTemperature(minWhiteBalanceTemperature);
        } else {
            whiteBalanceControl.setWhiteBalanceTemperature(WHITE_BALANCE_TEMPERATURE);
        }
    }

    @NonNull private String createAprilTagLogFileName() {
        String currentDate =
                new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(new Date());
        String currentTime =
                new SimpleDateFormat("HH-mm-ss", Locale.CANADA).format(new Date());

        return "April-Tag-Log-" + currentDate + "-" + currentTime + ".txt";
    }

    private void detectAprilTags() {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();

        int numberOfTags = currentDetections.size();

        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata == null) continue;

            if (detection.id != DESIRED_TAG_ID) continue;

            double yaw     = detection.ftcPose.yaw;
            double pitch   = detection.ftcPose.pitch;
            double roll    = detection.ftcPose.roll;
            double range   = detection.ftcPose.range;
            double bearing = detection.ftcPose.bearing;
            double x       = detection.ftcPose.x;
            double y       = detection.ftcPose.y;
            double z       = detection.ftcPose.z;

            double time = elapsedTime.seconds();

            String line = time    + ","
                        + yaw     + ","
                        + pitch   + ","
                        + roll    + ","
                        + range   + ","
                        + bearing + ","
                        + x       + ","
                        + y       + ","
                        + z       + ",";

            allAprilTagDetections.add(line);

            telemetry.addData("Yaw %.3f", yaw);
            telemetry.addData("Pitch %.3f", pitch);
            telemetry.addData("Roll %.3f", roll);
            telemetry.addData("Range %.3f", range);
            telemetry.addData("Bearing %.3f", bearing);
            telemetry.addData("X %.3f", x);
            telemetry.addData("Y %.3f", y);
            telemetry.addData("Z %.3f", z);
        }
    }

    private void saveAprilTagData() {
        @SuppressLint("sdCardPath")
        String sdCardPath = "/sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/";

        File aprilTagLogFile = new File(sdCardPath + aprilTagLogFileName);

        try {
            FileWriter fileWriter         = new FileWriter(aprilTagLogFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // ----- Write Header ----- //

            bufferedWriter.write("Exposure=" + EXPOSURE_MS);
            bufferedWriter.write("Gain=" + GAIN);
            bufferedWriter.write("WhiteBalance=" + WHITE_BALANCE_TEMPERATURE);
            bufferedWriter.write("Decimation=" + DECIMATION);

            bufferedWriter.newLine();

            bufferedWriter.write("time,yaw,pitch,roll,range,bearing,x,y,z");

            bufferedWriter.newLine();

            for (String detection: allAprilTagDetections) {
                bufferedWriter.write(detection);
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ioException) {
            telemetry.addData("Failed to write to file", ioException.getMessage());
        }

        allAprilTagDetections.clear();
    }
}

//@TeleOp(name = "Utility - April Tag Tuning", group = "Utility")
//public class AprilTagTuning extends OpMode {
//    private ArrayList<String> allAprilTagDetections;
//
//    private AprilTagProcessor aprilTagProcessor;
//
//    private static final int DESIRED_TAG_ID = 5; // -1 locks on to any tag
//
//    private VisionPortal visionPortal;
//
//    private int minExposure,
//            maxExposure,
//            exposure;
//
//    private int minGain,
//            gain,
//            maxGain;
//
//    private int minWhiteBalance,
//            whiteBalance,
//            maxWhiteBalance;
//
//    private float decimation = 3f;
//
//    private ElapsedTime elapsedTime;
//
//    private String aprilTagLogFileName;
//
//    @Override public void init() {
//        initAprilTagDetection();
//        getDefaultCameraSettings();
//        setCameraProperties(exposure, gain, whiteBalance);
//
//        aprilTagLogFileName = createAprilTagLogFileName();
//
//        elapsedTime = new ElapsedTime();
//        elapsedTime.reset();
//
//        allAprilTagDetections = new ArrayList<>();
//
//
//    }
//
//    @Override public void loop() {
//        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
//
//        int numberOfTags = currentDetections.size();
//
//        for (AprilTagDetection detection : currentDetections) {
//            if (detection.metadata == null) continue;
//
//            if ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID)) {
//                String line
//                        = elapsedTime.seconds()     + ","
//                        + detection.ftcPose.yaw     + ","
//                        + detection.ftcPose.pitch   + ","
//                        + detection.ftcPose.roll    + ","
//                        + detection.ftcPose.range   + ","
//                        + detection.ftcPose.bearing + ",";
//
//                allAprilTagDetections.add(line);
//            }
//        }
//
//        if (gamepad1.options || gamepad2.options) {
//            saveAprilTagData();
//        }
//
//        setCameraProperties(exposure, gain, whiteBalance);
//
//        telemetry.update();
//    }
//
//    private void initAprilTagDetection() {
//        aprilTagProcessor = new AprilTagProcessor.Builder()
//                .setDrawTagOutline(true)
//                .setLensIntrinsics(660.750, 660.75, 323.034, 230.681) // C615 measured kk Dec 5 2023
//                .build();
//
//        aprilTagProcessor.setDecimation(decimation);
//
//        visionPortal = new VisionPortal.Builder()
//                .addProcessor(aprilTagProcessor)
//                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 2"))
//                .setCameraResolution(new Size(640, 480))
//                .setAutoStopLiveView(true)
//                .build();
//    }
//
//    private void getDefaultCameraSettings() {
//        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
//            telemetry.addLine("Waiting for camera");
//            telemetry.update();
//        }
//
//        telemetry.addLine("Camera Ready");
//        telemetry.update();
//
//        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
//
//        minExposure = (int) exposureControl.getMinExposure(TimeUnit.MILLISECONDS) + 1;
//        maxExposure = (int) exposureControl.getMaxExposure(TimeUnit.MILLISECONDS);
//        exposure    = (int) exposureControl.getExposure(TimeUnit.MILLISECONDS);
//
//        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
//
//        minGain = gainControl.getMinGain();
//        maxGain = gainControl.getMaxGain();
//        gain    = gainControl.getGain();
//
//        WhiteBalanceControl whiteBalanceControl = visionPortal.getCameraControl(WhiteBalanceControl.class);
//
//        minWhiteBalance = whiteBalanceControl.getMinWhiteBalanceTemperature();
//        maxWhiteBalance = whiteBalanceControl.getMaxWhiteBalanceTemperature();
//        whiteBalance    = whiteBalanceControl.getWhiteBalanceTemperature();
//    }
//
//    private void setCameraProperties(int exposureMS, int gain, int white) {
//        // Wait for camera to start streaming
//        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
//            telemetry.addLine("Waiting for camera");
//            telemetry.update();
//        }
//
//        // Make sure that the telemetry clears properly after we exit the while loop
//        telemetry.update();
//
//        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
//
//        exposureControl.setMode(ExposureControl.Mode.Manual);
//        exposureControl.setExposure(exposureMS, TimeUnit.MILLISECONDS);
//
//        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
//        gainControl.setGain(gain);
//
//        WhiteBalanceControl whiteBalanceControl = visionPortal.getCameraControl(WhiteBalanceControl.class);
//
//        whiteBalanceControl.setMode(WhiteBalanceControl.Mode.MANUAL);
//        whiteBalanceControl.setWhiteBalanceTemperature(white);
//    }
//
//    @NonNull
//    private String createAprilTagLogFileName() {
//        String currentDate =
//                new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(new Date());
//        String currentTime =
//                new SimpleDateFormat("HH-mm-ss", Locale.CANADA).format(new Date());
//
//        return "April-Tag-Log-" + currentDate + "-" + currentTime + ".txt";
//    }
//
//    private void saveAprilTagData() {
//        String sdCardPath = "/sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/";
//
//        File aprilTagLogFile = new File(sdCardPath + aprilTagLogFileName);
//
//        try {
//            FileWriter fileWriter         = new FileWriter(aprilTagLogFile, true);
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//
//            // ----- Write Header ----- //
//
//            bufferedWriter.write("Exposure=" + exposure);
//            bufferedWriter.write("Gain=" + gain);
//            bufferedWriter.write("WhiteBalance=" + whiteBalance);
//            bufferedWriter.write("Decimation=" + decimation);
//
//            bufferedWriter.newLine();
//
//            bufferedWriter.write("t,y,p,r,d,b");
//
//            bufferedWriter.newLine();
//
//            for (String detection: allAprilTagDetections) {
//                bufferedWriter.write(detection);
//                bufferedWriter.newLine();
//            }
//
//            bufferedWriter.flush();
//            bufferedWriter.close();
//        } catch (IOException ioException) {
//            telemetry.addData("Failed to write to file", ioException.getMessage());
//        }
//
//        allAprilTagDetections.clear();
//    }
//}
