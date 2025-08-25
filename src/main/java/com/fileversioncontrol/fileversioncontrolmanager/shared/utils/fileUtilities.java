package com.fileversioncontrol.fileversioncontrolmanager.shared.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for verifying if a file exists or has changed and obtaining its saved path.
 * <p>
 * This class provides static methods to:
 * <ul>
 *   <li>Check if a file exists at a path</li>
 *   <li>Check if a file changed (for the commit option)</li>
 *   <li>Check if a file changed (for the restore option)</li>
 *   <li>Check if a file's content changed</li>
 *   <li>Obtain a version control file's saved path</li>
 * </ul>
 * <p>
 * The methods are designed to work with all types of paths
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * String directoryPath = "C:\\Users\\Documents\\test";
 * String filePath = "C:\\Users\\Documents\\test\\testFile.txt";
 * String vcDirectoryPath = "C:\\Users\\Documents\\test\\.vc\\1";
 * String vcFilePath = "C:\\Users\\Documents\\test\\.vc\\1\\testFile.txt";
 * HashMap<Integer, File> destinationHashMap = hashUtilities.createHashMap(directoryPath);
 * Map.Entry<Integer, File> vcEntry = new AbstractMap.SimpleEntry<>(1, new File(vcFilePath));
 *
 * boolean isFilePathAFile = fileUtilities.isFile(filePath); // true
 *
 * boolean isFilePathChanged = fileUtilities.isFileChangedForCommit(filePath); // true if the file at filePath changed;
 *                                                                          // Otherwise, false
 *
 * boolean isFilePathChangedComparedToDestination = fileUtilities.isFileChangedForRestore(vcEntry, destinationHashMap);
 *                                                  // true if the file has changed compared to its last saved version;
 *                                                  // Otherwise, false
 *
 * boolean isFilePathContentChanged = fileUtilities.isFileContentChanged(filePath, vcFilePath); // true if the file's
 *                                      // content has changed compared to its last saved version; Otherwise, false
 *
 * String vcFileSavedPath = fileUtilities.getVCFileSavedPath(vcFilePath); // "C:\\Users\\Documents\\test\\testFile.txt"
 * }</pre>
 *
 * @author Lotoya Willis
 * @version 1.0
 */
public class fileUtilities {
    /**
     * Checks if the inputted path string leads to a file.
     * <p>
     * The method returns {@code true} if the path exists and is a file
     * (not a directory); Otherwise, it returns {@code false}
     *
     * @param pathString the path string to check
     * @return {@code true} if the path exists and is a file;
     *          {@code false} otherwise
     *
     * @throws SecurityException if the user does not have permission to access the file
     * @throws NullPointerException if the inputted path string is null
     *
     * @see File#isFile()
     */
    public static boolean isFile(String pathString) {
        try {
            File file = new File(pathString);

            return file.isFile();
        } catch (SecurityException | NullPointerException ex) {
            return false;
        }
    }

    /**
     * Determines whether the given file from the version control directory has changed compared to the given directory.
     * <p>
     * The method calls {@link #isFileContentChanged(String, String)} if a file with the same key exists in the
     * inputted directory. If not, it returns {@code true}
     *
     * @param vcEntry the hash map entry for a file in the version control directory
     * @param currentDirectoryHashMap the hash map created from the inputted directory path
     * @return {@code true} if the file has changed;
     *          {@code false} otherwise
     *
     * @see #isFileContentChanged(String, String)
     * @see #getVCFileSavedPath(String)
     * @see String#contains(CharSequence)
     */
    public static boolean isFileChangedForCommit(Map.Entry<Integer, File> vcEntry, HashMap<Integer, File> currentDirectoryHashMap) {
        if (currentDirectoryHashMap.containsKey(vcEntry.getKey())) {
            String currentFilePath = currentDirectoryHashMap.get(vcEntry.getKey()).getAbsolutePath();
            String vcFilePath = vcEntry.getValue().getAbsolutePath();

            return isFileContentChanged(currentFilePath, vcFilePath);
        }

        return true;
    }

    /**
     * Determines whether a file from version control has changed with respect to the target restore destination.
     * <p>
     * The method calls {@link #isFileContentChanged(String, String)} if a file with the same key exists in the current
     * directory. If not, it attempts to find a file in the restore destination directory with the same relative path as
     * the version control directory file.
     * If not found, the file is considered changed.
     *
     * @param vcEntry the hash map entry for a file in the version control directory
     * @param currentDirectoryHashMap the hash map created from the inputted destination directory path string
     * @param destinationPathString the absolute path string of the restore destination directory
     * @return {@code true} if the file has changed (content, filename, deletion); {@code false} otherwise
     * 
     * @see #isFileContentChanged(String, String) 
     * @see #getVCFileSavedPath(String) 
     * @see pathUtilities#splitCharacterHelper(String) 
     * @see String#replace(CharSequence, CharSequence) 
     * @see java.util.regex.Pattern#compile(String) 
     * @see java.util.regex.Pattern#matcher(CharSequence) 
     * @see Matcher#find() 
     */
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

    /**
     * Compares the content of two files by hashing them.
     * <p>
     * The method takes in two file path strings, calls {@link hashUtilities#hashFile(String)} on both strings,
     * and saves their hash values. If both hashes are not equal, the method returns {@code true};
     * Otherwise (if hashing fails or if both hashes are equal), it returns {@code false}.
     *
     * @param currentFilePath the absolute path string of a file in the current directory
     * @param vcFilePath the absolute path string of a file in the version control directory
     * @return {@code true} if file contents changed; 
     *          {@code false} otherwise.
     *          
     * @see hashUtilities#hashFile(String) 
     * @see String#equals(Object) 
     */
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

    /**
     * Finds and returns the saved relative path of a file in a version control directory using its path string.
     * <p>
     * The expected format is either: {@code <...>/.vc/<version_number>/<saved_relative_path>} or
     * {@code <...>\\.vc\\<version_number>\\<saved_relative_path>}, depending on the user's operating system and is
     * determined using the version control file path string.
     * For example, given {@code C:\project\.vc\42\src\test.txt}, this method returns {@code src\test.txt}
     * and given {@code /home/user/project/.vc/42/src/test.txt}, it returns {@code src/test.txt}.
     *
     * @param vcFilePathString the absolute path of a file in the version control directory
     * @return the saved relative path of the version-controlled file or an empty string, if a relative path is
     * not found.
     *
     * @see pathUtilities#splitCharacterHelper(String)
     * @see java.util.regex.Pattern#compile(String)
     * @see java.util.regex.Pattern#matcher(CharSequence)
     */
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
