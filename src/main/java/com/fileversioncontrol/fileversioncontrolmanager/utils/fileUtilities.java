package com.fileversioncontrol.fileversioncontrolmanager.utils;

import java.io.File;
import java.nio.file.InvalidPathException;

public class fileUtilities {
    public static boolean isFile(String pathString) {
        try {
            File file = new File(pathString);

            if (file.exists()) {
                return file.isFile();
            } else {
                return false;
            }
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }
}
