package com.fileversioncontrol.fileversioncontrolmanager.shared.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A utility class for creating hash maps and hash strings.
 * <p>
 * This class provides static methods to:
 * <ul>
 *   <li>Create a hash string from a file's contents</li>
 *   <li>Create a hash map from a directory</li>
 * </ul>
 * <p>
 * The methods are designed to work with all types of paths
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * String directoryPath = "C:\\Users\\Documents\\test";
 * String filePath = "C:\\Users\\Documents\\test\\testFile.txt";
 * HashMap<Integer, File> hashMap = new HashMap<>();
 *
 * String hashString = hashUtilities.hashFile(filePath); // Creates a hash string based on a file's contents
 *
 * HashMap<Integer, File> directoryHashMap = hashUtilities.createHashMap(directoryPath, hashMap); // Creates a hash map
 *                                                                                      // of all a directory's files
 * }</pre>
 *
 * @author Lotoya Willis
 * @version 1.0
 */
public class hashUtilities {
    /**
     * Computes the SHA-256 hash of a file's contents.
     * <p>
     * The method reads the file in buffered chunks to compute the hash
     * and returns the result as a hexadecimal string.
     *
     * @param path the absolute path to the file
     * @return an SHA-256 hash of the file's contents as a hexadecimal string
     *
     * @throws IOException if an I/O error occurs while reading the file
     * @throws NoSuchAlgorithmException if SHA-256 is not supported
     *
     * @see java.nio.file.Paths#get(String, String...)
     * @see java.security.MessageDigest#getInstance(String)
     * @see java.security.MessageDigest#update(byte[], int, int)
     * @see MessageDigest#digest()
     * @see java.lang.StringBuilder#append(String)
     * @see StringBuilder#toString()
     */
    public static String hashFile(String path) throws IOException, NoSuchAlgorithmException {
        Path filePath = Paths.get(path);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(filePath.toFile()); BufferedInputStream bis = new BufferedInputStream(fis)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * Creates a hash map of the files in a directory.
     * <p>
     * The method collects all path strings of the files in the inputted directory,
     * removes the {@code .vc/<version_number>} portion from each path if it exists to reconstruct
     * the original path the file had when it was committed, and uses the int hash code of that path
     * created from {@code File.hashCode()} as the key. The accompanying value is the {@code File} object
     * created from the path string in the directory.
     *
     * @param pathString the inputted directory path string
     * @param hashMap an existing hash map to hold all the hash code and File pairings
     * @return the updated hash map containing file hash code keys and their associated File objects
     *
     * @see pathUtilities#getAllFilePaths(String, List)
     * @see pathUtilities#splitCharacterHelper(String)
     * @see String#replaceAll(String, String)
     * @see File#hashCode()
     */
    public static HashMap<Integer, File> createHashMap(String pathString, HashMap<Integer, File> hashMap) {
        List<String> paths = new ArrayList<>();
        List<String> allPaths = pathUtilities.getAllFilePaths(pathString, paths);
        int hash;
        try {
            for (String path : allPaths) {
                String originalPath;
                String delimiter = pathUtilities.splitCharacterHelper(path);
                if (delimiter.equals("\\")) {
                    originalPath = path.replaceAll("\\\\.vc\\\\\\d+", "");
                } else {
                    originalPath = path.replaceAll("/.vc/\\d+", "");
                }

                File hashFile = new File(originalPath);                            // Files are saved with the hash code values they had before they were committed and their current
                hash = hashFile.hashCode();                                        // absolute path

                File savedFile = new File(path);
                hashMap.put(hash, savedFile);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return hashMap;
    }

}
