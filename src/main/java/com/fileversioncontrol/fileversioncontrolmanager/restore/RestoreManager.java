package com.fileversioncontrol.fileversioncontrolmanager.restore;

import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.DirectoryUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.FileUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.HashUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.PathUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RestoreManager {
    private static List<String> restore(String vcSource, String destination) {
        List<String> results = new ArrayList<>();

        HashMap<Integer, File> vcMap = HashUtilities.createHashMap(vcSource);
        HashMap<Integer, File> destinationMap = HashUtilities.createHashMap(destination);

        File destinationFile = new File(destination);
        String destinationPathString = destinationFile.getAbsolutePath();

        for (Map.Entry<Integer, File> vcEntry : vcMap.entrySet()) {
            boolean isFileChanged = FileUtilities.isFileChangedForRestore(vcEntry, destinationMap, destinationPathString);

            if (isFileChanged) {
                File vcFile = vcEntry.getValue();
                String vcFilePathString = vcFile.getAbsolutePath();

                Pattern pattern;
                Matcher matcher;

                String destinationFilePathString;
                String delimiter = PathUtilities.splitCharacterHelper(vcFilePathString);
                if (delimiter.equals("\\")) {
                    pattern = Pattern.compile("^(.*?\\\\.vc\\\\\\d+)");
                    matcher = pattern.matcher(vcFilePathString);

                    if (matcher.find()) {
                        destinationFilePathString = matcher.replaceFirst(Matcher.quoteReplacement(destinationPathString));
                    } else {
                        destinationFilePathString = vcFilePathString.replaceFirst("\\\\.vc\\\\\\d+", "");
                    }
                } else {
                    pattern = Pattern.compile("^(.*?/.vc/\\d+)");
                    matcher = pattern.matcher(vcFilePathString);

                    if (matcher.find()) {
                        destinationFilePathString = matcher.replaceFirst(Matcher.quoteReplacement(destinationPathString));
                    } else {
                        destinationFilePathString = vcFilePathString.replaceFirst("/.vc/\\d+", "");
                    }
                }

                PathUtilities.createDirectoryPathIfItDoesNotExist(destinationFilePathString);

                Path vcPath = Paths.get(vcFilePathString);
                Path destinationPath = Paths.get(destinationFilePathString);

                try {
                    Files.copy(vcPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    results.add(String.format("%s has been restored\n", PathUtilities.name(vcFilePathString)));
                } catch (IOException e) {
                    results.add(String.format("%s has not been restored\n", PathUtilities.name(vcFilePathString)));
                }
            } else {
                results.add(String.format("%s is already up to date\n", PathUtilities.name(vcEntry.getValue().getAbsolutePath())));
            }
        }

        return results;
    }


    public static List<String> Restore(String versionPath, String destinationPath) {
        // Enter a version number to revert to and a path to the directory where you want the reverted files
        // Create HashMaps from the version number directory and the destination directory
        // Iterate through the version number directory's HashMap and search for its keys within the destination directory's HashMap
        // If a key from the version directory's files is not found, verify all directories in the path it was saved from still exist
        // If not, create them and copy the file to that original path

        List<String> results = new ArrayList<>();

        // Checks the directory path to make sure it exists
        if (DirectoryUtilities.isAVersionControlNumberDirectory(versionPath) && DirectoryUtilities.isDirectory(destinationPath)) {
            results = restore(versionPath, destinationPath);
        } else if (!DirectoryUtilities.isAVersionControlNumberDirectory(versionPath) && !DirectoryUtilities.isDirectory(destinationPath)) {
            results.add(String.format("%s is not a valid version control directory and %s is not a directory", versionPath, destinationPath));
        }  else if (!DirectoryUtilities.isAVersionControlNumberDirectory(versionPath)) {
            results.add(String.format("%s is not a valid version control directory", versionPath));
        } else {
            results.add(String.format("%s is not a directory", destinationPath));
        }

        return results;
    }
}
