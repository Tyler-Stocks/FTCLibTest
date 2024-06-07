package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

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

    public static class ConstantsLoader {
       private static final Properties properties = new Properties();

       private static final String CONSTANTS_FILE_LOCATION  = "/sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/";
       private static final String CONSTANTS_FILE_NAME      = "Constants.txt";

       private static Telemetry telemetry;

       public static void load(@NonNull Telemetry telemetry) {
           setTelemetry(telemetry);
           loadPropertiesFile();
           populateConstantsClass();
       }

       public static void setTelemetry(@NonNull Telemetry telemetry) {
           ConstantsLoader.telemetry = telemetry;
       }

       private static void loadPropertiesFile() {
            try {
                properties.clear();
                properties.load(new FileInputStream(CONSTANTS_FILE_LOCATION + CONSTANTS_FILE_NAME));
            } catch (Exception exception) {
                telemetry.addData("Exception in loading properties", exception.getMessage());
           }
       }

       private static void populateField(@NonNull Field field) {
           String fieldName   = field.getName();
           Class<?> fieldType = field.getType();

           try {
               if (!properties.containsKey(fieldName)) return;

              if (fieldType.isAssignableFrom(double.class)) {
                  field.setDouble(fieldName, getDoubleFromPropertiesFile(fieldName));
              }

              if (fieldType.isAssignableFrom(int.class)) {
                  field.setInt(fieldName, getIntFromPropertiesFile(fieldName));
              }
          } catch (Exception exception) {
              telemetry.addData("Exception in populating field", exception.getMessage());
           }
       }

       private static void populateClass(@NonNull Class<?> clazz) {
           Field[] fields = clazz.getDeclaredFields();

           for (Field field : fields) {
               if (!Modifier.isStatic(field.getModifiers())) continue;

               populateField(field);
           }
       }

       private static void populateConstantsClass() {
           Class<?>[] classes = Constants.class.getDeclaredClasses();

           for (Class<?> clazz : classes) {
                // Skip the constants loader class
                if (clazz.getName().equals("ConstantsLoader")) continue;

                populateClass(clazz);
            }
       }

       private static void saveFieldToConstantsFile(@NonNull Field field) {
           try {
               FileWriter writer = new FileWriter(CONSTANTS_FILE_LOCATION + CONSTANTS_FILE_NAME);
           } catch (Exception exception) {
               telemetry.addData("Exception in saving field to constants file", exception.getMessage());
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
