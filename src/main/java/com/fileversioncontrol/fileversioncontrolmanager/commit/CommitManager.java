package com.fileversioncontrol.fileversioncontrolmanager.commit;

import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.directoryUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.fileUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.pathUtilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


public class CommitManager {
    private static List<String> commit(String path) {
        List<String> results = new ArrayList<>();

        String vcPath = pathUtilities.pathBuilder(path, ".vc");

        // Checks if the .vc directory exists
        if (directoryUtilities.isDirectory(vcPath)) {
            results = commitFiles(vcPath, path);
        } else if (!directoryUtilities.isDirectory(vcPath)) {
            directoryUtilities.createDirectory(vcPath);
            results = commitFiles(vcPath, path);
        }
        return results;
    }

    private static List<String> commitFiles(String directoryPath, String originalDirectoryPath) {
        List<String> results = new ArrayList<>();
        int versionNumber = 1;
        String vcDirectoryPath = pathUtilities.pathBuilder(directoryPath, Integer.toString(versionNumber));
        while (directoryUtilities.isDirectory(vcDirectoryPath)) {
            versionNumber = versionNumber + 1;
            vcDirectoryPath = pathUtilities.pathBuilder(directoryPath, Integer.toString(versionNumber));
        }

        directoryUtilities.createDirectory(vcDirectoryPath);
        return copyAllFiles(vcDirectoryPath, originalDirectoryPath, results);
    }

    private static List<String> copyAllFiles(String vcDirectoryPath, String originalDirectoryPath, List<String> results) {
        List<String> allPaths = pathUtilities.getAllPathsInOneLayer(originalDirectoryPath);

        for (String path : allPaths) {
            if (directoryUtilities.isDirectory(path)) {
                String directoryName = pathUtilities.name(path);
                String directoryPath = pathUtilities.pathBuilder(vcDirectoryPath, directoryName);

                if (!directoryName.equals(".vc")) {
                    directoryUtilities.createDirectory(directoryPath);
                    results = copyAllFiles(directoryPath, path, results);
                }
            } else if (fileUtilities.isFile(path)) {
                String fileName = pathUtilities.name(path);
                String destinationPathString = pathUtilities.pathBuilder(vcDirectoryPath, fileName);

                Path sourcePath = Paths.get(path);
                Path destinationPath = Paths.get(destinationPathString);

                try {
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    results.add(String.format("%s has been committed\n", fileName));
                } catch (IOException e) {
                    results.add(String.format("%s has not been committed\n", fileName));
                }
            }
        }
        return results;
    }

    public static List<String> Commit(String path) {
        // Enter a path to a directory
        // If it does not exist, create a .vc directory
        // If .vc does exist, create a directory with a new version number
        // and copy all files in the input directory to their respective directories under the version directory

        List<String> results = new ArrayList<>();

        // Checks the directory path to make sure it exists
        if (directoryUtilities.isDirectory(path) && !directoryUtilities.isDirectoryUpToDate(path)) {
            return commit(path);
        } else if (!directoryUtilities.isDirectory(path)) {
            results.add(String.format("%s is not a directory", path));
        } else {
            results.add(String.format("%s is up to date", path));
        }

        return results;
    }
}
