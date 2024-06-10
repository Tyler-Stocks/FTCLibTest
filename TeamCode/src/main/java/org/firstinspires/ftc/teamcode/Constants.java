package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

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

    /**
     * <h1>Constants Loader</h1>
     * <br>
     * <p>
     *     The constants loader is a class that loads a properties file from Constants.txt found in
     *     onbot java and fills in all of the fields for all of constants nested classes using
     *     reflection. Due to this approach there is no need to modify this class if more constants, or
     *     nested classes are added.
     * </p>
     * <br>
     * <p>
     *     Another important functionality of this class is to all the saving of all constants to the
     *     aforementioned properties file in onbot java. By calling the saveConstantsFile() function you
     *     can overwrite all of the values in onbot java with the ones that are hardcoded into the
     *     class. This operation is very dangerous as once the values are overwritten there is no way to
     *     get them back unless you have a backup.
     * </p>
     */
    public static class ConstantsLoader {
        /*
         Environment.getExternalStorageDirectory() doesn't actually work so we suppress it with a
         lint.
         */
       @SuppressLint("SdCardPath")
       private static final String CONSTANTS_FILE_LOCATION
               = "/sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/Constants/";

       private static Telemetry telemetry;

       public static void setTelemetry(@NonNull Telemetry telemetry) {
          ConstantsLoader.telemetry = telemetry;
       }


        /**
         * Gets a list of all of the files found in
         * /sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/Constants
         * @return An array containing all of the files found at the aforementioned location.
         * Returns null if none are found.
         * @throws IOException Throws an IOException if a file is found with the same name as the
         * constants directory.
         */
       @Nullable private static File[] getConstantsFiles() throws IOException{
          File constantsDirectory = new File(CONSTANTS_FILE_LOCATION);

          if (constantsDirectory.isFile()) throw new IOException("Constants directory is a file");

          return constantsDirectory.listFiles();
       }

        /**
         * Parses a camelCase file name into its identifier. For Example, "ArmConstants" would be
         * parsed into "Arm".
         * <b>
         *     Note that file names with multiple camelCase words before the the "Constants" suffix
         *     DO NOT WORK. For example, "RedBackdropAutoConstants" will currently be incorrectly
         *     parsed into "Red". This will be fixed *at some point* in the future.
         * </b>
         * @param fileName A camelCase file name to be parsed into an identifier. For example
         *                 "ArmConstants" or "RoadrunnerConstants". Note that a file with multiple
         *                 camelCase words before constants DOES NOT CURRENTLY WORK. For example,
         *                 a file with the name "RedAutoConstants" would have an identifier of
         *                 "Red" instead of the intended "RedAuto".
         * @return The identifier of the passed in file name, if found. For example "ArmConstants"
         * would return "Arm". In the case that no identifier is found, or the file is not suffixed
         * with the word "Constants" returns and empty string.
         */
       @NonNull private static String parseFileName(@NonNull String fileName) {
           // TODO make so that fileNames with more than two words like "RedAutoConstants" work

           // Split the camel/Title case word into its sub words.
           String[] wordsInFileName = fileName.split("(?<!^)(?=[A-Z])");

           if (wordsInFileName.length == 1) return "";

           String lastWordInFileName = wordsInFileName[wordsInFileName.length - 1];

           if (!lastWordInFileName.equals("Constants")) return "";

           return wordsInFileName[0];
       }

       @Nullable private static Class<?> matchConstantsClassToConstantsFile(@NonNull String fileIdentifier) {
          for (Class<?> clazz : ConstantsLoader.class.getClasses()) {
              // Skip constants loader class
              if (clazz.getName().equals("ConstantsLoader")) continue;

              int modifiers = clazz.getModifiers();

              if (!Modifier.isStatic(modifiers)) continue;

              if (clazz.getName().equals(fileIdentifier)) return clazz;
          }

          return null;
       }

       public static void populateClassFromPropertiesFile(@NonNull Class<?> clazz, @NonNull String fileName) throws IOException {
           Properties properties = new Properties();
           properties.load(new FileInputStream(CONSTANTS_FILE_LOCATION + fileName));

           // We use getFields not getDeclaredFields because we don't want private fields.
           Field[] fields = clazz.getFields();

           for (Field field : fields) {
                int modifiers = field.getModifiers();

                // Skip all instance variables (Although there really shouldn't be any)
                if (!Modifier.isStatic(modifiers)) continue;

                // Skip any final variables
                if (Modifier.isFinal(modifiers)) continue;

                populateField(field, properties);
           }
       }

        /**
         * Populates a field with its value from the properties file. If a key matching the name of
         * the field is not found, the function will simply do nothing.
         * @param field The field to populate
         * @param properties The properties file to read from
         */
        private static void populateField(@NonNull Field field, @NonNull Properties properties) {
            String fieldName   = field.getName();
            Class<?> fieldType = field.getType();

            if (!properties.containsKey(fieldName)) return;

            try {
                if (fieldType.isAssignableFrom(double.class)) {
                    field.setDouble(fieldName, getDoubleFromPropertiesFile(fieldName, properties));
                }

                if (fieldType.isAssignableFrom(int.class)) {
                    field.setInt(fieldName, getIntFromPropertiesFile(fieldName, properties));
                }
            } catch (IllegalAccessException exception) {
                telemetry.addData("Illegal access to field", fieldName);
            }
        }

       public static void loadConstants() throws IOException {
            File[] constantsFiles = getConstantsFiles();

            // If there is no constants files, then we don't need to load the constants
            if (constantsFiles == null) {
                telemetry.addLine("No constants files found. Resorting to defaults.");
                return;
            }

            for (File file : constantsFiles) {
                String fileName = file.getName();

                String fileIdentifier = parseFileName(fileName);

                // Skip the file if it does not end in "Constants"
                if (fileIdentifier.isEmpty()) continue;

                Class<?> constantsClass = matchConstantsClassToConstantsFile(fileIdentifier);

                // If there is no constants file that matches a constants class, continue.
                if (constantsClass == null) continue;

                populateClassFromPropertiesFile(constantsClass, fileName);
            }
       }

       private static double getDoubleFromPropertiesFile(@NonNull String key, @NonNull Properties properties) {
            return Double.parseDouble(properties.getProperty(key));
       }

       private static int getIntFromPropertiesFile(@NonNull String key, @NonNull Properties properties) {
            return Integer.parseInt(properties.getProperty(key));
       }
    }
}
