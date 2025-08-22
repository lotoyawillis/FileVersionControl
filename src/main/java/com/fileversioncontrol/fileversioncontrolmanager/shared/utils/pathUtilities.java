package com.fileversioncontrol.fileversioncontrolmanager.shared.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class for obtaining and manipulating path strings.
 * <p>
 * This class provides static methods to:
 * <ul>
 *   <li>Ensure the directory path to a file exists</li>
 *   <li>Obtain all the directories in the first layer of a path</li>
 *   <li>Obtain all the files of a path</li>
 *   <li>Obtain all the paths in the first layer of a path</li>
 *   <li>Obtain the file or folder name of a path</li>
 *   <li>Build new paths by appending a segment to the end with the correct delimiter</li>
 *   <li>Determine the appropriate path delimiter (backslash or forward slash)</li>
 * </ul>
 * <p>
 * The methods are designed to work with all types of paths
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * String directoryPath = "C:\\Users\\Documents\\test";
 * String filePath = "C:\\Users\\Documents\\test\\testFile.txt";
 * List<String> filePathsList = new ArrayList<>();
 *
 * pathUtilities.createDirectoryPathIfItDoesNotExist(filePath); // Creates the directories "C:\\Users\\Documents" and
 *                                                              // "C:\\Users\\Documents\\test" if they do not exist
 *
 * List<String> allDirectoriesInFirstLayer = pathUtilities.getAllDirectoryPathsInOneLayer(directoryPath); // A
 *                                                                      // list of all directories in the first layer
 *                                                                      // of "C:\\Users\\Documents\\test"
 *
 * List<String> allFiles = pathUtilities.getAllFilePaths(directoryPath, filePathsList); // A list of all files
 *                                                                                  // in "C:\\Users\\Documents\\test"
 *
 * List<String> allPathsInFirstLayer = pathUtilities.getAllPathsInOneLayer(String directoryPath); // A list of
 *                                                                      // all files and directories in the first layer
 *                                                                      // of "C:\\Users\\Documents\\test"
 *
 * String directoryName = PathUtils.name(directoryPath); // "test"
 *
 * String newPath = PathUtils.pathBuilder(directoryPath, "newFile.txt"); // "C:\\Users\\Documents\\test\\newFile.txt"
 * }</pre>
 *
 * @author Lotoya Willis
 * @version 1.0
 */
public class pathUtilities {
    /**
     * Ensures the directory path to a file exists. If any directories do not exist, they are created.
     * <p>
     * The method determines the type of path of the path string, splits the path string, removes the file from
     * the string, constructs a minimum path string of the home directory and the first directory in the string,
     * and iteratively adds a directory name to the path, checks if that directory exists at that path, and creates it
     * if it does not.
     *
     * @param path the absolute path string to a file
     *
     * @see #splitCharacterHelper(String)
     * @see String#equals(Object)
     * @see #pathBuilder(String, String)
     * @see Arrays#asList(Object[])
     * @see ArrayList#remove(Object)
     * @see ArrayList#toArray(Object[])
     * @see directoryUtilities#isDirectory(String)
     * @see directoryUtilities#createDirectory(String, String)
     */
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

    /**
     * Returns a list of absolute path strings to all directories in the first layer of the inputted directory path
     * string.
     * <p>
     * The method checks for all directories one layer deep and returns a list of those directories. If the user does
     * not have permission to access the inputted directory or there are no directories in the first layer of the
     * inputted directory, it returns an empty list.
     *
     * @param directoryPath the absolute path to a directory
     * @return the list of all directories in the first layer of the inputted directory or an empty list if none are
     * found or an error occurs.
     *
     * @throws SecurityException if the user does not have permission to access the inputted directory
     *
     * @see java.io.File#listFiles(java.io.FileFilter)
     * @see java.util.Arrays#stream(Object[])
     * @see java.util.stream.Stream#map(java.util.function.Function)
     * @see java.util.stream.Stream#toList()
     */
    public static List<String> getAllDirectoryPathsInOneLayer(String directoryPath) {
        List<String> emptyList = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files;

        try {
            files = directory.listFiles(File::isDirectory);
        } catch (SecurityException e) {
            files = null;
        }

        if (files == null) {
            return emptyList;
        }
        return Arrays.stream(files).map(File::toString).toList();
    }

