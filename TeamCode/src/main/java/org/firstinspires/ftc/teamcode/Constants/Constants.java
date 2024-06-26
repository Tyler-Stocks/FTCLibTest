package org.firstinspires.ftc.teamcode.Constants;


import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.LogoFacingDirection.*;
import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection.UP;

import android.util.Size;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU.Parameters;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.LogoFacingDirection;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection;

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

        public static volatile double INTAKE_POWER              = 1.0;
        public static volatile double OUTTAKE_POWER             = 0.4;
        public static volatile double INTAKE_TRIGGER_THRESHOLD  = 0.2;
        public static volatile double OUTTAKE_TRIGGER_THRESHOLD = 0.2;
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

        public static volatile double DEFAULT_ELEVATOR_POWER        = 1.0;
        public static volatile double DEFAULT_WORM_POWER            = 1.0;
        public static volatile double ELEVATOR_HOMING_POWER         = -0.8;
        public static volatile double WORM_HOMING_POWER             = -0.8;
        public static volatile double WORM_BACKLASH_REMOVING_POWER  = 0.25;
        public static volatile double ELEVATOR_INTAKE_HOLDING_POWER = 0.001;

        public static volatile double WORM_SAFETY_VOLTAGE = 0.8;

        public static volatile int WORM_SAFETY_LIMIT     = 700;
        public static volatile int WORM_SAFETY_THRESHOLD = WORM_SAFETY_LIMIT + 20;
        public static volatile int MAX_ELEVATOR_POSITION = 2750;
        public static volatile int MAX_WORM_POSITION     = 2500;

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

        public static volatile int HANGING_WORM_POSITION   = 2500;
        public static volatile int LAUNCHING_WORM_POSITION = 1600;

        public static volatile int WORM_FRAME_LIMIT     = 500;
        public static volatile int ELEVATOR_FRAME_LIMIT = 500;

        public static volatile double OUTTAKE_DOOR_OPEN_POSITION   = 0.25;
        public static volatile double OUTTAKE_DOOR_CLOSED_POSITION = 0.0;
    }

    @Config
    public static class DrivebaseConstants {
       public static final String FRONT_LEFT_DRIVE_MOTOR_NAME  = "frontLeftDriveMotor";
       public static final String FRONT_RIGHT_DRIVE_MOTOR_NAME = "frontRightDriveMotor";
       public static final String BACK_LEFT_DRIVE_MOTOR_NAME   = "backLeftDriveMotor";
       public static final String BACK_RIGHT_DRIVE_MOTOR_NAME  = "backRightDriveMotor";
       public static final String IMU_NAME                     = "imu";

       public static volatile double DRIVE_DEAD_ZONE  = 0.05;
       public static volatile double STRAFE_DEAD_ZONE = 0.05;
       public static volatile double TURN_DEAD_ZONE   = 0.05;

       // ----- IMU Parameters ----- //
       private static final UsbFacingDirection IMU_USB_DIRECTION   = UP;
       private static final LogoFacingDirection IMU_LOGO_DIRECTION = RIGHT;

       private static  final RevHubOrientationOnRobot IMU_ORIENTATION_ON_ROBOT
               = new RevHubOrientationOnRobot(IMU_LOGO_DIRECTION, IMU_USB_DIRECTION);
       public static final Parameters IMU_PARAMETERS
               = new Parameters(IMU_ORIENTATION_ON_ROBOT);
    }

    @Config
    public static class PurplePixelPlacerConstants {
        public static final String PURPLE_PIXEL_PLACER_SERVO_NAME = "purplePixelPlacerServo";

        public static volatile double PURPLE_PIXEL_PLACER_NEUTRAL_POSITION = 1.0;
        public static volatile double PURPLE_PIXEL_PLACER_ACTIVE_POSITION  = 0.0;
    }

    @Config
    public static class MosaicFixerConstants {
        public static final String MOSAIC_FIXER_SERVO_LEFT_NAME  = "leftMosaicFixerServo";
        public static final String MOSAIC_FIXER_SERVO_RIGHT_NAME = "rightMosaicFixerServo";

        public static volatile double MOSAIC_FIXER_LEFT_RETRACTED_POSITION = 0.0;
        public static volatile double MOSAIC_FIXER_LEFT_LOW_POSITION    = 0.62;
        public static volatile double MOSAIC_FIXER_LEFT_MEDIUM_POSITION = 0.59;
        public static volatile double MOSAIC_FIXER_LEFT_HIGH_POSITION   = 0.54;

        public static volatile double MOSAIC_FIXER_RIGHT_RETRACTED_POSITION = 0.0;
        public static volatile double MOSAIC_FIXER_RIGHT_LOW_POSITION  = 0.61;
        public static volatile double MOSAIC_FIXER_RIGHT_HIGH_POSITION = 0.55;
    }

    @Config
    public static class AprilTagConstants {
        public static volatile float DECIMATION = 3.0f;

        public static volatile double FX = 660.75;
        public static volatile double FY = 660.75;
        public static volatile double CX = 323.034;
        public static volatile double CY = 230.681;

        public static final Size APRIL_TAG_CAMERA_RESOLUTION = new Size(640, 480);
    }
}
