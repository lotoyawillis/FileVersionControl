package com.fileversioncontrol.fileversioncontrolmanager.restore;

import com.fileversioncontrol.fileversioncontrolmanager.utils.directoryUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.fileUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.hashUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.pathUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RestoreManager {
    public static List<String> restore(String vcSource, String destination) {
        List<String> results = new ArrayList<>();

        HashMap<Integer, File> map1 = new HashMap<>();
        HashMap<Integer, File> map2 = new HashMap<>();
        HashMap<Integer, File> vcMap = hashUtilities.createHashMap(vcSource, map1);
        HashMap<Integer, File> destinationMap = hashUtilities.createHashMap(destination, map2);

        File destinationFile = new File(destination);
        String destinationPathString = destinationFile.getAbsolutePath();

        for (Map.Entry<Integer, File> vcEntry : vcMap.entrySet()) {
            boolean isFileChanged = fileUtilities.isFileChangedForRestore(vcEntry, destinationMap, destinationPathString);

            if (isFileChanged) {
                File vcFile = vcEntry.getValue();
                String vcFilePathString = vcFile.getAbsolutePath();

                Pattern pattern;
                Matcher matcher;

                String destinationFilePathString;
                String delimiter = pathUtilities.splitCharacterHelper(vcFilePathString);
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

                pathUtilities.createDirectoryPathIfItDoesNotExist(destinationFilePathString);

                Path vcPath = Paths.get(vcFilePathString);
                Path destinationPath = Paths.get(destinationFilePathString);

                try {
                    Files.copy(vcPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    results.add(String.format("%s has been restored\n", pathUtilities.name(vcFilePathString)));
                } catch (IOException e) {
                    results.add(e.getMessage());
                }
            } else {
                results.add(String.format("%s is already up to date\n", pathUtilities.name(vcEntry.getValue().getAbsolutePath())));
            }
        }

        return results;
    }


    public static String Restore(String versionPath, String destinationPath) {
        // Enter a version number to revert to and a path to the directory where you want the reverted files
        // Create HashMaps from the version number directory and the destination directory
        // Iterate through the version number directory's HashMap and search for its keys within the destination directory's HashMap
        // If a key from the version directory's files is not found, verify all directories in the path it was saved from still exist
        // If not, create them and copy the file to that original path

        // Checks the directory path to make sure it exists
        if (directoryUtilities.isAVersionControlNumberDirectory(versionPath) && directoryUtilities.isDirectory(destinationPath)) {
            List<String> results = restore(versionPath, destinationPath);
            for (String result : results) {
                if (result.contains("has been restored")) {
                    return "OK";
                } else if (!result.contains("is already up to date")) {
                    return "RESTORE_FAILED";
                }
            }
            return "UP_TO_DATE";
        } else {
            return "BAD_REQUEST";
        }
    }

    /*
    public static void main(String[] args) {
        // String destination = "C:/Users/lotlo/OneDrive/Documents/commit_test_do_commit/";
        String destination = "C:/Users/lotlo/OneDrive/Documents/destination";

        // String vcSource = "C:/Users/lotlo/OneDrive/Documents/commit_test_do_commit/.vc/1";
        String vcSource = "C:/Users/lotlo/OneDrive/Documents/test2/.vc/1";

        String status = Restore(vcSource, destination);

        System.out.println(status);
    }
    */

}
