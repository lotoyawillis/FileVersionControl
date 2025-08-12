package com.fileversioncontrol.fileversioncontrolmanager.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class pathUtilities {
    public static List<String> getAllPathsInOneLayer(String directoryPath) {
        List<String> emptyList = new ArrayList<>();
        File directory = new File(directoryPath);

        File[] files = directory.listFiles();

        if (files != null) {
            try {
                return Arrays.stream(files).map(File::toString).toList();
            } catch (Exception e) {
                return emptyList;
            }
        }
        return emptyList;
    }
}
