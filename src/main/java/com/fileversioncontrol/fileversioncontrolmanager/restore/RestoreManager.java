package com.fileversioncontrol.fileversioncontrolmanager.restore;

import com.fileversioncontrol.fileversioncontrolmanager.utils.directoryUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.hashUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.pathUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RestoreManager {
    public static void restore(String vcSource, String destination) {
        HashMap<Integer, File> map1 = new HashMap<>();
        HashMap<Integer, File> map2 = new HashMap<>();
        HashMap<Integer, File> vcMap = hashUtilities.createHashMap(vcSource, map1);
        HashMap<Integer, File> destinationMap = hashUtilities.createHashMap(destination, map2);

        File destinationFile = new File(destination);

        for (Map.Entry<Integer, File> vcEntry : vcMap.entrySet()) {
            boolean isFileChanged = directoryUtilities.isFileChanged(vcEntry, destinationMap);

            if (isFileChanged) {
                File vcFile = vcEntry.getValue();
                String vcFilePathString = vcFile.getAbsolutePath();

                Pattern pattern;
                Matcher matcher;

                String destinationPathString;
                String delimiter = pathUtilities.splitCharacterHelper(vcFilePathString);
                if (delimiter.equals("\\")) {
                    pattern = Pattern.compile("^(.*?\\\\.vc\\\\\\d+)");
                    matcher = pattern.matcher(vcFilePathString);

                    if (matcher.find()) {
                        destinationPathString = matcher.replaceFirst(Matcher.quoteReplacement(destinationFile.getAbsolutePath()));
                    }
                    else {
                        destinationPathString = vcFilePathString.replaceFirst("\\\\.vc\\\\\\d+", "");
                    }
                }
                else {
                    pattern = Pattern.compile("^(.*?/.vc/\\d+)");
                    matcher = pattern.matcher(vcFilePathString);

                    if (matcher.find()) {
                        destinationPathString = matcher.replaceFirst(Matcher.quoteReplacement(destinationFile.getAbsolutePath()));
                    }
                    else {
                        destinationPathString = vcFilePathString.replaceFirst("/.vc/\\d+", "");
                    }
                }

                createDirectoryPathIfItDoesNotExist(destinationPathString);

                Path vcPath = Paths.get(vcFilePathString);
                Path destinationPath = Paths.get(destinationPathString);

                try {
                    Files.copy(vcPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.printf("%s has been restored\n", pathUtilities.name(vcFilePathString));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                System.out.printf("%s is already up to date\n", pathUtilities.name(vcEntry.getValue().getAbsolutePath()));
            }
        }
    }

    /*
    public static void restore(String vcSource, String destination) {
        HashMap<Integer, File> map1 = new HashMap<>();
        HashMap<Integer, File> map2 = new HashMap<>();
        HashMap<Integer, File> vcMap = hashUtilities.createHashMap(vcSource, map1);
        HashMap<Integer, File> destinationMap = hashUtilities.createHashMap(destination, map2);

        for (Map.Entry<Integer, File> vcEntry : vcMap.entrySet()) {
            if (destinationMap.get(vcEntry.getKey()) == null) {
                File file = vcEntry.getValue();
                String filePath = file.getAbsolutePath();

                String originalPath;
                String delimiter = pathUtilities.splitCharacterHelper(filePath);
                if (delimiter.equals("\\")) {
                    // originalPath = filePath.replaceAll("\\\\.vc\\\\\\d", "");
                    originalPath = filePath.replaceAll("\\\\[^\\\\]+\\\\.vc\\\\\\d", String.format("\\\\%s", pathUtilities.name(destination)));
                }
                else {
                    // originalPath = filePath.replaceAll("\\/.vc\\/\\d", "");
                    originalPath = filePath.replaceAll("/[^/]+/.vc/\\d", String.format("\\/%s", pathUtilities.name(destination)));
                }

                // String originalPath = filePath.replaceAll("\\\\.vc\\\\\\d", "");
                verifyPathExists(originalPath);

                Path vcPath = Paths.get(filePath);
                Path originalDestinationPath = Paths.get(originalPath);

                try {
                    Files.copy(vcPath, originalDestinationPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                File vcFile = vcEntry.getValue();
                File destinationFile = destinationMap.get(vcEntry.getKey());

                String vcFileName = pathUtilities.name(vcFile.getAbsolutePath());
                String destinationFileName = pathUtilities.name(destinationFile.getAbsolutePath());

                if (!vcFileName.equals(destinationFileName)) {
                    String destinationFilePath = destinationFile.getAbsolutePath();

                    String delimiter = pathUtilities.splitCharacterHelper(destinationFilePath);

                    String filePath = destinationFilePath.replace(String.format("%s%s", delimiter, destinationFileName), String.format("%s%s", delimiter, vcFileName));
                    while (fileUtilities.isFile(filePath)) {
                        String[] fileParts = filePath.split("\\.");
                        String extension = fileParts[fileParts.length - 1];
                        String filePathWithoutExtension = filePath.substring(0, filePath.length() - (extension.length() + 1));                       // Added one to the length of the extension to account for the "." character
                        filePath = filePathWithoutExtension + "-Copy." + extension;
                    }

                    File renameFile = new File(filePath);

                    try {
                        boolean renameSuccessful = destinationFile.renameTo(renameFile);
                        if (renameSuccessful) {
                            System.out.println("Rename was successful");
                        } else {
                            System.out.println("Failed to rename file");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
    */

    /*
    public static void restore(String vcSource, String destination) {
        HashMap<String, File> map1 = new HashMap<>();
        HashMap<String, File> map2 = new HashMap<>();
        HashMap<String, File> vcMap = hashUtilities.createHashMap(vcSource, map1);
        HashMap<String, File> destinationMap = hashUtilities.createHashMap(destination, map2);

        for (Map.Entry<String, File> vcEntry : vcMap.entrySet()) {
            if (destinationMap.get(vcEntry.getKey()) == null) {
                File file = vcEntry.getValue();
                String filePath = file.getAbsolutePath();

                String originalPath;
                String delimiter = pathUtilities.splitCharacterHelper(filePath);
                if (delimiter.equals("\\")) {
                    // originalPath = filePath.replaceAll("\\\\.vc\\\\\\d", "");
                    originalPath = filePath.replaceAll("\\\\[^\\\\]+\\\\.vc\\\\\\d", String.format("\\\\%s", pathUtilities.name(destination)));
                }
                else {
                    // originalPath = filePath.replaceAll("\\/.vc\\/\\d", "");
                    originalPath = filePath.replaceAll("/[^/]+/.vc/\\d", String.format("\\/%s", pathUtilities.name(destination)));
                }

                // String originalPath = filePath.replaceAll("\\\\.vc\\\\\\d", "");
                verifyPathExists(originalPath);

                Path vcPath = Paths.get(filePath);
                Path originalDestinationPath = Paths.get(originalPath);

                try {
                    Files.copy(vcPath, originalDestinationPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                File vcFile = vcEntry.getValue();
                File destinationFile = destinationMap.get(vcEntry.getKey());

                String vcFileName = pathUtilities.name(vcFile.getAbsolutePath());
                String destinationFileName = pathUtilities.name(destinationFile.getAbsolutePath());

                if (!vcFileName.equals(destinationFileName)) {
                    String destinationFilePath = destinationFile.getAbsolutePath();

                    String delimiter = pathUtilities.splitCharacterHelper(destinationFilePath);

                    String filePath = destinationFilePath.replace(String.format("%s%s", delimiter, destinationFileName), String.format("%s%s", delimiter, vcFileName));
                    while (fileUtilities.isFile(filePath)) {
                        String[] fileParts = filePath.split("\\.");
                        String extension = fileParts[fileParts.length - 1];
                        String filePathWithoutExtension = filePath.substring(0, filePath.length() - (extension.length() + 1));                       // Added one to the length of the extension to account for the "." character
                        filePath = filePathWithoutExtension + "-Copy." + extension;
                    }

                    File renameFile = new File(filePath);

                    try {
                        boolean renameSuccessful = destinationFile.renameTo(renameFile);
                        if (renameSuccessful) {
                            System.out.println("Rename was successful");
                        } else {
                            System.out.println("Failed to rename file");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

     */

    public static void createDirectoryPathIfItDoesNotExist(String path) {
        String[] splitPath;
        String partialPath;

        String delimiter = pathUtilities.splitCharacterHelper(path);
        if (delimiter.equals("\\")) {
            splitPath = path.split("\\\\");
            partialPath = pathUtilities.pathBuilder(splitPath[0], splitPath[1]);
        }
        else {
            splitPath = path.split("/");
            partialPath = pathUtilities.pathBuilder(splitPath[0], splitPath[1]);
        }
        // String[] splitPath = path.split("\\\\");
        // String partialPath = splitPath[0] + "\\" + splitPath[1];

        ArrayList<String> list = new ArrayList<>(Arrays.asList(splitPath));
        list.remove(splitPath[0]);
        list.remove(splitPath[1]);
        list.remove(splitPath[splitPath.length - 1]); // Erase the filename from the path

        splitPath = list.toArray(new String[0]);

        for (String piece : splitPath) {
            // partialPath = partialPath + "\\" + piece;
            partialPath = pathUtilities.pathBuilder(partialPath, piece);
            if (!directoryUtilities.isDirectory(partialPath)) {
                // String[] splitPartialPath = partialPath.split("\\\\");
                /*
                String[] splitPartialPath;
                if (delimiter.equals("\\")) {
                    splitPartialPath = partialPath.split("\\\\");
                }
                else {
                    splitPartialPath = partialPath.split("\\/");
                }
                */
                //directoryUtilities.createDirectory(partialPath, splitPartialPath[splitPartialPath.length - 1]);
                directoryUtilities.createDirectory(partialPath, pathUtilities.name(partialPath));
            }
        }
    }

    public static void Restore(String versionPath, String destinationPath) {
        // Enter a version number to revert to and a path to the directory where you want the reverted files
        // Create HashMaps from the version number directory and the destination directory
        // Iterate through the version number directory's HashMap and search for its keys within the destination directory's HashMap
        // If a key from the version directory's files is not found, verify all directories in the path it was saved from still exist
        // If not, create them and copy the file to that original path

        // Checks the directory path to make sure it exists
        if (directoryUtilities.isAVersionControlNumberDirectory(versionPath) && directoryUtilities.isDirectory(destinationPath)) {
            restore(versionPath, destinationPath);
        }
        else {
            System.out.println("Invalid request");
        }
    }

    /*
    public static void main(String[] args) {
        // String destination = "C:/Users/lotlo/OneDrive/Documents/commit_test_do_commit/";
        String destination = "C:/Users/lotlo/OneDrive/Documents/destination/test/other notes/embedded/new";

        // String vcSource = "C:/Users/lotlo/OneDrive/Documents/commit_test_do_commit/.vc/1";
        String vcSource = "C:/Users/lotlo/OneDrive/Documents/test2/.vc/1";

        Restore(vcSource, destination);
    }
    */

}
