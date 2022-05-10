package ru.kgogolev.app.util;

import lombok.experimental.UtilityClass;
import ru.kgogolev.app.entity.MediaTypes;

import java.io.File;

@UtilityClass
public class FileUtils {

    private static final String FILE_EXTENSION = ".";

    public static MediaTypes getFileMediaType(String filename) {
        var extension = getFileExtension(filename);
        for (MediaTypes type : MediaTypes.values()) {
            var types = type.getExtensions();
            if (types == null) {
                continue;
            }
            for (String s : types) {
                if (extension.equalsIgnoreCase(s)) {
                    return type;
                }
            }
        }
        return MediaTypes.FILE;
    }


    private static String getFileExtension(String fileName) {

        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null!");
        }

        var extension = "";
        var indexOfLastExtension = fileName.lastIndexOf(FILE_EXTENSION);
        var indexOfLastSeparator = fileName.lastIndexOf(File.separatorChar);
        if (indexOfLastExtension > indexOfLastSeparator) {
            extension = fileName.substring(indexOfLastExtension + 1);
        }

        return extension;

    }

}
