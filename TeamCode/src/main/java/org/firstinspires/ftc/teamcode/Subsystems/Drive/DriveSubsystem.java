package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Utility.MotorUtility;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS;
import static org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit.AMPS;
import static org.firstinspires.ftc.teamcode.Constants.Constants.DrivebaseConstants.*;

/**
 * <h1>Drive Subsystem</h1>
 * <br>
 * <p>
 *     Subsystem to encapsulate the drive motors. Contains the following hardware
 *     <ul>
 *         <li>Front Left Drive Motor</li>
 *         <li>Front Right Drive Motor</li>
 *         <li>Back Left Drive Motor</li>
 *         <li>Back Right Drive Motor</li>
 *         <li>Imu</li>
 *     </ul>
 * </p>
 */
public class DriveSubsystem extends SubsystemBase {
    private final DcMotorImplEx frontLeftMotor,
                                frontRightMotor,
                                backLeftMotor,
                                backRightMotor;

    private final IMU imu;

    private final Telemetry telemetry;

    /**
     * Constructs a new drive subsystems
     * @param opMode The opMode you are running ; To obtain the hardwareMap and telemetry objects
     */
    public DriveSubsystem(@NonNull OpMode opMode) {
        telemetry = opMode.telemetry;

        frontLeftMotor  = opMode.hardwareMap.get(DcMotorImplEx.class, FRONT_LEFT_DRIVE_MOTOR_NAME);
        frontRightMotor = opMode.hardwareMap.get(DcMotorImplEx.class, FRONT_RIGHT_DRIVE_MOTOR_NAME);
        backLeftMotor   = opMode.hardwareMap.get(DcMotorImplEx.class, BACK_LEFT_DRIVE_MOTOR_NAME);
        backRightMotor  = opMode.hardwareMap.get(DcMotorImplEx.class, BACK_RIGHT_DRIVE_MOTOR_NAME);

        imu = opMode.hardwareMap.get(IMU.class, IMU_NAME);

        imu.initialize(IMU_PARAMETERS);

        MotorUtility.setDirections(REVERSE, frontLeftMotor, backLeftMotor);
        MotorUtility.setDirections(FORWARD, frontRightMotor, backRightMotor);

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

    /**
     * Drives the robot relative to itself
     * @param drive The drive value
     * @param strafe The strafe value
     * @param turn The turn value
     */
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
        double backLeftPower   = power * sin_theta / max + turn;
        double backRightPower  = power * cos_theta / max - turn;

        double turnMagnitude = Math.abs(turn);

        if ((power + turnMagnitude) > 1.0) {
            frontLeftPower  /= power + turnMagnitude;
            frontRightPower /= power + turnMagnitude;
            backLeftPower   /= power + turnMagnitude;
            backRightPower  /= power + turnMagnitude;
        }

        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }

    /**
     * Drives the robot relative to the field
     * @param drive The value to move forward
     * @param strafe The value to strafe
     * @param turn The value to turn
     */
    private void driveFieldCentric(double drive, double strafe, double turn) {
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
     * Displays debug information about the drive base
     */
    public void debugDrive() {
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
