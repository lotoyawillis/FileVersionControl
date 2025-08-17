package com.fileversioncontrol.fileversioncontrolmanager.restore;

import com.fileversioncontrol.fileversioncontrolmanager.shared.dto.Response;
import com.fileversioncontrol.fileversioncontrolmanager.shared.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestoreService {
    public Response restoreFiles(List<String> paths){
        String versionPath = paths.get(0);
        String destinationPath = paths.get(1);
        int totalUpToDateFiles = 0;
        List<String> results = RestoreManager.Restore(versionPath, destinationPath);

        for (String result : results) {
            if (result.contains("is not a valid version control directory and")) {
                throw new ApiException(HttpStatus.BAD_REQUEST, results, "The version control directory and the destination directory are not valid");
            } else if (result.contains("is not a valid version control directory")) {
                throw new ApiException(HttpStatus.BAD_REQUEST, results, "The version control directory is not valid");
            } else if (result.contains("is not a directory")) {
                throw new ApiException(HttpStatus.BAD_REQUEST, results, "The destination directory is not valid");
            } else if (result.contains("has not been restored")) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, results, "Not all files have been restored");
            } else if (result.contains("is already up to date")) {
                totalUpToDateFiles = totalUpToDateFiles + 1;
            }
        }

        if (totalUpToDateFiles == results.size()) {
            throw new ApiException(HttpStatus.NOT_MODIFIED, results, "The requested restore directory is up to date");
        }
        return new Response(HttpStatus.CREATED.value(), results, "All files have been restored"); // only success case
    }
}
