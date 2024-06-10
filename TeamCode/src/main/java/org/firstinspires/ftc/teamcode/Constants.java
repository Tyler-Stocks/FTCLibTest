package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

/**
 * <h1>Constants Class</h1>
 *
 * <h2>Subsystem Nested Classes</h2>
 * <br>
 * <p>
 *     Each subsystem declared in the Subsystems package has a corresponding nested class that
 *     contains the related values. Due to the way that the constants loader is implemented, any
 *     constants values that are outside of a nested class are ignored.
 * </p>
 * <br>
 * <h2>Constants Loader</h2>
 * <br>
 * <p>
        The constants loader class loads constants from the files located in
        /sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/Constants. each file located in the
        aforementioned folder represent some group of constants (E.G Roadrunner, Arm Subsystem).
        To assign a constants nested class a constants file in onbot java simply name the file
        [Subsystem Name]Constants.txt. For example, to assign the nested IntakeConstants a file you
        would name it IntakeConstants.txt. Any file that does not have a corresponding nested class
        will be ignored, and any constants class without a corresponding file will default to the
        values hardcoded into the class.
 * </p>
 * <br>
 * <p>
 *     Note that the nested class doesn't have to represent a subsystem. It could also represent
 *     something more abstract such as vision or path following constants.
 * </p>
 * <br>
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
