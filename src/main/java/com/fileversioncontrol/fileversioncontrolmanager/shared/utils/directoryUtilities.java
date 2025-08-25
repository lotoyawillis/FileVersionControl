package com.fileversioncontrol.fileversioncontrolmanager.shared.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for creating a directory, verifying if a path is a version control directory, if a path is a
 * directory, if a directory is up-to-date or has changed, and obtaining the latest version control directory of
 * a directory.
 * <p>
 * This class provides static methods to:
 * <ul>
 *   <li>Create a directory at a path</li>
 *   <li>Check if a path is a version control directory</li>
 *   <li>Check if a path is a directory</li>
 *   <li>Check if a directory is up to date</li>
 *   <li>Check if a directory has changed</li>
 *   <li>Obtain the latest version control directory</li>
 * </ul>
 * <p>
 * The methods are designed to work with all types of paths
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * String newDirectoryPath = "C:\\Users\\Documents\\test\\newDirectory";
 * String vcDirectoryPath = "C:\\Users\\Documents\\test\\.vc\\1";
 * String directoryPath = "C:\\Users\\Documents\\test";
 * HashMap<Integer, File> directoryHashMap = hashUtilities.createHashMap(directoryPath);
 * HashMap<Integer, File> vcDirectoryHashMap = hashUtilities.createHashMap(vcDirectoryPath);
 *
 * directoryUtilities.createDirectory(newDirectoryPath); // creates the directory
 *                                                       // "C:\\Users\\Documents\\test\\newDirectory"
 *
 * boolean isVCDirectoryPathAVCDirectory = directoryUtilities.isAVersionControlNumberDirectory(vcDirectoryPath); // true
 *
 * boolean isDirectoryPathADirectory = directoryUtilities.isDirectory(directoryPath); // true
 *
 * boolean isDirectoryPathUpToDate = directoryUtilities.isDirectoryUpToDate(directoryPath); // true if the directory has
 *                                                                                  // not changed; Otherwise, false
 *
 * boolean isDirectoryPathChanged = directoryUtilities.isDirectoryChanged(directoryHashMap, vcDirectoryHashMap);
 * // true if a file in the directoryHashMap has changed compared to the vcDirectoryHashMap; Otherwise, false
 *
 * String latestVersionControlDirectory = directoryUtilities.getLatestVersionNumberDirectory(directoryPath);
 * // returns the path string for the directory with the latest creation date or an empty string if no directories exist
 * // or an error occurs
 * }</pre>
 *
 * @author Lotoya Willis
 * @version 1.0
 */
