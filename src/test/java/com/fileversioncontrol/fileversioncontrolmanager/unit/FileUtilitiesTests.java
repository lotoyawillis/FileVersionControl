package com.fileversioncontrol.fileversioncontrolmanager.unit;

import com.fileversioncontrol.fileversioncontrolmanager.shared.utils.FileUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FileUtilitiesTests {
    @TempDir
    Path tempDirectory;

    @Test
    void testIsFile_true() throws IOException {
        String filePathString = tempDirectory.resolve("test_file.txt").toString();
        File file = new File(filePathString);

        boolean isFileCreated = file.createNewFile();
        assertTrue(isFileCreated);

        assertTrue(FileUtilities.isFile(filePathString));
    }

    @Test
    void testIsFile_false() {
        String filePathString = tempDirectory.resolve("invalid_file.txt").toString();
        assertFalse(FileUtilities.isFile(filePathString));
    }

    @Test
    void testIsFileChangedForCommit_true() throws IOException {
        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");
        List<String> differentContent = List.of("This is the first sentence.", "This is the second sentence.");

        HashMap<Integer, File> directoryHashMap = new HashMap<>();

        // Creates all the directories missing from the version control directory path string
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        String vcFilePathString = versionControlPath.resolve("test_file.txt").toString();
        File vcFile = new File(vcFilePathString);
        boolean isVCFileCreated = vcFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Create a map entry with the version control file as the value and the hash code of path it would have been saved under as the key
        File originalPathFile = new File(vcFile.getAbsolutePath().replaceAll("\\\\.vc\\\\\\d+", "").replaceAll("/.vc/\\d+", ""));
        Map.Entry<Integer, File> vcEntry = new AbstractMap.SimpleEntry<>(originalPathFile.hashCode(), new File(vcFilePathString));

        // Creates and writes different content to a file with the same name as the one in the version control directory
        String filePathString = tempDirectory.resolve("newDirectory").resolve("test_file.txt").toString();
        File file = new File(filePathString);
        boolean isFileCreated = file.createNewFile();
        assertTrue(isFileCreated);
        Path testFilePath = Paths.get(file.getAbsolutePath());
        Files.write(testFilePath, differentContent);

        // Add the directory file to its hash map with the hash code of its path as the key
        directoryHashMap.put(file.hashCode(), file);

        assertTrue(FileUtilities.isFileChangedForCommit(vcEntry, directoryHashMap));
    }

    @Test
    void testIsFileChangedForCommit_false() throws IOException {
        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");

        HashMap<Integer, File> directoryHashMap = new HashMap<>();

        // Creates all the directories missing from the version control directory path string
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        String vcFilePathString = versionControlPath.resolve("test_file.txt").toString();
        File vcFile = new File(vcFilePathString);
        boolean isVCFileCreated = vcFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Create a map entry with the version control file as the value and the hash code of path it would have been saved under as the key
        File originalPathFile = new File(vcFile.getAbsolutePath().replaceAll("\\\\.vc\\\\\\d+", "").replaceAll("/.vc/\\d+", ""));
        Map.Entry<Integer, File> vcEntry = new AbstractMap.SimpleEntry<>(originalPathFile.hashCode(), new File(vcFilePathString));

        // Creates and writes to a file with the same name and content as the one in the version control directory
        String filePathString = tempDirectory.resolve("newDirectory").resolve("test_file.txt").toString();
        File file = new File(filePathString);
        boolean isFileCreated = file.createNewFile();
        assertTrue(isFileCreated);
        Path testFilePath = Paths.get(file.getAbsolutePath());
        Files.write(testFilePath, content);

        // Add the directory file to its hash map with the hash code of its path as the key
        directoryHashMap.put(file.hashCode(), file);

        assertFalse(FileUtilities.isFileChangedForCommit(vcEntry, directoryHashMap));
    }

    @Test
    void testIsFileChangedForRestore_true() throws IOException {
        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");
        List<String> differentContent = List.of("This is the first sentence.", "This is the second sentence.");

        HashMap<Integer, File> directoryHashMap = new HashMap<>();

        // Creates all the directories missing from the version control directory path string
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        String vcFilePathString = versionControlPath.resolve("test_file.txt").toString();
        File vcFile = new File(vcFilePathString);
        boolean isVCFileCreated = vcFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Create a map entry with the version control file as the value and the hash code of path it would have been saved under as the key
        File originalPathFile = new File(vcFile.getAbsolutePath().replaceAll("\\\\.vc\\\\\\d+", "").replaceAll("/.vc/\\d+", ""));
        Map.Entry<Integer, File> vcEntry = new AbstractMap.SimpleEntry<>(originalPathFile.hashCode(), new File(vcFilePathString));

        // Creates and writes different content to a file with the same name as the one in the version control directory
        String destinationDirectoryPathString = tempDirectory.resolve("destinationDirectory").toString();
        File destinationDirectory = new File(destinationDirectoryPathString);
        boolean isDirectoryCreated = destinationDirectory.mkdir();
        assertTrue(isDirectoryCreated);

        Path destinationPath = Paths.get(destinationDirectoryPathString);
        String filePathString = destinationPath.resolve("test_file.txt").toString();
        File file = new File(filePathString);
        boolean isFileCreated = file.createNewFile();
        assertTrue(isFileCreated);
        Path testFilePath = Paths.get(file.getAbsolutePath());
        Files.write(testFilePath, differentContent);

        // Add the directory file to its hash map with the hash code of its path as the key
        directoryHashMap.put(file.hashCode(), file);

        assertTrue(FileUtilities.isFileChangedForRestore(vcEntry, directoryHashMap, destinationDirectoryPathString));
    }

    @Test
    void testIsFileChangedForRestore_false() throws IOException {
        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");

        HashMap<Integer, File> directoryHashMap = new HashMap<>();

        // Creates all the directories missing from the version control directory path string
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        String vcFilePathString = versionControlPath.resolve("test_file.txt").toString();
        File vcFile = new File(vcFilePathString);
        boolean isVCFileCreated = vcFile.createNewFile();
        assertTrue(isVCFileCreated);
        Path vcTestFilePath = Paths.get(vcFile.getAbsolutePath());
        Files.write(vcTestFilePath, content);

        // Create a map entry with the version control file as the value and the hash code of path it would have been saved under as the key
        File originalPathFile = new File(vcFile.getAbsolutePath().replaceAll("\\\\.vc\\\\\\d+", "").replaceAll("/.vc/\\d+", ""));
        Map.Entry<Integer, File> vcEntry = new AbstractMap.SimpleEntry<>(originalPathFile.hashCode(), new File(vcFilePathString));

        // Creates and writes to a file with the same name and content as the one in the version control directory
        String destinationDirectoryPathString = tempDirectory.resolve("destinationDirectory").toString();
        File destinationDirectory = new File(destinationDirectoryPathString);
        boolean isDirectoryCreated = destinationDirectory.mkdir();
        assertTrue(isDirectoryCreated);

        Path destinationPath = Paths.get(destinationDirectoryPathString);
        String filePathString = destinationPath.resolve("test_file.txt").toString();
        File file = new File(filePathString);
        boolean isFileCreated = file.createNewFile();
        assertTrue(isFileCreated);
        Path testFilePath = Paths.get(file.getAbsolutePath());
        Files.write(testFilePath, content);

        // Add the directory file to its hash map with the hash code of its path as the key
        directoryHashMap.put(file.hashCode(), file);

        assertFalse(FileUtilities.isFileChangedForRestore(vcEntry, directoryHashMap, destinationDirectoryPathString));
    }

    @Test
    void testIsFileContentChanged_true() throws IOException {
        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");
        List<String> differentContent = List.of("This is the first sentence.", "This is the second sentence.");

        String firstTestFilePathString = tempDirectory.resolve("test_file1.txt").toString();
        File firstTestFile = new File(firstTestFilePathString);
        boolean isFirstTestFileCreated = firstTestFile.createNewFile();
        assertTrue(isFirstTestFileCreated);
        Path firstTestFilePath = Paths.get(firstTestFile.getAbsolutePath());
        Files.write(firstTestFilePath, content);

        String secondTestFilePathString = tempDirectory.resolve("test_file2.txt").toString();
        File secondTestFile = new File(secondTestFilePathString);
        boolean isSecondTestFileCreated = secondTestFile.createNewFile();
        assertTrue(isSecondTestFileCreated);
        Path secondTestFilePath = Paths.get(secondTestFile.getAbsolutePath());
        Files.write(secondTestFilePath, differentContent);

        assertTrue(FileUtilities.isFileContentChanged(firstTestFilePathString, secondTestFilePathString));
    }

    @Test
    void testIsFileContentChanged_false() throws IOException {
        List<String> content = List.of("This is the first sentence.", "This is the second sentence.", "This is the third sentence.");

        String firstTestFilePathString = tempDirectory.resolve("test_file1.txt").toString();
        File firstTestFile = new File(firstTestFilePathString);
        boolean isFirstTestFileCreated = firstTestFile.createNewFile();
        assertTrue(isFirstTestFileCreated);
        Path firstTestFilePath = Paths.get(firstTestFile.getAbsolutePath());
        Files.write(firstTestFilePath, content);

        String secondTestFilePathString = tempDirectory.resolve("test_file2.txt").toString();
        File secondTestFile = new File(secondTestFilePathString);
        boolean isSecondTestFileCreated = secondTestFile.createNewFile();
        assertTrue(isSecondTestFileCreated);
        Path secondTestFilePath = Paths.get(secondTestFile.getAbsolutePath());
        Files.write(secondTestFilePath, content);

        assertFalse(FileUtilities.isFileContentChanged(firstTestFilePathString, secondTestFilePathString));
    }

    @Test
    void testGetVCFileSavedPath_success() throws IOException {
        // Creates all the directories missing from the version control directory path string
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("1").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        String vcSubdirectoryPathString = versionControlPath.resolve("subdirectory").toString();
        File vcSubdirectory = new File(vcSubdirectoryPathString);
        boolean isDirectoryCreated = vcSubdirectory.mkdir();
        assertTrue(isDirectoryCreated);

        Path vcSubdirectoryPath = Paths.get(vcSubdirectoryPathString);
        String vcSubdirectoryFilePathString = vcSubdirectoryPath.resolve("test_file.txt").toString();
        File vcSubdirectoryFile = new File(vcSubdirectoryFilePathString);
        boolean isSubdirectoryFileCreated = vcSubdirectoryFile.createNewFile();
        assertTrue(isSubdirectoryFileCreated);

        Path partialPath = Paths.get("subdirectory");
        String partialPathString = partialPath.resolve("test_file.txt").toString();

        assertEquals(partialPathString, FileUtilities.getVCFileSavedPath(vcSubdirectoryFilePathString));
    }

    @Test
    void testGetVCFileSavedPath_invalidVersionControlDirectoryFilePath() throws IOException {
        // Creates all the directories missing from the version control directory path string
        String versionControlDirectoryPathString = tempDirectory.resolve("newDirectory").resolve(".vc").resolve("invalidDirectory").toString();
        File versionControlDirectory = new File(versionControlDirectoryPathString);
        boolean areDirectoriesCreated = versionControlDirectory.mkdirs();
        assertTrue(areDirectoriesCreated);

        // Creates and writes to a file in the version control directory
        Path versionControlPath = Paths.get(versionControlDirectoryPathString);
        String vcSubdirectoryPathString = versionControlPath.resolve("subdirectory").toString();
        File vcSubdirectory = new File(vcSubdirectoryPathString);
        boolean isDirectoryCreated = vcSubdirectory.mkdir();
        assertTrue(isDirectoryCreated);

        Path vcSubdirectoryPath = Paths.get(vcSubdirectoryPathString);
        String vcSubdirectoryFilePathString = vcSubdirectoryPath.resolve("test_file.txt").toString();
        File vcSubdirectoryFile = new File(vcSubdirectoryFilePathString);
        boolean isSubdirectoryFileCreated = vcSubdirectoryFile.createNewFile();
        assertTrue(isSubdirectoryFileCreated);

        assertEquals("", FileUtilities.getVCFileSavedPath(vcSubdirectoryFilePathString));
    }
}
