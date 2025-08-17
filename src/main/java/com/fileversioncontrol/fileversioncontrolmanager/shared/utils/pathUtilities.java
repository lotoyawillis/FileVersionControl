package com.fileversioncontrol.fileversioncontrolmanager.shared.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class pathUtilities {
    public static void createDirectoryPathIfItDoesNotExist(String path) {
        String[] splitPath;
        String partialPath;

        String delimiter = splitCharacterHelper(path);
        if (delimiter.equals("\\")) {
            splitPath = path.split("\\\\");
            partialPath = pathBuilder(splitPath[0], splitPath[1]);
        } else {
            splitPath = path.split("/");
            partialPath = pathBuilder(splitPath[0], splitPath[1]);
        }

        ArrayList<String> list = new ArrayList<>(Arrays.asList(splitPath));
        list.remove(splitPath[0]);
        list.remove(splitPath[1]);
        list.remove(splitPath[splitPath.length - 1]); // Erase the filename from the path

        splitPath = list.toArray(new String[0]);

        for (String piece : splitPath) {
            partialPath = pathBuilder(partialPath, piece);
            if (!directoryUtilities.isDirectory(partialPath)) {
                directoryUtilities.createDirectory(partialPath, name(partialPath));
            }
        }
    }

    public static List<String> getAllDirectoryPathsInOneLayer(String directoryPath) {
        List<String> emptyList = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files;

        try {
            files = directory.listFiles(File::isDirectory);
        } catch (SecurityException e) {
            files = null;
        }

        if (files != null) {
            try {
                return Arrays.stream(files).map(File::toString).toList();
            } catch (Exception e) {
                return emptyList;
            }
        }
        return emptyList;
    }

    public static List<String> getAllFilePaths(String originalDirectoryPath, List<String> filePathsList) {
        List<String> allPaths = getAllPathsInOneLayer(originalDirectoryPath);

        for (String path : allPaths) {
            if (directoryUtilities.isDirectory(path)) {
                String directoryName = name(path);

                if (!directoryName.equals(".vc")) {
                    getAllFilePaths(path, filePathsList);
                }
            } else if (fileUtilities.isFile(path)) {
                filePathsList.add(path);
            }
        }
        return filePathsList;
    }

    public static List<String> getAllPathsInOneLayer(String directoryPath) {
        List<String> emptyList = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files;

        try {
            files = directory.listFiles();
        } catch (SecurityException e) {
            files = null;
        }

        if (files != null) {
            try {
                return Arrays.stream(files).map(File::toString).toList();
            } catch (Exception e) {
                return emptyList;
            }
        }
        return emptyList;
    }

    public static String name(String path) {
        String delimiter = splitCharacterHelper(path);
        if (delimiter.equals("\\")) {
            String[] splitBackSlashPath = path.split("\\\\");
            return splitBackSlashPath[splitBackSlashPath.length - 1];
        } else {
            String[] splitForwardSlashPath = path.split("/");
            return splitForwardSlashPath[splitForwardSlashPath.length - 1];
        }
    }

    public static String pathBuilder(String path, String piece) {
        StringBuilder stringBuilder = new StringBuilder(path);
        String delimiter = splitCharacterHelper(path);
        stringBuilder.append(delimiter);
        stringBuilder.append(piece);
        return stringBuilder.toString();
    }

    public static String splitCharacterHelper(String path) {
        if (path.contains("\\")) {
            return "\\";
        } else {
            return "/";
        }
    }
}
