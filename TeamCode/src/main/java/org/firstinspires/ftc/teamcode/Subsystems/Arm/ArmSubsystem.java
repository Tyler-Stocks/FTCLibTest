package org.firstinspires.ftc.teamcode.Subsystems.Arm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;

import static org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit.AMPS;
import static org.firstinspires.ftc.teamcode.Constants.Constants.ArmConstants.*;
import static org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmHomingState.*;
import static org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmState.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Utility.MotorUtility;

/**
 * <h1>ArmSubsystem</h1>
 * <br>
 * <p>
 *     Subsystem to encapsulate the entire arm mechanism. This includes the following components:
 *     <ul>
 *         <li>The worm and extension motors</li>
 *         <li>The worm and extension limit switches</li>
 *         <li>The worm potentiometer</li>
 *         <li>The outtake servos</li>
 *     </ul>
 * </p>
 */
public class ArmSubsystem extends SubsystemBase {
    private ArmHomingState armHomingState;
    private ArmState armState;

    private final DcMotorImplEx wormMotor,
                                elevatorMotor;

    private final ServoImplEx leftOuttakeServo,
                              rightOuttakeServo;

    private final RevTouchSensor wormLimitSwitch,
                                 elevatorLimitSwitch;

    private final AnalogInput wormPotentiometer;

    private final Telemetry telemetry;

    private int wormTargetPosition, elevatorTargetPosition;

    private double elevatorPower, wormPower;

    /**
     * Constructs, and initializes a new arm subsystem.
     * @param opMode The opmode you are running ; To obtain the hardwareMap and telemetry objects
     */
    public ArmSubsystem(@NonNull OpMode opMode) {
        telemetry = opMode.telemetry;

        wormLimitSwitch
                = opMode.hardwareMap.get(RevTouchSensor.class, WORM_LIMIT_SWITCH_NAME);
        elevatorLimitSwitch
                = opMode.hardwareMap.get(RevTouchSensor.class, ELEVATOR_LIMIT_SWITCH_NAME);

        wormMotor     = opMode.hardwareMap.get(DcMotorImplEx.class, WORM_MOTOR_NAME);
        elevatorMotor = opMode.hardwareMap.get(DcMotorImplEx.class, ELEVATOR_MOTOR_NAME);

        leftOuttakeServo  = opMode.hardwareMap.get(ServoImplEx.class, LEFT_OUTTAKE_SERVO_NAME);
        rightOuttakeServo = opMode.hardwareMap.get(ServoImplEx.class, RIGHT_OUTTAKE_SERVO_NAME);

        wormPotentiometer = opMode.hardwareMap.analogInput.get(WORM_POTENTIOMETER_NAME);

        elevatorMotor.setDirection(REVERSE);
        elevatorMotor.setZeroPowerBehavior(BRAKE);

        elevatorMotor.setMode(STOP_AND_RESET_ENCODER);
        wormMotor.setMode(STOP_AND_RESET_ENCODER);

        elevatorMotor.setMode(RUN_USING_ENCODER);
        wormMotor.setMode(RUN_USING_ENCODER);

        elevatorMotor.setPower(0.0);
        wormMotor.setPower(0.0);

        elevatorTargetPosition = 0;
        wormTargetPosition = 0;

        elevatorPower = DEFAULT_ELEVATOR_POWER;
        wormPower     = DEFAULT_WORM_POWER;

        closeOuttake();

        armState       = ArmState.UNKNOWN;
        armHomingState = START;
    }

    @Override public void periodic() {
        switch (armState) {
            case AT_POSITION:
                if (!wormMotor.isBusy()) wormMotor.setPower(0.0);

                if (elevatorTargetPosition == 0 && wormTargetPosition == 0) {
                    elevatorMotor.setPower(0.0);
                }
                break;
            case TO_POSITION:
                if (elevatorTargetPosition == 0 && wormTargetPosition == 0) {

                    if (wormMotor.getCurrentPosition() > WORM_SAFETY_LIMIT
                            && elevatorMotor.getCurrentPosition() > 10) {
                        extendElevator(0, elevatorPower);
                    }

                    else if (wormMotor.getCurrentPosition() < WORM_SAFETY_LIMIT
                            && elevatorMotor.getCurrentPosition() > 900) {
                        rotateWorm(WORM_SAFETY_THRESHOLD, wormPower);
                        extendElevator(1000);

                    } else if (90 < elevatorMotor.getCurrentPosition()
                            && elevatorMotor.getCurrentPosition() < 900) {
                        extendElevator(0, elevatorPower);
                    } else {
                        rotateWorm(wormTargetPosition, wormPower);
                    }
                } else if (elevatorTargetPosition> 0 && wormMotor.getCurrentPosition() < WORM_SAFETY_LIMIT) {
                    wormTargetPosition = Math.max(wormTargetPosition, (WORM_SAFETY_LIMIT + 20));
                    rotateWorm(wormTargetPosition, wormPower);
                } else {
                    rotateWorm(wormTargetPosition, wormPower);

                    if (wormMotor.getCurrentPosition() > WORM_SAFETY_LIMIT) {
                        extendElevator(elevatorTargetPosition, elevatorPower);
                    }
                }

                if (isAtPosition()) armState = AT_POSITION;

                break;
            case UNKNOWN:
                setHoming();
                break;
            case HOMING:
                home();
                break;
        }
    }

