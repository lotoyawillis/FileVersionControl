package com.fileversioncontrol.fileversioncontrolmanager.shared.utils;

import java.io.File;
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
 * HashMap<Integer, File> directoryHashMap = HashUtilities.createHashMap(directoryPath);
 * HashMap<Integer, File> vcDirectoryHashMap = HashUtilities.createHashMap(vcDirectoryPath);
 *
 * DirectoryUtilities.createDirectory(newDirectoryPath); // creates the directory
 *                                                       // "C:\\Users\\Documents\\test\\newDirectory"
 *
 * boolean isVCDirectoryPathAVCDirectory = DirectoryUtilities.isAVersionControlNumberDirectory(vcDirectoryPath); // true
 *
 * boolean isDirectoryPathADirectory = DirectoryUtilities.isDirectory(directoryPath); // true
 *
 * boolean isDirectoryPathUpToDate = DirectoryUtilities.isDirectoryUpToDate(directoryPath); // true if the directory has
 *                                                                                  // not changed; Otherwise, false
 *
 * boolean isDirectoryPathChanged = DirectoryUtilities.isDirectoryChanged(directoryHashMap, vcDirectoryHashMap);
 * // true if a file in the directoryHashMap has changed compared to the vcDirectoryHashMap; Otherwise, false
 *
 * String latestVersionControlDirectory = DirectoryUtilities.getLatestVersionNumberDirectory(directoryPath);
 * // returns the path string for the directory with the latest creation date or an empty string if no directories exist
 * // or an error occurs
 * }</pre>
 *
 * @author Lotoya Willis
 * @version 1.0
 */
public class DirectoryUtilities {
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
     * @see PathUtilities#name(String)
     * @see File#mkdir()
     */
    public static void createDirectory(String pathString) {
        File newDirectory = new File(pathString);
        String directoryName = PathUtilities.name(pathString);

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
     * @see PathUtilities#splitCharacterHelper(String)
     * @see java.util.regex.Pattern#compile(String)
     * @see java.util.regex.Pattern#matcher(CharSequence)
     * @see Matcher#find()
     * @see #isDirectory(String)
     */
    public static boolean isAVersionControlNumberDirectory(String pathString) {
        Pattern pattern;

        String delimiter = PathUtilities.splitCharacterHelper(pathString);
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
     * @see HashUtilities#createHashMap(String)
     * @see #getLatestVersionNumberDirectory(String)
     * @see #isDirectoryChanged(HashMap, HashMap)
     */
    public static boolean isDirectoryUpToDate(String path) {
        HashMap<Integer, File> currentDirectoryHashMap = HashUtilities.createHashMap(path);

        String latestVCPath = getLatestVersionNumberDirectory(path);
        if (!latestVCPath.isEmpty()) {
            HashMap<Integer, File> latestVersionControlHashMap = HashUtilities.createHashMap(latestVCPath);

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
     * FileUtilities.isFileChangedForCommit method for each item
     *
     * @param currentDirectoryHashMap the hash map created from the path of the directory inputted when commit is requested
     * @param vcDirectoryHashMap the hash map created from the path of the latest version control directory
     * @return {@code true} if at least one file has changed;
     *          {@code false} otherwise.
     *
     * @see FileUtilities#isFileChangedForCommit(Map.Entry, HashMap)
     */
    public static boolean isDirectoryChanged(HashMap<Integer, File> currentDirectoryHashMap, HashMap<Integer, File> vcDirectoryHashMap) {
        for (Map.Entry<Integer, File> vcEntry : vcDirectoryHashMap.entrySet()) {
            boolean isFileChanged = FileUtilities.isFileChangedForCommit(vcEntry, currentDirectoryHashMap);
            if (isFileChanged) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds and returns the highest version numbered version control directory under the {@code .vc} directory.
     * <p>
     * The method builds the path to the {@code .vc} directory using {@link PathUtilities#pathBuilder(String, String)},
     * checks if the directory exists, then loops through the directories in its first layer. If successful, it returns
     * the directory path that has the latest version number; Otherwise, it returns an empty string
     *
     * @param path the path string of the directory inputted when commit is requested
     * @return the path string of the latest version control directory or an empty string if not found
     *
     * @throws java.util.regex.PatternSyntaxException if the pattern being compiled is not a valid regex expression
     * @throws NumberFormatException if the string being parsed does not contain a parsable integer
     *
     * @see PathUtilities#pathBuilder(String, String)
     * @see #isDirectory(String)
     * @see PathUtilities#getAllDirectoryPathsInOneLayer(String)
     * @see PathUtilities#splitCharacterHelper(String)
     * @see java.util.regex.Pattern#compile(String)
     * @see java.util.regex.Pattern#matcher(CharSequence)
     * @see Matcher#find()
     * @see Integer#parseInt(String)
     * @see Matcher#group(int)
     */
    public static String getLatestVersionNumberDirectory(String path) {
        String vcPath = PathUtilities.pathBuilder(path, ".vc");
        if (isDirectory(vcPath)) {
            List<String> directories = PathUtilities.getAllDirectoryPathsInOneLayer(vcPath);
            String latestDirectoryNumber = "0";
            String latestDirectory = "";

            for (String directory : directories) {
                Pattern pattern;
                String delimiter = PathUtilities.splitCharacterHelper(directory);
                if (delimiter.equals("\\")) {
                    pattern = Pattern.compile("\\\\\\.vc\\\\(\\d+)\\\\?");
                } else {
                    pattern = Pattern.compile("/.vc/(\\d+)/?");
                }

                Matcher matcher = pattern.matcher(directory);
                if (matcher.find() && Integer.parseInt(matcher.group(1)) > Integer.parseInt(latestDirectoryNumber)) {
                    latestDirectoryNumber = matcher.group(1);
                    latestDirectory = directory;
                }
            }
            return latestDirectory;
        }

        return "";
    }
}
