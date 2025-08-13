package com.fileversioncontrol.fileversioncontrolmanager.service;

import com.fileversioncontrol.fileversioncontrolmanager.commit.VersionManager;
import com.fileversioncontrol.fileversioncontrolmanager.restore.RestoreManager;

public class FileVersionControlManagerService {
    public static boolean commitFiles(String path){
        // String path = "C:\\Users\\lotlo\\OneDrive\\Documents\\test5";
        return VersionManager.Commit(path);

    }

    public static void restoreFiles(String[] body){
        String versionPath = body[0];
        String destinationPath = body[1];
        RestoreManager.Restore(versionPath, destinationPath);
    }
}
