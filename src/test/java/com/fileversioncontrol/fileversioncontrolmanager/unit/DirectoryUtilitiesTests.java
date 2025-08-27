package com.fileversioncontrol.fileversioncontrolmanager.unit;
import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.DirectoryUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DirectoryUtilitiesTests {
    @TempDir
    Path tempDirectory;

    @Test
    void testCreateDirectory_success() {
        String newDirectoryPathString = tempDirectory.resolve("tempDirectory").toString();
        File directory = new File(newDirectoryPathString);

        DirectoryUtilities.createDirectory(newDirectoryPathString);
        assertTrue(directory.isDirectory());
    }

    @Test
    void testCreateDirectory_alreadyExists() {
        String newDirectoryPathString = tempDirectory.resolve("tempDirectory").toString();
        File directory = new File(newDirectoryPathString);
        DirectoryUtilities.createDirectory(newDirectoryPathString);
        assertTrue(directory.isDirectory());

        DirectoryUtilities.createDirectory(newDirectoryPathString);
        assertTrue(directory.isDirectory());
    }

    @Test
    void testCreateDirectory_invalidPath() {
        String invalidDirectoryPathString = ":1234~";
        File directory = new File(invalidDirectoryPathString);

        DirectoryUtilities.createDirectory(invalidDirectoryPathString);
        assertFalse(directory.isDirectory());
    }

    @Test
    void testIsAVersionControlNumberDirectory_true() {
        String versionControlDirectoryPathString = tempDirectory.resolve(".vc").resolve("1").toString();
        File directory = new File(versionControlDirectoryPathString);
        boolean isCreated = directory.mkdirs();
        assertTrue(isCreated);

        assertTrue(DirectoryUtilities.isAVersionControlNumberDirectory(versionControlDirectoryPathString));
    }

    @Test
    void testIsAVersionControlNumberDirectory_false() {
        String versionControlDirectoryPathString = tempDirectory.resolve("invalidVersionControlDirectory").toString();
        assertFalse(DirectoryUtilities.isAVersionControlNumberDirectory(versionControlDirectoryPathString));
    }

    @Test
    void testIsDirectory_true() {
        String directoryPathString = tempDirectory.resolve("newDirectory").toString();
        File directory = new File(directoryPathString);
        boolean isCreated = directory.mkdir();
        assertTrue(isCreated);

        assertTrue(DirectoryUtilities.isDirectory(directoryPathString));
    }

    @Test
    void testIsDirectory_false() {
        String directoryPathString = tempDirectory.resolve("invalidDirectory").toString();
        assertFalse(DirectoryUtilities.isDirectory(directoryPathString));
    }

    @Test
    void testIsDirectoryUpToDate_true() throws IOException {
        // Create version history
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        File vcTestFile = new File(versionControlPath.resolve("test_file.txt").toString());
        boolean isVCFileCreated = vcTestFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcTestFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Creates and writes to a file with the same name and content as the one in the version control directory
        String directoryPathString = tempDirectory.resolve("newDirectory").toString();
        Path directoryPath = Paths.get(directoryPathString);
        File directoryTestFile = new File(directoryPath.resolve("test_file.txt").toString());
        boolean isDirectoryFileCreated = directoryTestFile.createNewFile();
        assertTrue(isDirectoryFileCreated);
        Path directoryTestFilePath = Paths.get(directoryTestFile.getAbsolutePath());
        Files.write(directoryTestFilePath, content);

        assertTrue(DirectoryUtilities.isDirectoryUpToDate(directoryPathString));
    }

    @Test
    void testIsDirectoryUpToDate_false() throws IOException {
        // Create version history
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");
        List<String> differentContent = List.of("This is the first sentence.", "This is the second sentence.");

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        File vcTestFile = new File(versionControlPath.resolve("test_file.txt").toString());
        boolean isVCFileCreated = vcTestFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcTestFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Creates and writes different content to a file with the same name as the one in the version control directory
        String directoryPathString = tempDirectory.resolve("newDirectory").toString();
        Path directoryPath = Paths.get(directoryPathString);
        File directoryTestFile = new File(directoryPath.resolve("test_file.txt").toString());
        boolean isDirectoryFileCreated = directoryTestFile.createNewFile();
        assertTrue(isDirectoryFileCreated);
        Path directoryTestFilePath = Paths.get(directoryTestFile.getAbsolutePath());
        Files.write(directoryTestFilePath, differentContent);

        assertFalse(DirectoryUtilities.isDirectoryUpToDate(directoryPathString));
    }

    @Test
    void testIsDirectoryChanged_true() throws IOException {
        HashMap<Integer, File> vcHashMap = new HashMap<>();
        HashMap<Integer, File> directoryHashMap = new HashMap<>();

        // Create version history
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");
        List<String> differentContent = List.of("This is the first sentence.", "This is the second sentence.");

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        File vcTestFile = new File(versionControlPath.resolve("test_file.txt").toString());
        boolean isVCFileCreated = vcTestFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcTestFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Add the version control file to its hash map with the hash code of path it would have been saved under as the key
        File originalPathFile = new File(vcTestFile.getAbsolutePath().replaceAll("\\\\.vc\\\\\\d+", "").replaceAll("/.vc/\\d+", ""));
        vcHashMap.put(originalPathFile.hashCode(), vcTestFile);

        // Creates and writes different content to a file with the same name as the one in the version control directory
        String directoryPathString = tempDirectory.resolve("newDirectory").toString();
        Path directoryPath = Paths.get(directoryPathString);
        File directoryTestFile = new File(directoryPath.resolve("test_file.txt").toString());
        boolean isDirectoryFileCreated = directoryTestFile.createNewFile();
        assertTrue(isDirectoryFileCreated);
        Path directoryTestFilePath = Paths.get(directoryTestFile.getAbsolutePath());
        Files.write(directoryTestFilePath, differentContent);

        // Add the directory file to its hash map with the hash code of its path as the key
        directoryHashMap.put(directoryTestFile.hashCode(), directoryTestFile);

        assertTrue(DirectoryUtilities.isDirectoryChanged(directoryHashMap, vcHashMap));
    }

    @Test
    void testIsDirectoryChanged_false() throws IOException {
        HashMap<Integer, File> vcHashMap = new HashMap<>();
        HashMap<Integer, File> directoryHashMap = new HashMap<>();

        // Create version history
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        File vcTestFile = new File(versionControlPath.resolve("test_file.txt").toString());
        boolean isVCFileCreated = vcTestFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcTestFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Add the version control file to its hash map with the hash code of path it would have been saved under as the key
        File originalPathFile = new File(vcTestFile.getAbsolutePath().replaceAll("\\\\.vc\\\\\\d+", "").replaceAll("/.vc/\\d+", ""));
        vcHashMap.put(originalPathFile.hashCode(), vcTestFile);

        // Creates and writes to a file with the same name and content as the one in the version control directory
        String directoryPathString = tempDirectory.resolve("newDirectory").toString();
        Path directoryPath = Paths.get(directoryPathString);
        File directoryTestFile = new File(directoryPath.resolve("test_file.txt").toString());
        boolean isDirectoryFileCreated = directoryTestFile.createNewFile();
        assertTrue(isDirectoryFileCreated);
        Path directoryTestFilePath = Paths.get(directoryTestFile.getAbsolutePath());
        Files.write(directoryTestFilePath, content);

        // Add the directory file to its hash map with the hash code of its path as the key
        directoryHashMap.put(directoryTestFile.hashCode(), directoryTestFile);

        assertFalse(DirectoryUtilities.isDirectoryChanged(directoryHashMap, vcHashMap));
    }

    @Test
    void testGetLatestVersionControlDirectory_success() {
        // Create two version control directories
        String firstVersionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File firstVersionControlDirectory = new File(firstVersionControlDirectoryPathString);
        boolean areDirectoriesCreated = firstVersionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        String secondVersionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("2").toString();
        File secondVersionControlDirectory = new File(secondVersionControlDirectoryPathString);
        areDirectoriesCreated = secondVersionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        // Create a path string for the directory that contains the version control directories
        String directoryPathString = tempDirectory.resolve("newDirectory").toString();

        assertEquals(secondVersionControlDirectoryPathString, DirectoryUtilities.getLatestVersionNumberDirectory(directoryPathString));
    }

    @Test
    void testGetLatestVersionControlDirectory_noVersionControlDirectoryFound() {
        // Create two version control directories
        String firstVersionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve("subdirectory").resolve("1").toString();
        File firstVersionControlDirectory = new File(firstVersionControlDirectoryPathString);
        boolean areDirectoriesCreated = firstVersionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        String secondVersionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve("subdirectory").resolve("2").toString();
        File secondVersionControlDirectory = new File(secondVersionControlDirectoryPathString);
        areDirectoriesCreated = secondVersionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        // Create a path string for the directory that contains the version control directories
        String directoryPathString = tempDirectory.resolve("newDirectory").toString();

        assertEquals("", DirectoryUtilities.getLatestVersionNumberDirectory(directoryPathString));
    }
}
