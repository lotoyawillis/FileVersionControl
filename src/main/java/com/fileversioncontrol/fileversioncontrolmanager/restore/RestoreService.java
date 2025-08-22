package com.fileversioncontrol.fileversioncontrolmanager.restore;

import com.fileversioncontrol.fileversioncontrolmanager.shared.dto.Response;
import com.fileversioncontrol.fileversioncontrolmanager.shared.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles the business logic related to restoring a directory's files.
 * <p>
 * The service restores a directory's files and returns the Response if successful or triggers the exception
 * that corresponds with the error that happened during the restore attempt.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Restore a directory's files</li>
 * </ul>
 * <p>
 * Example Usage:
 * <pre>{@code
 * RestoreService.restoreFiles(path);
 * }</pre>
 *
 * @author Lotoya Willis
 * @version 1.0
 */
@Service
public class RestoreService {
    /**
     * Restores a directory's previous commit to a destination directory.
     * <p>
     * The method performs the following:
     * <ul>
     *     <li>Attempts to restore files to a destination directory</li>
     *     <li>Evaluates if any errors happened during the restore attempt</li>
     * </ul>
     *
     * @param versionPath the version control directory with the files to be restored
     * @param destinationPath the directory where the restored files should go
     * @return the results Response for the restore
     *
     * @throws ApiException if restore fails
     */
    public Response restoreFiles(String versionPath, String destinationPath){
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
            throw new ApiException(HttpStatus.CONFLICT, results, "The requested destination directory is up to date with the version control directory");
        }
        return new Response(HttpStatus.CREATED.value(), results, "All changed files have been restored"); // only success case
    }
}