public class directoryUtilities {
    /**
     * Attempts to create a directory from a given path and a directory name.
     * <p>
     * If a directory is created successfully, a success message is printed to the console.
     * If a directory fails to be created, or it already exists, an error message is printed to the console.
     * If a user does not have permission to create a directory, a permission error message is printed to the console.
     *
     * @param pathString the path to the created directory
     *
     * @throws SecurityException if the user does not have permission to create a directory
     * 
     * @see pathUtilities#name(String) 
     * @see File#mkdir()
     */
    public static void createDirectory(String pathString) {
        File newDirectory = new File(pathString);
        String directoryName = pathUtilities.name(pathString);

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

    /**
     * Determines if the path string leads to a valid version control directory
     * <p>
     * The path string is matched against its expected version control directory format:
     * <pre>
     *     [root_path]/[directory]/.vc/[version_number]
     * </pre>
     * or
     * <pre>
     *     [root_path]\\[directory]\\.vc\\[version_number]
     * </pre>
     * The method determines the delimiter that the path string uses, matches the path against
     * its associated regex, and checks if it is a directory
     *
     * @param pathString the path to a version control directory
     * @return {@code true} if the path string matches the expected version control directory format;
     *         {@code false} otherwise
     *
     * @see pathUtilities#splitCharacterHelper(String)
     * @see java.util.regex.Pattern#compile(String)
     * @see java.util.regex.Pattern#matcher(CharSequence)
     * @see Matcher#find()
     * @see #isDirectory(String)
     */
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

    /**
     * Determines if the path string is a directory
     * <p>
     * The method handles null or invalid paths. It returns {@code true} if the path string is a directory.
     *
     * @param pathString the path string to be checked
     * @return {@code true} if the path string leads to a directory;
     *         {@code false} otherwise.
     *
     * @throws SecurityException if the user does not have permission to access the directory
     * @throws NullPointerException if the path string is null and attempts to be converted to a Path object.
     *
     * @see File#isDirectory()
     */
    public static boolean isDirectory(String pathString) {
        try {
            File directory = new File(pathString);

            return directory.isDirectory();
        } catch (SecurityException | NullPointerException ex) {
            return false;
        }
    }

    /**
     * Determines if a directory has been unchanged since the last commit.
     * <p>
     * The method verifies if the directory that the path string leads to contains all the files contained
     * within the latest version control directory and that the contents of those files have not changed.
     *
     * @param path the path string of the directory that is being checked
     * @return {@code true} if the directory has not changed since the last commit;
     *          {@code false} otherwise.
     *
     * @see hashUtilities#createHashMap(String)
     * @see #getLatestVersionNumberDirectory(String)
     * @see #isDirectoryChanged(HashMap, HashMap)
     */
    public static boolean isDirectoryUpToDate(String path) {
        HashMap<Integer, File> currentDirectoryHashMap = hashUtilities.createHashMap(path);

        String latestVCPath = getLatestVersionNumberDirectory(path);
        if (!latestVCPath.isEmpty()) {
            HashMap<Integer, File> latestVersionControlHashMap = hashUtilities.createHashMap(latestVCPath);

            if (currentDirectoryHashMap.size() != latestVersionControlHashMap.size()) {
                return false;
            } else {
                return !isDirectoryChanged(currentDirectoryHashMap, latestVersionControlHashMap);
            }
        }
        return false;
    }

    /**
     * Determines if any files in the inputted directory have been changed since the last commit
     * <p>
     * The method loops through the hash map created from the files in the inputted directory and calls the
     * fileUtilities.isFileChangedForCommit method for each item
     *
     * @param currentDirectoryHashMap the hash map created from the path of the directory inputted when commit is requested
     * @param vcDirectoryHashMap the hash map created from the path of the latest version control directory
     * @return {@code true} if at least one file has changed;
     *          {@code false} otherwise.
     *
     * @see fileUtilities#isFileChangedForCommit(Map.Entry, HashMap)
     */
    public static boolean isDirectoryChanged(HashMap<Integer, File> currentDirectoryHashMap, HashMap<Integer, File> vcDirectoryHashMap) {
        for (Map.Entry<Integer, File> vcEntry : vcDirectoryHashMap.entrySet()) {
            boolean isFileChanged = fileUtilities.isFileChangedForCommit(vcEntry, currentDirectoryHashMap);
            if (isFileChanged) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds and returns the most recently created version control directory under the {@code .vc} directory.
     * <p>
     * The method builds the path to the {@code .vc} directory using {@link pathUtilities#pathBuilder(String, String)},
     * checks if the directory exists, then loops through the directories in its first layer. If successful, it returns
     * the directory path that has the latest creation time; Otherwise, it returns an empty string
     *
     * @param path the path string of the directory inputted when commit is requested
     * @return the path string of the latest version control directory or an empty string if not found
     *
     * @throws UnsupportedOperationException if the class given to Files.readAttributes is not supported
     * @throws SecurityException if the user does not have permission to read the attributes of a directory
     *
     * @see pathUtilities#pathBuilder(String, String)
     * @see #isDirectory(String)
     * @see pathUtilities#getAllDirectoryPathsInOneLayer(String)
     * @see java.nio.file.attribute.FileTime#fromMillis(long)
     * @see java.nio.file.Paths#get(String, String...)
     * @see java.nio.file.Files#readAttributes(java.nio.file.Path, Class, java.nio.file.LinkOption...)
     * @see java.nio.file.attribute.FileTime#compareTo(FileTime)
     * @see BasicFileAttributes#creationTime()
     */
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
