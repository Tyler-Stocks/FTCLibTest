package org.firstinspires.ftc.teamcode.Subsystems.Arm;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;

import static org.firstinspires.ftc.teamcode.Constants.ArmConstants.*;
import static org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmHomingState.*;
import static org.firstinspires.ftc.teamcode.Subsystems.Arm.ArmState.*;

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

    private int wormTargetPosition, elevatorTargetPosition;

    private double elevatorPower, wormPower;

    public ArmSubsystem(@NonNull HardwareMap hardwareMap) {

        // Control Hub Digital Port 0
        wormLimitSwitch  = hardwareMap.get(RevTouchSensor.class, "wormLimitSwitch");
        // Expansion Hub Digital Port 0
        elevatorLimitSwitch = hardwareMap.get(RevTouchSensor.class, "elevatorLimitSwitch");

        // Expansion Hub Motor Port 0
        wormMotor  = hardwareMap.get(DcMotorImplEx.class, "wormMotor");
        // Control Hub Motor Port 3
        elevatorMotor = hardwareMap.get(DcMotorImplEx.class, "elevatorMotor");

        // Expansion Hub Port 1
        leftOuttakeServo = hardwareMap.get(ServoImplEx.class, "leftOuttakeServo");
        // Expansion Hub Port 2
        rightOuttakeServo = hardwareMap.get(ServoImplEx.class, "rightOuttakeServo");

        wormPotentiometer = hardwareMap.analogInput.get("wormPotentiometer");

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

        openOuttake();

        armState       = ArmState.UNKNOWN;
        armHomingState = START;
    }

    @Override public void periodic() {
        switch (armState) {
            case AT_POSITION:
                if (!wormMotor.isBusy()) wormMotor.setPower(0.0);

                if (elevatorTargetPosition == 0 && wormTargetPosition == 0) {
                    elevatorMotor.setPower(0.0);
                } else {
                    elevatorMotor.setPower(0.001);
                }
                break;
            case TO_POSITION:
                if (elevatorTargetPosition == 0 && wormTargetPosition == 0) {

                    if (wormMotor.getCurrentPosition() > WORM_SAFETY_LIMIT && elevatorMotor.getCurrentPosition() > 40) {
                        extendElevator(0, elevatorPower);
                    }

                    else if (wormMotor.getCurrentPosition() < WORM_SAFETY_LIMIT && elevatorMotor.getCurrentPosition() > 900) {
                        rotateWorm(WORM_SAFETY_LIMIT + 20, wormPower);
                        extendElevator(1000);

                    } else if (90 < elevatorMotor.getCurrentPosition() && elevatorMotor.getCurrentPosition() < 900) {
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
                if (wormMotor.getTargetPosition()== wormTargetPosition && elevatorMotor.getTargetPosition()== elevatorTargetPosition&&
                        !elevatorMotor.isBusy() && !wormMotor.isBusy()) {

                    armState = AT_POSITION;
                }
                break;
            // If we don't know what the current state of the robot is we need to home.
            // This is mostly used at the beginning of TeleOp because we don't know what the state
            // of the arm is. It is also useful to automatically home after an unexpected robot
            // disconnect
            case UNKNOWN:
                setHoming();
                break;
            // Homing sequence should only be called when the arm state is homing
            case HOMING:
                home();
                break;
        }
    }

    /**
     * Homes the arm and worm. First, it homes the elevator, then the worm after it is finished
     */
    private void home() {
        switch (armHomingState) {
            case START:
                openOuttake(); // Make sure the tray is closed so we don't break it

                wormMotor.setMode(RUN_USING_ENCODER);
                elevatorMotor.setMode(RUN_USING_ENCODER);

                // If the worm power is greater than the safety voltage, start homing the elevator
                if (wormPotentiometer.getVoltage() >= WORM_SAFETY_VOLTAGE) {
                    wormMotor.setPower(0.0);
                    armHomingState = HOMING_ELEVATOR;
                } else { // If the voltage is less than the safety limit, drive the worm up,
                    wormMotor.setPower(1.0);
                    elevatorMotor.setPower(-0.05);

                    // If the limit switch is pressed, we start homing the elevator
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
                wormMotor.setMode(STOP_AND_RESET_ENCODER);
                elevatorMotor.setMode(STOP_AND_RESET_ENCODER);
                wormMotor.setMode(RUN_USING_ENCODER);
                elevatorMotor.setMode(RUN_USING_ENCODER);

                armState      = AT_POSITION;
                armHomingState= IDLE;

                elevatorTargetPosition = 0;
                wormTargetPosition     = 0;
                break;
            case IDLE:
                break;
        }
    }

    public void openOuttake() {
        leftOuttakeServo.setPosition(OUTTAKE_DOOR_OPEN_POSITION);
        rightOuttakeServo.setPosition(OUTTAKE_DOOR_OPEN_POSITION);
    }

    public void openLeftOuttakeDoor() {
        leftOuttakeServo.setPosition(OUTTAKE_DOOR_OPEN_POSITION);
    }

    public void openRightOuttakeDoor() {
        leftOuttakeServo.setPosition(OUTTAKE_DOOR_CLOSED_POSITION);
    }

    public void closeOuttake() {
        rightOuttakeServo.setPosition(OUTTAKE_DOOR_OPEN_POSITION);
    }

    public void closeLeftOuttakeDoor() {
        leftOuttakeServo.setPosition(OUTTAKE_DOOR_CLOSED_POSITION);
    }

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

    /**
     * Extends the elevator to the provided position at the provided power
     * @param targetPos The position to move the elevator to
     * @param power The power to move to the target position at
     */
    private void extendElevator(int targetPos, double power) {
        elevatorMotor.setTargetPosition(targetPos);
        elevatorMotor.setMode(RUN_TO_POSITION);
        elevatorMotor.setPower(power);
    }

    private void extendElevator(int targetPosition) {
        extendElevator(targetPosition, DEFAULT_ELEVATOR_POWER);
    }

    /**
     * Rotates the worm to the provided position at the provided power
     * @param targetPosition The position to move the elevator to
     * @param power The power to move to the target position at
     */
    private void rotateWorm(int targetPosition, double power) {
        wormMotor.setTargetPosition(targetPosition);
        wormMotor.setMode(RUN_TO_POSITION);
        wormMotor.setPower(power);
    }

    public boolean isAtHome() {
        return elevatorTargetPosition == 0 && wormTargetPosition == 0 && armState == AT_POSITION;
    }

    public boolean isWithinFrame() {
        return elevatorMotor.getCurrentPosition() <= ELEVATOR_FRAME_LIMIT
                && wormMotor.getCurrentPosition() <= WORM_FRAME_LIMIT;
    }
}
