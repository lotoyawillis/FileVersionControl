package com.fileversioncontrol.fileversioncontrolmanager.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class pathUtilities {
    public static List<String> getAllDirectoryPathsInOneLayer(String directoryPath) {
        List<String> emptyList = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files;

        try {
            files = directory.listFiles(File::isDirectory);
        }
        catch (SecurityException e) {
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
                // String[] splitPath = path.split("\\\\");
                // String directoryName = splitPath[splitPath.length - 1];
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
        }
        catch (SecurityException e) {
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
        }
        else {
            String[] splitForwardSlashPath = path.split("\\/");
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
        //String[] splitBackSlashPath = path.split("\\\\");
        // String[] splitForwardSlashPath = path.split("\\/");
        // int index = path.indexOf("\\");

        if (path.contains("\\")) {
            return "\\";
        }
        else {
            return "/";
        }
    }
}