    /**
     * Returns a list of absolute path strings of all the files (not directories) contained within an inputted
     * directory.
     * <p></p>
     * The method calls {@link #getAllPathsInOneLayer(String)} to get all the absolute path strings of the
     * paths (files and directories) within the first layer of the inputted directory, iterates through those paths,
     * and checks if the path leads to a file or a directory. If the path is a directory, the method calls itself with
     * that path and the file path list as the input. If the path is a file, it is added to the list.
     *
     * @param originalDirectoryPath the path string of a directory
     * @param filePathsList the list of file path strings
     * @return the list of the absolute path strings of all the files in a directory
     *
     * @see #getAllPathsInOneLayer(String)
     * @see directoryUtilities#isDirectory(String)
     * @see #name(String)
     * @see String#equals(Object)
     * @see fileUtilities#isFile(String)
     * @see java.util.List#add(Object)
     */
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

    /**
     * Returns a list of all the absolute path strings of the paths (files and directories) in the first layer of
     * the inputted directory.
     * <p>
     * The method gets all the paths in the first layer of an inputted directory. If the user does not have permission
     * to access the inputted directory or there are no files in the directory, an empty list is returned. Otherwise,
     * the method returns the list of all the paths in the first layer
     *
     * @param directoryPath the inputted directory path string
     * @return the list of all the absolute path strings of the paths in the first layer of the inputted directory or
     * an empty list if there are no paths in the first layer or an error occurred
     *
     * @throws SecurityException if the user does not have permission to access the directory
     */
    public static List<String> getAllPathsInOneLayer(String directoryPath) {
        List<String> emptyList = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files;

        try {
            files = directory.listFiles();
        } catch (SecurityException e) {
            files = null;
        }

        if (files == null) {
            return emptyList;
        }
        return Arrays.stream(files).map(File::toString).toList();
    }

    /**
     * Returns the file (with the extension) or the directory name of the inputted path string
     * <p>
     * The method calls {@link #splitCharacterHelper(String)} to determine what delimiter the path uses, splits the
     * path, and returns the string at the last index of the split path string array.
     * 
     * @param path the path to a file or directory
     * @return the name of the file or directory that the path leads to
     * 
     * @see #splitCharacterHelper(String) 
     * @see String#equals(Object) 
     * @see String#split(String) 
     */
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

    /**
     * Returns a path string with the path segment appended to the end.
     * <p>
     * The method calls {@link #splitCharacterHelper(String)} to determine what delimiter the path uses and appends the
     * delimiter and {@code piece} to the inputted {@code path}.
     *
     * @param path the original path that the segment will be appended to
     * @param piece the path segment to append to the original path
     * @return the new path string created from the path segment
     *
     * @see #splitCharacterHelper(String)
     * @see java.lang.StringBuilder#append(String)
     * @see java.lang.StringBuilder#toString()
     */
    public static String pathBuilder(String path, String piece) {
        StringBuilder stringBuilder = new StringBuilder(path);
        String delimiter = splitCharacterHelper(path);
        stringBuilder.append(delimiter);
        stringBuilder.append(piece);
        return stringBuilder.toString();
    }

    /**
     * Returns the path delimiter that the path string uses.
     * <p></p>
     * If the path contains a backslash, it returns a backslash. Otherwise, it returns a forward slash.
     *
     * @param path the path string to check
     * @return the delimiter used in the path
     *
     * @see String#contains(CharSequence)
     */
    public static String splitCharacterHelper(String path) {
        if (path.contains("\\")) {
            return "\\";
        } else {
            return "/";
        }
    }
}
