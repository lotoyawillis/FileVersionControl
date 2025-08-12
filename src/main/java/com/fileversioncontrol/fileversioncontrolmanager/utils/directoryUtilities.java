package com.fileversioncontrol.fileversioncontrolmanager.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class directoryUtilities {
    public static boolean isDirectory(String pathString) {
        try {
            Path path = Paths.get(pathString);

            if (Files.exists(path)) {
                return Files.isDirectory(path);
            } else {
                return false;
            }
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }

    public static void createDirectory(String pathString, String directoryName) {
        File newDirectory = new File(pathString);

        if (newDirectory.mkdir()) {
            System.out.println("Directory \"" + directoryName + "\" was created successfully.");
        } else {
            System.out.println("Failed to create directory \"" + directoryName + "\" or it already exists.");
        }
    }
}