    private void home() {
        switch (armHomingState) {
            case START:
                closeOuttake();

                MotorUtility.setRunModes(RUN_USING_ENCODER, wormMotor, elevatorMotor);

                if (wormPotentiometer.getVoltage() >= WORM_SAFETY_VOLTAGE) {
                    wormMotor.setPower(0.0);
                    armHomingState = HOMING_ELEVATOR;
                } else {
                    wormMotor.setPower(1.0);
                    elevatorMotor.setPower(-0.05);

                    if (elevatorLimitSwitch.isPressed()) {
                        wormMotor.setPower(0.0);
                        armHomingState = HOMING_ELEVATOR;
                    }
                }

                armState = HOMING;
                break;
            case HOMING_ELEVATOR:
                elevatorMotor.setPower(ELEVATOR_HOMING_POWER);

                // Stop homing once the limit switch is pressed
                if (elevatorLimitSwitch.isPressed()) {
                    armHomingState = HOMING_WORM;
                    elevatorMotor.setMode(STOP_AND_RESET_ENCODER);
                    wormMotor.setPower(0.0);
                }
                break;
            case HOMING_WORM:
                wormMotor.setPower(WORM_HOMING_POWER);

                // If the worm limit switch is pressed, start removing backlash
                if (wormLimitSwitch.isPressed()) {
                    armHomingState = REMOVING_WORM_BACKLASH;
                    wormMotor.setPower(0);
                }
                break;
            case REMOVING_WORM_BACKLASH:
                wormMotor.setPower(WORM_BACKLASH_REMOVING_POWER);

                // Remove the backlash of the worm.
                if (!wormLimitSwitch.isPressed()) {
                    armHomingState = COMPLETE;
                    wormMotor.setMode(STOP_AND_RESET_ENCODER);
                    rotateWorm(-10,0.0);
                }
                break;
            case COMPLETE:
                MotorUtility.setRunModes(STOP_AND_RESET_ENCODER, wormMotor, elevatorMotor);
                MotorUtility.setRunModes(RUN_USING_ENCODER, wormMotor, elevatorMotor);

                armState       = AT_POSITION;
                armHomingState = IDLE;

                elevatorTargetPosition = 0;
                wormTargetPosition     = 0;
                break;
            case IDLE:
                break;
        }
    }

    /**
     * Opens both outtake doors
     */
    public void openOuttake() {
        leftOuttakeServo.setPosition(OUTTAKE_DOOR_OPEN_POSITION);
        rightOuttakeServo.setPosition(OUTTAKE_DOOR_OPEN_POSITION);
    }

    /**
     * Opens the left outtake door
     */
    public void openLeftOuttakeDoor() {
        leftOuttakeServo.setPosition(OUTTAKE_DOOR_OPEN_POSITION);
    }

    /**
     * Opens the right outtake door
     */
    public void openRightOuttakeDoor() {
        leftOuttakeServo.setPosition(OUTTAKE_DOOR_CLOSED_POSITION);
    }

    /**
     * Closes both outtake doors
     */
    public void closeOuttake() {
        rightOuttakeServo.setPosition(OUTTAKE_DOOR_OPEN_POSITION);
    }

    /**
     * Closes the left outtake door
     */
    public void closeLeftOuttakeDoor() {
        leftOuttakeServo.setPosition(OUTTAKE_DOOR_CLOSED_POSITION);
    }

    /**
     * Closes the right outtake door
     */
    public void closeRightOuttakeDoor() {
        rightOuttakeServo.setPosition(OUTTAKE_DOOR_CLOSED_POSITION);
    }

    /**
     * Tells the arm that it should be homing. Note that homing cannot be
     * interrupted, and that homing doesn't start until the next time periodic
     * is called.
     */
    public void setHoming() {
        armHomingState = START;
        armState       = HOMING;
    }

