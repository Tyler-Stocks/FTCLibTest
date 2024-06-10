package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

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
public class ConstantsLoader {
    /*
     Environment.getExternalStorageDirectory() doesn't actually work so we suppress it with a
     lint.
     */
    @SuppressLint("SdCardPath")
    private static final String CONSTANTS_FILE_LOCATION
            = "/sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/Constants/";

    private final Telemetry telemetry;

    public ConstantsLoader(@NonNull Telemetry telemetry) {
        this.telemetry = telemetry;
    }


    /**
     * Gets a list of all of the files found in
     * /sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/Constants
     * @return An array containing all of the files found at the aforementioned location.
     * Returns null if none are found.
     * @throws IOException Throws an IOException if a file is found with the same name as the
     * constants directory.
     */
    @Nullable
    private File[] getConstantsFiles() throws IOException{
        File constantsDirectory = new File(CONSTANTS_FILE_LOCATION);

        if (constantsDirectory.isFile()) throw new IOException("Constants directory is a file");

        return constantsDirectory.listFiles();
    }

    @NonNull private String parseClassName(@NonNull String className) {
        int dollarSignIndex = className.lastIndexOf('$');

        return className.substring(dollarSignIndex + 1);
    }

    @NonNull private String stripFileExtension(@NonNull String fileName) {
        int dotIndex = fileName.lastIndexOf('.');

        return fileName.substring(0, dotIndex);
    }

    @Nullable private Class<?> matchConstantsClassToConstantsFile(@NonNull String fileName) {
        for (Class<?> clazz : Constants.class.getDeclaredClasses()) {
            if (!Modifier.isStatic(clazz.getModifiers())) continue;

            String className = parseClassName(clazz.getName());

            if (className.equals(fileName)) return clazz;
        }

        return null;
    }

    private void populateClassFromPropertiesFile(@NonNull Class<?> clazz, @NonNull String fileName) throws IOException {
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

            // Skip any variables we don't have access to.
            if (Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers)) continue;

            populateField(field, properties);
        }
    }

    /**
     * Populates a field with its value from the properties file. If a key matching the name of
     * the field is not found, the function will simply do nothing.
     * @param field The field to populate
     * @param properties The properties file to read from
     */
    private void populateField(@NonNull Field field, @NonNull Properties properties) {
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

    public void loadConstants() throws IOException {
        File[] constantsFiles = getConstantsFiles();

        if (constantsFiles == null) {
            telemetry.addLine("No constants files found.");
            return;
        }

        for (File file : constantsFiles) {
            String fileName = stripFileExtension(file.getName());

            Class<?> constantsClass = matchConstantsClassToConstantsFile(fileName);

            if (constantsClass == null) continue;

            populateClassFromPropertiesFile(constantsClass, fileName + ".txt");
        }
    }

    private double getDoubleFromPropertiesFile(@NonNull String key, @NonNull Properties properties) {
        return Double.parseDouble(properties.getProperty(key));
    }

    private int getIntFromPropertiesFile(@NonNull String key, @NonNull Properties properties) {
        return Integer.parseInt(properties.getProperty(key));
    }
}