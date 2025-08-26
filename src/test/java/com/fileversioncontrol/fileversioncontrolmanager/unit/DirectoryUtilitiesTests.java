package com.fileversioncontrol.fileversioncontrolmanager.unit;
import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.DirectoryUtilities;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DirectoryUtilitiesTests {
    private static Path tempDirectory;

    @BeforeAll
    static void setUp() throws Exception {
        tempDirectory = Files.createTempDirectory("tempRoot");
    }

    @AfterAll
    static void cleanUp() throws Exception {
        File directory = new File(tempDirectory.toString());
        FileUtils.cleanDirectory(directory);
    }

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
    void testIsAVersionControlNumberDirectory_success() {
        String versionControlDirectoryPathString = tempDirectory.resolve(".vc").resolve("1").toString();
        File directory = new File(versionControlDirectoryPathString);
        boolean isCreated = directory.mkdirs();
        assertTrue(isCreated);

        assertTrue(DirectoryUtilities.isAVersionControlNumberDirectory(versionControlDirectoryPathString));
    }

    @Test
    void testIsAVersionControlNumberDirectory_invalidVersionControlPath() {
        String versionControlDirectoryPathString = tempDirectory.resolve("invalidVersionControlDirectory").toString();
        assertFalse(DirectoryUtilities.isAVersionControlNumberDirectory(versionControlDirectoryPathString));
    }

    @Test
    void testIsDirectory_success() {
        String directoryPathString = tempDirectory.resolve("newDirectory").toString();
        File directory = new File(directoryPathString);
        boolean isCreated = directory.mkdir();
        assertTrue(isCreated);

        assertTrue(DirectoryUtilities.isDirectory(directoryPathString));
    }

    @Test
    void testIsDirectory_invalidDirectory() {
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

        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        File vcTestFile = new File(versionControlPath.resolve("test_file.txt").toString());

        boolean isVCFileCreated = vcTestFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcTestFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Create a directory to check with the same file and file contents as the version control directory
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

        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        File vcTestFile = new File(versionControlPath.resolve("test_file.txt").toString());

        boolean isVCFileCreated = vcTestFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcTestFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Create a directory to check with file changes compared to the version control directory
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

        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        File vcTestFile = new File(versionControlPath.resolve("test_file.txt").toString());

        boolean isVCFileCreated = vcTestFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcTestFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Add the version control file to its hash map with the hash code of path it would have been saved under as the key
        File originalPathFile = new File(vcTestFile.getAbsolutePath().replaceAll("\\\\.vc\\\\\\d+", "").replaceAll("/.vc/\\d+", ""));
        vcHashMap.put(originalPathFile.hashCode(), vcTestFile);

        // Create a directory to check with file changes compared to the version control directory
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

        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        File vcTestFile = new File(versionControlPath.resolve("test_file.txt").toString());

        boolean isVCFileCreated = vcTestFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcTestFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Add the version control file to its hash map with the hash code of path it would have been saved under as the key
        File originalPathFile = new File(vcTestFile.getAbsolutePath().replaceAll("\\\\.vc\\\\\\d+", "").replaceAll("/.vc/\\d+", ""));
        vcHashMap.put(originalPathFile.hashCode(), vcTestFile);

        // Create a directory to check with the same file and file contents as the version control directory
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
}