    /**
     * Sets the target position of the arm. Also sets the
     * normalPeriodArmState to TO_POSITION
     * @param wormTargetPosition New worm target position
     * @param elevatorTargetPos New elevator target position
     */
    public void setTargetPos(int elevatorTargetPos,  int wormTargetPosition) {
        armState = TO_POSITION;

        this.elevatorTargetPosition = Math.min(elevatorTargetPos, MAX_ELEVATOR_POSITION);
        this.wormTargetPosition     = Math.min(wormTargetPosition, MAX_WORM_POSITION);
    }

    private boolean isAtPosition() {
        return wormMotor.getTargetPosition()         == wormTargetPosition
                && elevatorMotor.getTargetPosition() == elevatorTargetPosition
                && !elevatorMotor.isBusy()
                && !wormMotor.isBusy();
    }

    private void extendElevator(int targetPos, double power) {
        elevatorMotor.setTargetPosition(targetPos);
        elevatorMotor.setMode(RUN_TO_POSITION);
        elevatorMotor.setPower(power);
    }

    private void extendElevator(int targetPosition) {
        extendElevator(targetPosition, DEFAULT_ELEVATOR_POWER);
    }

    private void rotateWorm(int targetPosition, double power) {
        wormMotor.setTargetPosition(targetPosition);
        wormMotor.setMode(RUN_TO_POSITION);
        wormMotor.setPower(power);
    }

    /**
     * @return Whether the arm is currently at home (0,0) / Fully inside the frame
     */
    public boolean isAtHome() {
        return elevatorTargetPosition == 0 && wormTargetPosition == 0 && armState == AT_POSITION;
    }

    /**
     * @return Whether the worm and elevator is within the frame of the robot
     */
    public boolean isWithinFrame() {
        return elevatorMotor.getCurrentPosition() <= ELEVATOR_FRAME_LIMIT
                && wormMotor.getCurrentPosition() <= WORM_FRAME_LIMIT;
    }

    /**
     * @return The current position of the motor
     */
    public int wormPosition() {
        return wormMotor.getCurrentPosition();
    }

    /**
     * @return The target position of the motor reported by wormMotor.getTargetPosition()
     */
    public int wormTargetPosition() {
        return wormMotor.getTargetPosition();
    }

    /**
     * @return The local target position of the worm motor obtained through the local variable
     * wormMotorTargetPosition
     */
    public int localWormTargetPosition() {
        return wormTargetPosition;
    }

    /**
     * @return The current position of the elevator motor
     */
    public int elevatorPosition() {
        return elevatorMotor.getCurrentPosition();
    }

    /**
     * @return The target position of the elevator as reported by elevatorMotor.getTargetPosition()
     */
    public int elevatorTargetPosition() {
        return elevatorMotor.getTargetPosition();
    }

    /**
     * @return The local target position of the elevator motor obtained through the local variable
     * elevatorTargetPosition
     */
    public int localElevatorTargetPosition() {
        return elevatorTargetPosition;
    }

    /**
     * Displays debug information about the arm
     */
    public void debugArm() {

       telemetry.addLine("----- Arm Debug -----");
       telemetry.addData("Worm Current (AMPS)", wormMotor.getCurrent(AMPS));
       telemetry.addData("Elevator Current (AMPS)", elevatorMotor.getCurrent(AMPS));
       telemetry.addData("Worm Limit Switch Pressed", wormLimitSwitch.isPressed());
       telemetry.addData("Elevator Limit Switch Pressed", elevatorLimitSwitch.isPressed());
       telemetry.addData("Worm Position", wormMotor.getCurrentPosition());
       telemetry.addData("Elevator Position", elevatorMotor.getCurrentPosition());
       telemetry.addData("Worm Target Position", wormMotor.getTargetPosition());
       telemetry.addData("Elevator Target Position", elevatorMotor.getTargetPosition());
       telemetry.addData("Worm Target Position (Local)", wormTargetPosition);
       telemetry.addData("Elevator Target Position (Local)", elevatorTargetPosition);
       telemetry.addData("Worm Direction", wormMotor.getDirection());
       telemetry.addData("Elevator Direction", elevatorMotor.getDirection());
       telemetry.addData("Potentiometer Voltage", wormPotentiometer.getVoltage());
    }

    /**
     * Displays debug information about the outtake
     */
    public void debugOuttake() {

        telemetry.addLine("----- Outtake Debug -----");
        telemetry.addData("Left Outtake Position", leftOuttakeServo.getPosition());
        telemetry.addData("Right Outtake Position", rightOuttakeServo.getPosition());
        telemetry.addData("Left Outtake Direction (SDK)", leftOuttakeServo.getDirection());
        telemetry.addData("Right Outtake Direction (SDK)", rightOuttakeServo.getDirection());
    }
}
