package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Utility.MotorUtility;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS;
import static org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit.AMPS;
import static org.firstinspires.ftc.teamcode.Constants.Constants.DrivebaseConstants.*;

public class DriveSubsystem extends SubsystemBase {
    private final DcMotorImplEx frontLeftMotor,
                                frontRightMotor,
                                backLeftMotor,
                                backRightMotor;

    private final IMU imu;

    @Nullable
    private Telemetry telemetry;

    public DriveSubsystem(@NonNull HardwareMap hardwareMap) {
        telemetry = null;

        frontLeftMotor  = hardwareMap.get(DcMotorImplEx.class, FRONT_LEFT_DRIVE_MOTOR_NAME);
        frontRightMotor = hardwareMap.get(DcMotorImplEx.class, FRONT_RIGHT_DRIVE_MOTOR_NAME);
        backLeftMotor   = hardwareMap.get(DcMotorImplEx.class, BACK_LEFT_DRIVE_MOTOR_NAME);
        backRightMotor  = hardwareMap.get(DcMotorImplEx.class, BACK_RIGHT_DRIVE_MOTOR_NAME);

        imu = hardwareMap.get(IMU.class, IMU_NAME);

        imu.initialize(IMU_PARAMETERS);

        frontLeftMotor.setDirection(FRONT_LEFT_DRIVE_MOTOR_DIRECTION);
        frontRightMotor.setDirection(FRONT_RIGHT_DRIVE_MOTOR_DIRECTION);
        backLeftMotor.setDirection(BACK_LEFT_DRIVE_MOTOR_DIRECTION);
        backRightMotor.setDirection(BACK_RIGHT_DRIVE_MOTOR_DIRECTION);

        MotorUtility.setMotorZeroPowerBehaviors(
                BRAKE, frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);
    }

    // ---------- IMU Functions ---------- //

    /**
     * Resets the IMU heading
      */
    public void resetIMU() {
        imu.resetYaw();
    }

    /**
     * @return The yaw of the robot
     */
    public double robotYaw() {
        return imu.getRobotYawPitchRollAngles().getYaw(RADIANS);
    }

    // ---------- Drive Functions ---------- //

    public void driveRobotCentric(double drive, double strafe, double turn) {
        if (drive < DRIVE_DEAD_ZONE)   drive  = 0.0;
        if (strafe < STRAFE_DEAD_ZONE) strafe = 0.0;
        if (turn < TURN_DEAD_ZONE)     turn   = 0.0;

        double theta = Math.atan2(drive, strafe);
        double power = Math.hypot(strafe, drive);

        double sin_theta = Math.sin(Math.toRadians(theta));
        double cos_theta = Math.cos(Math.toRadians(theta));

        double max = Math.max(Math.abs(cos_theta), Math.abs(sin_theta));

        double frontLeftPower  = power * cos_theta / max + turn;
        double frontRightPower = power * sin_theta / max - turn;
        double backLeftPower   = power * sin_theta / max - turn;
        double backRightPower  = power * cos_theta / max - turn;

        if ((power + Math.abs(turn)) > 1) {
            frontLeftPower  /= power + Math.abs(turn);
            frontRightPower /= power + Math.abs(turn);
            backLeftPower   /= power + Math.abs(turn);
            backRightPower  /= power + Math.abs(turn);
        }

        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }

    public void driveFieldCentric(double drive, double strafe, double turn) {
        if (drive < DRIVE_DEAD_ZONE)   drive  = 0.0;
        if (strafe < STRAFE_DEAD_ZONE) strafe = 0.0;
        if (turn < TURN_DEAD_ZONE)     turn   = 0.0;

        double theta = Math.atan2(drive, strafe);
        double power = Math.hypot(strafe, drive);

        theta -= robotYaw();

        double sin_theta = Math.sin(Math.toRadians(theta));
        double cos_theta = Math.cos(Math.toRadians(theta));

        double max = Math.max(Math.abs(cos_theta), Math.abs(sin_theta));

        double frontLeftPower  = power * cos_theta / max + turn;
        double frontRightPower = power * sin_theta / max - turn;
        double backLeftPower   = power * sin_theta / max - turn;
        double backRightPower  = power * cos_theta / max - turn;

        if ((power + Math.abs(turn)) > 1) {
            frontLeftPower  /= power + Math.abs(turn);
            frontRightPower /= power + Math.abs(turn);
            backLeftPower   /= power + Math.abs(turn);
            backRightPower  /= power + Math.abs(turn);
        }

        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }

    /**
     * Sets the telemetry for the subsystem
     * @param telemetry The telemetry to set the subsystem
     */
    public void setTelemetry(@NonNull Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    /**
     * Displays debug information about the drive base
     */
    public void debugDrive() {
       if (telemetry == null) return;

       telemetry.addLine("----- Drive Debug -----");
       telemetry.addData("Front Left Direction", frontLeftMotor.getDirection());
       telemetry.addData("Front Right Direction", frontRightMotor.getDirection());
       telemetry.addData("Back Left Direction", backLeftMotor.getDirection());
       telemetry.addData("Back Right Direction", backRightMotor.getDirection());
       telemetry.addData("Front Left Current", frontLeftMotor.getCurrent(AMPS));
       telemetry.addData("Front Right Current", frontRightMotor.getCurrent(AMPS));
       telemetry.addData("Back Left Current", backLeftMotor.getCurrent(AMPS));
       telemetry.addData("Back Right Current", backRightMotor.getCurrent(AMPS));
       telemetry.addData("Front Left Power", frontLeftMotor.getPower());
       telemetry.addData("Front Right Power", frontRightMotor.getPower());
       telemetry.addData("Back Left Power", backLeftMotor.getPower());
       telemetry.addData("Back Right Power", backRightMotor.getPower());
    }
}
