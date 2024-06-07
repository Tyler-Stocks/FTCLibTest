package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

/**
 * <h1>Constants Class</h1>
 *
 * <h2>Subsystem Nested Classes</h2>
 * <p>
 *     Each subsystem declared in the Subsystems package has a corresponding nested class that
 *     contains the related values. Due to the way that the constants loader is implemented, any
 *     constants values that are outside of a nested class will be ignored. This can be useful if
 *     you would like to have a constant that cannot be overwritten from onbot java such as the
 *     config file name of a hardware device.
 * </p>
 * <br>
 * <h2>Constants Loader</h2>
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
public class Constants {

    public static class LauncherConstants {
        public static volatile double LAUNCH_POSITION = 0.6;
        public static volatile double ZERO_POSITION   = 0.0;
    }

    public static class HangerConstants {
        public static volatile double HANG_POSITION = 1.0;
        public static volatile double ZERO_POSITION = 0.0;
    }

    public static class IntakeConstants {
        public static volatile double INTAKE_POWER  = 1.0;
        public static volatile double OUTTAKE_POWER = 0.4;
    }

    public static class ArmConstants {
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
       private static final Properties properties = new Properties();

       /*
       TODO Try out Environment.getExternalStorageDirectory() so that it stops bugging me and we can
       TODO rid of this stupid SuppressLint
        */
       @SuppressLint("SdCardPath")
       private static final String CONSTANTS_FILE_LOCATION  = "/sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/";
       private static final String CONSTANTS_FILE_NAME      = "Constants.txt";

       private static Telemetry telemetry;

        /**
         * Loads constants from a properties file located in onbot java.
         * @param telemetry The telemetry to display errors on
         */
       public static void load(@NonNull Telemetry telemetry) throws IOException {
           ConstantsLoader.telemetry = telemetry;

           loadPropertiesFile();
           populateConstantsClass();

           telemetry.update();
       }

        /**
         * Loads the properties file
         * @throws IOException Throws an IOException if the properties file cannot be loaded
         */
       private static void loadPropertiesFile() throws IOException {
                properties.clear();
                properties.load(new FileInputStream(CONSTANTS_FILE_LOCATION + CONSTANTS_FILE_NAME));
       }

        /**
         * Populates a field with its value from the properties file. If a key matching the name of
         * the field is not found, the function will simply do nothing.
         * @param field The field to populate
         */
       private static void populateField(@NonNull Field field) {
           String fieldName   = field.getName();
           Class<?> fieldType = field.getType();

           if (!properties.containsKey(fieldName)) return;

           try {
              if (fieldType.isAssignableFrom(double.class)) {
                  field.setDouble(fieldName, getDoubleFromPropertiesFile(fieldName));
              }

              if (fieldType.isAssignableFrom(int.class)) {
                  field.setInt(fieldName, getIntFromPropertiesFile(fieldName));
              }
          } catch (IllegalAccessException exception) {
                telemetry.addData("Illegal access to field", fieldName);
           }
       }

        /**
         * Populates all of the values of one of the nested classes with the matching value found
         * in Constants.txt in onbot java
         * @param clazz The class to populate the fields of
         */
       private static void populateClass(@NonNull Class<?> clazz) {
           Field[] fields = clazz.getDeclaredFields();

           for (Field field : fields) {
               if (!Modifier.isStatic(field.getModifiers())) continue;

               populateField(field);
           }
       }

        /**
         * Populates the constants class with values from the Constants.txt file located in onbot
         * java
         */
       private static void populateConstantsClass() {
           Class<?>[] classes = Constants.class.getDeclaredClasses();

           for (Class<?> clazz : classes) {
                // Skip the constants loader class
                if (clazz.getName().equals("ConstantsLoader")) continue;

                populateClass(clazz);
            }
       }

        /**
         * Saves a field to the constants file
         * @param field The field to save to the constants file
         * @throws IOException Throws and IOException if the file cannot be written to
         */
       private static void saveFieldToConstantsFile(@NonNull Field field) throws IOException {
           String fieldName   = field.getName();
           Class<?> fieldType = field.getType();

           String output = "";

           try {
               if (fieldType.isAssignableFrom(double.class)) {
                   output = fieldName + "=" + field.getDouble(null);
               }

               if (fieldType.isAssignableFrom(int.class)) {
                   output = fieldName + "=" + field.getInt(null);
               }

           } catch (IllegalAccessException exception) {
                telemetry.addData("Illegal access to field", fieldName);
           }

           if (output.isEmpty()) return;

           Writer writer = new FileWriter(CONSTANTS_FILE_LOCATION + CONSTANTS_FILE_NAME, true);
           BufferedWriter bufferedWriter = new BufferedWriter(writer);

           bufferedWriter.write(output);
           bufferedWriter.close();
       }

        /**
         * Saves all of the fields in one nested class to the properties file
         * @param clazz The nested class to save the fields of
         * @throws IOException Throws an IOException if the properties file cannot be written to
         */
       private static void saveNestedClassToConstantsFile(@NonNull Class<?> clazz) throws IOException {
           Field[] fields = clazz.getDeclaredFields();

           for (Field field : fields) {
               saveFieldToConstantsFile(field);
           }
       }

        /**
         * Saves all constants in the Constants class to a file in onbot java (Constants.txt)
         * @throws IOException Throws an IOException if Constants.txt cannot be written to.
         */
       public static void saveConstantsToFile() throws IOException {
           Class<?>[] classes = Constants.class.getDeclaredClasses();

           for (Class<?> clazz : classes) {
               saveNestedClassToConstantsFile(clazz);
           }
       }

        private static double getDoubleFromPropertiesFile(@NonNull String key) {
            return Double.parseDouble(properties.getProperty(key));
        }

        private static int getIntFromPropertiesFile(@NonNull String key) {
            return Integer.parseInt(properties.getProperty(key));
        }
    }
}
