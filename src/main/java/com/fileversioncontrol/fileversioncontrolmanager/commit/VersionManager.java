package com.fileversioncontrol.fileversioncontrolmanager.commit;

import com.fileversioncontrol.fileversioncontrolmanager.utils.directoryUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.fileUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.pathUtilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;


public class VersionManager {
    public static boolean commit(String path) {
        String vcPath = pathUtilities.pathBuilder(path, ".vc");

        boolean successfulCommit;

        // Checks if the .vc directory exists
        if (directoryUtilities.isDirectory(vcPath)) {
            successfulCommit = commitFiles(vcPath, path);
        } else {
            directoryUtilities.createDirectory(vcPath, ".vc");
            successfulCommit = commitFiles(vcPath, path);
        }
        return successfulCommit;
    }

    /*
    public static boolean commitDirectory(String directoryPath, String originalDirectoryPath) {
        return commitFiles(directoryPath, originalDirectoryPath);
    }
    */

    public static boolean commitFiles(String directoryPath, String originalDirectoryPath) {
        int versionNumber = 1;
        String vcDirectoryPath = pathUtilities.pathBuilder(directoryPath, String.valueOf(versionNumber));
        while (directoryUtilities.isDirectory(vcDirectoryPath)) {
            versionNumber = versionNumber + 1;
            vcDirectoryPath = pathUtilities.pathBuilder(directoryPath, String.valueOf(versionNumber));
        }

        directoryUtilities.createDirectory(vcDirectoryPath, Integer.toString(versionNumber));
        return copyAllFiles(vcDirectoryPath, originalDirectoryPath);
    }

    public static boolean copyAllFiles(String vcDirectoryPath, String originalDirectoryPath) {
        List<String> allPaths = pathUtilities.getAllPathsInOneLayer(originalDirectoryPath);

        for (String path : allPaths) {
            if (directoryUtilities.isDirectory(path)) {
                String directoryName = pathUtilities.name(path);
                String directoryPath = pathUtilities.pathBuilder(vcDirectoryPath, directoryName);

                if (!directoryName.equals(".vc")) {
                    directoryUtilities.createDirectory(directoryPath, directoryName);
                    copyAllFiles(directoryPath, path);
                }
            } else if (fileUtilities.isFile(path)) {
                String fileName = pathUtilities.name(path);
                String destinationPathString = pathUtilities.pathBuilder(vcDirectoryPath, fileName);

                Path sourcePath = Paths.get(path);
                Path destinationPath = Paths.get(destinationPathString);

                try {
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println(e.getMessage()); // No one will see this
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean Commit(String path) {
        // Enter a path to a directory
        // If it does not exist, create a .vc directory
        // If .vc does exist, create a directory with a new version number
        // and copy all files in the input directory to their respective directories under the version directory

        // path = "C:\\Users\\lotlo\\OneDrive\\Documents\\test1";

        boolean successfulCommit = false;

        // Checks the directory path to make sure it exists
        if (directoryUtilities.isDirectory(path)) {
            return commit(path);
        }
        else {
            return successfulCommit;
        }
    }

    /*
    public static void main(String[] args) {
        String path = "C:/Users/lotlo/OneDrive/Documents/test2/";
        ArrayList<String> empty = new ArrayList<>();

        ArrayList<String> paths = pathUtilities.getAllFilePaths(path, empty);

        for (String p : paths)
        {
            System.out.println(p);
        }

    }
    */
}
