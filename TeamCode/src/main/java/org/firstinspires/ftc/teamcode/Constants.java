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
import java.util.ArrayList;
import java.util.Properties;

/**
 * <h1>Constants Class</h1>
 *
 * <h2>Subsystem Nested Classes</h2>
 * <br>
 * <p>
 *     Each subsystem declared in the Subsystems package has a corresponding nested class that
 *     contains the related values. Due to the way that the constants loader is implemented, any
 *     constants values that are outside of a nested class will be ignored. This can be useful if
 *     you would like to have a constant that cannot be overwritten from onbot java such as the
 *     config file name of a hardware device.
 * </p>
 * <br>
 * <h2>Constants Loader</h2>
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
public class Constants {

    @Config
    public static class LauncherConstants {
        public static volatile double LAUNCH_POSITION = 0.6;
        public static volatile double ZERO_POSITION   = 0.0;
    }

    @Config
    public static class HangerConstants {
        public static volatile double HANG_POSITION = 1.0;
        public static volatile double ZERO_POSITION = 0.0;
    }

    @Config
    public static class IntakeConstants {
        public static volatile double INTAKE_POWER  = 1.0;
        public static volatile double OUTTAKE_POWER = 0.4;
    }

    @Config
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
        private static ArrayList<Properties> propertiesFiles = new ArrayList<>();

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

       private static File[] getConstantsFiles() throws IOException{
          File constantsDirectory = new File(CONSTANTS_FILE_LOCATION);

          if (constantsDirectory.isFile()) throw new IOException("Constants directory is a file");

          return constantsDirectory.listFiles();
       }

       @NonNull private static String parseFileName(@NonNull String fileName) {
           String[] wordsInFileName = fileName.split("(?<!^)(?=[A-Z])");

           String lastWordInFileName = wordsInFileName[wordsInFileName.length - 1];

           if (!lastWordInFileName.equals("Constants")) return "";

           return wordsInFileName[0];
       }

       @Nullable private static Class<?> matchConstantsClassToConstantsFile(@NonNull String fileIdentifier) {
          for (Class<?> clazz : ConstantsLoader.class.getDeclaredClasses()) {
              // Skip the constants loader
              if (clazz.getName().equals("ConstantsLoaderClass")) continue;

              if (clazz.getName().equals(fileIdentifier)) return clazz;
          }

          return null;
       }

       public static void populateClassFromPropertiesFile(@NonNull Class<?> clazz, @NonNull String fileName) throws IOException {
           Properties properties = new Properties();
           properties.load(new FileInputStream(CONSTANTS_FILE_LOCATION + fileName));

           Field[] fields = clazz.getDeclaredFields();

           for (Field field : fields) {

           }
       }



       public static void loadConstants() throws IOException {
            File[] constantsFiles = getConstantsFiles();

            for (File file : constantsFiles) {
                String fileName = file.getName();

                String fileIdentifier = parseFileName(fileName);

                // Skip the file if it does not end in "Constants"
                if (fileIdentifier.isEmpty()) continue;

                Class<?> constantsClass = matchConstantsClassToConstantsFile(fileIdentifier);

                // If there is no constants file that matches a constants class, continue.
                if (constantsClass == null) continue;



            }
       }






    }
}
