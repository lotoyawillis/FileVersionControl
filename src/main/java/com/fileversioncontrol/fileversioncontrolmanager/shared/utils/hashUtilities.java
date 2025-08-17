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

public class hashUtilities {
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
