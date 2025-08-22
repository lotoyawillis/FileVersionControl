package com.fileversioncontrol.fileversioncontrolmanager.commit;

import com.fileversioncontrol.fileversioncontrolmanager.shared.dto.Response;
import com.fileversioncontrol.fileversioncontrolmanager.shared.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles the business logic related to committing a directory's files.
 * <p>
 * The service commits a directory's files and returns the Response if successful or triggers the exception
 * that corresponds with the error that happened during the commit attempt.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Commit a directory's files</li>
 * </ul>
 * <p>
 * Example Usage:
 * <pre>{@code
 * CommitService.commitFiles(path);
 * }</pre>
 *
 * @author Lotoya Willis
 * @version 1.0
 */
@Service
public class CommitService {
    /**
     * Commits a directory's files.
     * <p>
     * The method performs the following:
     * <ul>
     *     <li>Attempts to commit files</li>
     *     <li>Evaluates if any errors happened during the commit attempt</li>
     * </ul>
     *
     * @param path the directory with the files to be committed
     * @return the results Response for the commit
     *
     * @throws ApiException if commit fails
     */
    public Response commitFiles(String path){
        List<String> results = CommitManager.Commit(path);

        for (String result : results) {
            if (result.contains("has not been committed")) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, results, "Not all files have been committed");
            }
            else if (result.contains("is not a directory"))
            {
                throw new ApiException(HttpStatus.BAD_REQUEST, results, "The requested directory is not valid");
            }
            else if ((result.contains("is up to date"))) {
                throw new ApiException(HttpStatus.CONFLICT, results, "The requested directory is up to date");
            }
        }
        return new Response(HttpStatus.CREATED.value(), results, "All files have been committed");
    }
}
