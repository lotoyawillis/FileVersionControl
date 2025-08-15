package com.fileversioncontrol.fileversioncontrolmanager.utils;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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

    public static boolean isFileChangedForCommit(Map.Entry<Integer, File> vcEntry, HashMap<Integer, File> currentDirectoryHashMap) {
        if (currentDirectoryHashMap.containsKey(vcEntry.getKey())) {
            String currentFilePath = currentDirectoryHashMap.get(vcEntry.getKey()).getAbsolutePath();
            String vcFilePath = vcEntry.getValue().getAbsolutePath();

            return isFileContentChanged(currentFilePath, vcFilePath);
        } else {
            String vcFilePathString = vcEntry.getValue().getAbsolutePath();
            String match = getVCFileSavedPath(vcFilePathString);

            if (!match.isEmpty()) {
                for (Map.Entry<Integer, File> cdEntry : currentDirectoryHashMap.entrySet()) {
                    String currentFilePathString = cdEntry.getValue().getAbsolutePath();
                    if (currentFilePathString.contains(match)) {
                        return isFileContentChanged(currentFilePathString, vcFilePathString);
                    }
                }
            }
            return true;
        }
    }

    public static boolean isFileChangedForRestore(Map.Entry<Integer, File> vcEntry, HashMap<Integer, File> currentDirectoryHashMap, String destinationPathString) {
        if (currentDirectoryHashMap.containsKey(vcEntry.getKey())) {
            String currentFilePath = currentDirectoryHashMap.get(vcEntry.getKey()).getAbsolutePath();
            String vcFilePath = vcEntry.getValue().getAbsolutePath();

            return isFileContentChanged(currentFilePath, vcFilePath);
        } else {
            String vcFilePathString = vcEntry.getValue().getAbsolutePath();
            String match = getVCFileSavedPath(vcFilePathString);

            if (!match.isEmpty()) {
                Pattern destinationPattern;
                String delimiter = pathUtilities.splitCharacterHelper(destinationPathString);

                if (delimiter.equals("\\")) {
                    destinationPathString = destinationPathString.replace("\\", "\\\\");
                    String regexSafeMatch = match.replace("\\", "\\\\");
                    destinationPattern = Pattern.compile(String.format("^%s\\\\%s$", destinationPathString, regexSafeMatch));
                } else {
                    destinationPattern = Pattern.compile(String.format("^%s/%s$", destinationPathString, match));
                }

                for (Map.Entry<Integer, File> cdEntry : currentDirectoryHashMap.entrySet()) {
                    String currentFilePathString = cdEntry.getValue().getAbsolutePath();

                    Matcher destinationMatcher = destinationPattern.matcher(currentFilePathString);

                    if (destinationMatcher.find() && currentFilePathString.contains(match)) {
                        return isFileContentChanged(currentFilePathString, vcFilePathString);
                    }
                }
            }
            return true;
        }
    }

    public static boolean isFileContentChanged(String currentFilePath, String vcFilePath) {
        try {
            String currentFileHash = hashUtilities.hashFile(currentFilePath);
            String vcFileHash = hashUtilities.hashFile(vcFilePath);
            return !currentFileHash.equals(vcFileHash);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;                                       // Do not commit or restore file if it cannot be hashed
        }
    }

    public static String getVCFileSavedPath(String vcFilePathString) {
        Pattern pattern;
        String delimiter = pathUtilities.splitCharacterHelper(vcFilePathString);
        if (delimiter.equals("\\")) {
            pattern = Pattern.compile(".*\\\\\\.vc\\\\\\d+\\\\(.*)");
        } else {
            pattern = Pattern.compile(".*/.vc/\\d+/(.*)");
        }

        Matcher matcher = pattern.matcher(vcFilePathString);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }
}
