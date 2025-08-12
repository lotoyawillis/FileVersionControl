package com.fileversioncontrol.fileversioncontrolmanager.commit;

import com.fileversioncontrol.fileversioncontrolmanager.utils.directoryUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.fileUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.pathUtilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


public class VersionManager {
    public static void commit(String path) {
        String vcPath = path + "\\.vc";

        // Checks if the .vc directory exists
        if (directoryUtilities.isDirectory(vcPath)) {
            commitDirectory(vcPath, path);
        } else {
            directoryUtilities.createDirectory(vcPath, ".vc");
            commitDirectory(vcPath, path);
        }
    }

    public static void commitDirectory(String directoryPath, String originalDirectoryPath) {
        commitFiles(directoryPath, originalDirectoryPath);
    }

    public static void commitFiles(String directoryPath, String originalDirectoryPath) {
        int versionNumber = 1;
        String vcDirectoryPath = directoryPath + "\\" + versionNumber;
        while (directoryUtilities.isDirectory(vcDirectoryPath)) {
            versionNumber = versionNumber + 1;
            vcDirectoryPath = directoryPath + "\\" + versionNumber;
        }

        directoryUtilities.createDirectory(vcDirectoryPath, Integer.toString(versionNumber));
        copyFiles(vcDirectoryPath, originalDirectoryPath);
    }

    public static void copyFiles(String vcDirectoryPath, String originalDirectoryPath) {
        List<String> allPaths = pathUtilities.getAllPathsInOneLayer(originalDirectoryPath);
        try {
            for (String path : allPaths) {
                if (directoryUtilities.isDirectory(path)) {
                    String[] splitPath = path.split("\\\\");
                    String directoryName = splitPath[splitPath.length - 1];
                    String directoryPath = vcDirectoryPath + "\\" + directoryName;

                    if (!directoryName.equals(".vc")) {
                        directoryUtilities.createDirectory(directoryPath, directoryName);
                        copyFiles(directoryPath, path);
                    }
                } else if (fileUtilities.isFile(path)) {
                    String[] splitPath = path.split("\\\\");
                    String fileName = splitPath[splitPath.length - 1];
                    String destinationPathString = vcDirectoryPath + "\\" + fileName;

                    Path sourcePath = Paths.get(path);
                    Path destinationPath = Paths.get(destinationPathString);

                    try {
                        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Commit(String path) {
        // Enter a path to a directory
        // If it does not exist, create a .vc directory
        // If .vc does exist, create a directory with a new version number
        // and copy all files in the input directory to their respective directories under the version directory

        // path = "C:\\Users\\lotlo\\OneDrive\\Documents\\test1";

        // Checks the directory path to make sure it exists
        if (directoryUtilities.isDirectory(path)) {
            commit(path);
        }
    }
}
