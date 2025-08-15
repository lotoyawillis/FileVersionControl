package com.fileversioncontrol.fileversioncontrolmanager.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class directoryUtilities {
    public static void createDirectory(String pathString, String directoryName) {
        File newDirectory = new File(pathString);

        try {
            if (newDirectory.mkdir()) {
                System.out.println("Directory \"" + directoryName + "\" was created successfully.");
            } else {
                System.out.println("Failed to create directory \"" + directoryName + "\" or it already exists.");
            }
        } catch (SecurityException e) {
            System.out.println("You do not have permission to create directory \"" + directoryName + "\"");
        }
    }

    public static boolean isAVersionControlNumberDirectory(String pathString) {
        Pattern pattern;

        String delimiter = pathUtilities.splitCharacterHelper(pathString);
        if (delimiter.equals("\\")) {
            pattern = Pattern.compile("^[^\\\\].*\\\\[^\\\\]+\\\\.vc\\\\\\d+\\\\?$");
        } else {
            pattern = Pattern.compile("^[^/].*/[^/]+/.vc/\\d+/?$");
        }

        Matcher matcher = pattern.matcher(pathString);
        if (matcher.find()) {
            return isDirectory(pathString);
        } else {
            return false;
        }
    }

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

    public static boolean isDirectoryUpToDate(String path) {
        HashMap<Integer, File> currentFiles = new HashMap<>();
        HashMap<Integer, File> currentDirectoryHashMap = hashUtilities.createHashMap(path, currentFiles);

        HashMap<Integer, File> lastSavedFiles = new HashMap<>();
        String latestVCPath = getLatestVersionNumberDirectory(path);
        if (!latestVCPath.isEmpty()) {
            HashMap<Integer, File> latestVersionControlHashMap = hashUtilities.createHashMap(latestVCPath, lastSavedFiles);

            if (currentDirectoryHashMap.size() != latestVersionControlHashMap.size()) {
                return false;
            } else {
                return !isDirectoryChanged(currentDirectoryHashMap, latestVersionControlHashMap);
            }
        }
        return false;
    }

    public static boolean isDirectoryChanged(HashMap<Integer, File> currentDirectoryHashMap, HashMap<Integer, File> vcDirectoryHashMap) {
        for (Map.Entry<Integer, File> vcEntry : vcDirectoryHashMap.entrySet()) {
            boolean isFileChanged = fileUtilities.isFileChangedForCommit(vcEntry, currentDirectoryHashMap);
            if (isFileChanged) {
                return true;
            }
        }
        return false;
    }


    public static String getLatestVersionNumberDirectory(String path) {
        String vcPath = pathUtilities.pathBuilder(path, ".vc");
        if (isDirectory(vcPath)) {
            List<String> directories = pathUtilities.getAllDirectoryPathsInOneLayer(vcPath);
            FileTime latestCreationTime = FileTime.fromMillis(0);
            String latestDirectory = "";

            for (String directory : directories) {
                Path filePath = Paths.get(directory);
                try {
                    BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                    if (latestCreationTime.compareTo(attributes.creationTime()) < 0) {
                        latestCreationTime = attributes.creationTime();
                        latestDirectory = directory;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Could not read directory attributes");
                    return "";
                }
            }
            return latestDirectory;
        }

        return "";
    }
}
