package org.firstinspires.ftc.teamcode.Constants;

import com.acmerobotics.dashboard.config.Config;

/**
 * <h1>Constants Class</h1>
 * <br>
 * <h2>Compatibility with FTC dashboard and Constants File Loading</h2>
 * <p>
 *     Each nested class should be wrapped with the {@link com.acmerobotics.dashboard.config.Config}
 *     decorator so that they can be used with FTC dashboard.
 * </p>
 * <br>
 * <p>
 *     To be compatible with the {@link ConstantsLoader} class a
 *     nested class must meet the following requirements.
 *     <ul>
 *         <li>The nested class must be suffixed with the word Constants</li>
 *         <li>The nested class must have the same name as its corresponding onbot java file</li>
 *     </ul>
 *     Note that values labeled as final, private, or protected have their privacy/immutability
 *     respected.
 * </p>
 * <br>
 * <h2>Organization</h2>
 * <br>
 * <p>
 *     The following organization guidelines are very general and there may be situations in which
 *     they should be violated, at the end of the day you should use your own discretion when
 *     determining how to organize the nested classes. However, some general guidelines are as
 *     follows:
 *     <ul>
 *         <li>Each subsystem should have it's own nested class</li>
 *         <li>Different aspects of auto should have their own nested classes</li>
 *         <li>Device names should be private as to not be controllable via Onbot Java</li>
 *     </ul>
 * </p>
 */
public class Constants {

    @Config
    public static class LauncherConstants {
        public static final String LAUNCHER_SERVO_NAME = "launcherServo";

        public static volatile double LAUNCH_POSITION = 0.6;
        public static volatile double ZERO_POSITION   = 0.0;
    }

    @Config
    public static class HangerConstants {
        public static final String HANGER_SERVO_NAME = "hangerServo";

        public static volatile double HANG_POSITION = 1.0;
        public static volatile double ZERO_POSITION = 0.0;
    }

    @Config
    public static class IntakeConstants {
        public static final String INTAKE_MOTOR_NAME     = "intakeMotor";
        public static final String FRONT_BEAM_BREAK_NAME = "frontBeamBreak";
        public static final String BACK_BEAM_BREAK_NAME  = "backBeamBreak";

        public static volatile double INTAKE_POWER  = 1.0;
        public static volatile double OUTTAKE_POWER = 0.4;
    }

    @Config
    public static class ArmConstants {
        public static final String WORM_MOTOR_NAME            = "wormMotor";
        public static final String ELEVATOR_MOTOR_NAME        = "elevatorMotor";
        public static final String WORM_LIMIT_SWITCH_NAME     = "wormLimitSwitch";
        public static final String ELEVATOR_LIMIT_SWITCH_NAME = "elevatorLimitSwitch";
        public static final String WORM_POTENTIOMETER_NAME    = "wormPotentiometer";
        public static final String LEFT_OUTTAKE_SERVO_NAME    = "leftOuttakeServo";
        public static final String RIGHT_OUTTAKE_SERVO_NAME   = "rightOuttakeServo";

        public static volatile double DEFAULT_ELEVATOR_POWER       = 1.0;
        public static volatile double DEFAULT_WORM_POWER           = 1.0;
        public static volatile double ELEVATOR_HOMING_POWER        = -0.8;
        public static volatile double WORM_HOMING_POWER            = -0.8;
        public static volatile double WORM_BACKLASH_REMOVING_POWER = 0.25;

        public static volatile double WORM_SAFETY_VOLTAGE = 0.8;

        public static volatile int WORM_SAFETY_LIMIT     = 700;
        public static volatile int MAX_ELEVATOR_POSITION = 2750;
        public static volatile int MAX_WORM_POSITION     = 2200;

        public static volatile int BOTTOM_ELEVATOR_POSITION  = 1580;
        public static volatile int BOTTOM_WORM_POSITION      = 1250;
        public static volatile int LOW_ELEVATOR_POSITION     = 1782;
        public static volatile int LOW_WORM_POSITION         = 1432;
        public static volatile int MEDIUM_ELEVATOR_POSITION  = 2107;
        public static volatile int MEDIUM_WORM_POSITION      = 1713;
        public static volatile int HIGH_ELEVATOR_POSITION    = 2415;
        public static volatile int HIGH_WORM_POSITION        = 1822;
        public static volatile int TOP_ELEVATOR_POSITION     = 2667;
        public static volatile int TOP_WORM_POSITION         = 1909;

        public static volatile int WORM_FRAME_LIMIT     = 500;
        public static volatile int ELEVATOR_FRAME_LIMIT = 500;

        public static volatile double OUTTAKE_DOOR_OPEN_POSITION   = 0.25;
        public static volatile double OUTTAKE_DOOR_CLOSED_POSITION = 0.0;
    }
}
