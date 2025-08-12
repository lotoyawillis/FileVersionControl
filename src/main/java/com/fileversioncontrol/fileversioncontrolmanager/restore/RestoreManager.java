package com.fileversioncontrol.fileversioncontrolmanager.restore;

import com.fileversioncontrol.fileversioncontrolmanager.utils.directoryUtilities;
import com.fileversioncontrol.fileversioncontrolmanager.utils.hashUtilities;

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


public class RestoreManager {

    public static void restore(String vcSource, String destination) {
        HashMap<String, File> map1 = new HashMap<>();
        HashMap<String, File> map2 = new HashMap<>();
        HashMap<String, File> vcMap = hashUtilities.createHashMap(vcSource, map1);
        HashMap<String, File> destinationMap = hashUtilities.createHashMap(destination, map2);

        for (Map.Entry<String, File> vcEntry : vcMap.entrySet()) {
            if (destinationMap.get(vcEntry.getKey()) == null) {
                File file = vcEntry.getValue();
                String filePath = file.getAbsolutePath();
                String originalPath = filePath.replaceAll("\\\\.vc\\\\\\d", "");
                verifyPathExists(originalPath);

                Path vcPath = Paths.get(filePath);
                Path originalDestinationPath = Paths.get(originalPath);

                try {
                    Files.copy(vcPath, originalDestinationPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void verifyPathExists(String path) {
        String[] splitPath = path.split("\\\\");
        String partialPath = splitPath[0] + "\\" + splitPath[1];

        ArrayList<String> list = new ArrayList<>(Arrays.asList(splitPath));
        list.remove(splitPath[0]);
        list.remove(splitPath[1]);
        list.remove(splitPath[splitPath.length - 1]);

        splitPath = list.toArray(new String[0]);

        for (String piece : splitPath) {
            partialPath = partialPath + "\\" + piece;
            if (!directoryUtilities.isDirectory(partialPath)) {
                String[] splitPartialPath = partialPath.split("\\\\");
                directoryUtilities.createDirectory(partialPath, splitPartialPath[splitPartialPath.length - 1]);
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
        if (directoryUtilities.isDirectory(versionPath) && directoryUtilities.isDirectory(destinationPath)) {
            restore(versionPath, destinationPath);
        }
    }
}
