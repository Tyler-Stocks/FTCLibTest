package org.firstinspires.ftc.teamcode.Constants;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
 *     The constants loader class attempts to override the values located in the
 *     {@link org.firstinspires.ftc.teamcode.Constants.Constants} class with ones that are found in
 *     the corresponding file in Onbot java. To better organize the constants, the Constants class
 *     is broken up into nested classes. Each one of these nested classes can have a corresponding
 *     file in the Constants folder of the teamcode directory. There is no need to hardcode in the
 *     names of the files, simply ensure that the name of the file in onbot java is the same as the
 *     name of the nested classes, and ConstantsLoader will dynamically assign the class the
 *     correct file. Additionally, the following conditions will cause a file to be ignored.
 *     <ul>
 *         <li>The file is not a txt file</li>
 *         <li>The file name is not suffixed with the word "Constants"</li>
 *         <li>The file does not have a corresponding nested class within Constants</li>
 *     </ul>
 * </p>
 * <br>
 * <p>
 *     Any fields that are present in the constants file, but not the corresponding nested constants
 *     class will be silently ignored. This means that typos in onbot java may have unexpected
 *     behaviour, but will not cause the program to crash. Additionally, any fields in the nested
 *     classes that are marked as final, private, or protected will be ignored to prevent an
 *     {@link java.lang.IllegalAccessException}.
 * </p>
 */
public class ConstantsLoader {
    @SuppressLint("SdCardPath")
    private static final String CONSTANTS_FILE_LOCATION
            = "/sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/Constants/";

    public ConstantsLoader() {}

    @Nullable private File[] getConstantsFilesFromOnbotJava() throws IOException {
        File constantsDirectory = new File(CONSTANTS_FILE_LOCATION);

        if (constantsDirectory.isFile()) throw new IOException("Constants directory is a file");

        return constantsDirectory.listFiles();
    }

    @Nullable private Class<?> matchConstantsClassToConstantsFile(@NonNull String fileName) {
        for (Class<?> clazz : Constants.class.getDeclaredClasses()) {
            if (!Modifier.isStatic(clazz.getModifiers())) continue;

            if (clazz.getSimpleName().equals(fileName)) return clazz;
        }

        return null;
    }

    private void populateClassFromPropertiesFile(@NonNull Class<?> clazz, @NonNull String fileName) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(CONSTANTS_FILE_LOCATION + fileName));

        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            int modifiers = field.getModifiers();

            if (!Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) continue;

            populateField(field, properties);
        }
    }

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
        } catch (IllegalAccessException ignored) {}
    }

    /**
     * Loads all of the constants with the constants found in Onbot Java under the Constants
     * directory
     * @throws IOException Throws an IOException if there is an issue reading any of the constants
     * files.
     */
    public void loadConstants() throws IOException {
        File[] constantsFiles = getConstantsFilesFromOnbotJava();

        if (constantsFiles == null) return;

        for (File file : constantsFiles) {
            if (!isTextFile(file)) continue;

            String fileName = file.getName();

            Class<?> constantsClass
                    = matchConstantsClassToConstantsFile(stripFileExtension(fileName));

            if (constantsClass == null) continue;

            populateClassFromPropertiesFile(constantsClass, fileName);
        }
    }

    private boolean isTextFile(@NonNull File file) {
        String fileName = file.getName();

        int dotPosition = fileName.lastIndexOf('.');

        return fileName.substring(dotPosition + 1).equals("txt");
    }

    @NonNull private String stripFileExtension(@NonNull String fileName) {
        int dotIndex = fileName.lastIndexOf('.');

        return fileName.substring(0, dotIndex);
    }

    private double getDoubleFromPropertiesFile(@NonNull String key, @NonNull Properties properties) {
        return Double.parseDouble(properties.getProperty(key));
    }

    private int getIntFromPropertiesFile(@NonNull String key, @NonNull Properties properties) {
        return Integer.parseInt(properties.getProperty(key));
    }
}