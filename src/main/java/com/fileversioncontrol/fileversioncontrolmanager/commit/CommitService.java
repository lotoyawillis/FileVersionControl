package com.fileversioncontrol.fileversioncontrolmanager.commit;

import com.fileversioncontrol.fileversioncontrolmanager.shared.dto.Response;
import com.fileversioncontrol.fileversioncontrolmanager.shared.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitService {
    public Response commitFiles(String path){
        List<String> results = CommitManager.Commit(path);

        for (String result : results) {
            if (result.contains("has not been committed")) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, results, "Not all files have been committed");
            }
            else if (result.contains("is not a directory"))
            {
                throw new ApiException(HttpStatus.BAD_REQUEST, results, "The request is invalid");
            }
            else if ((result.contains("is up to date"))) {
                throw new ApiException(HttpStatus.NOT_MODIFIED, results, "The requested directory is up to date");
            }
        }
        return new Response(HttpStatus.CREATED.value(), results, "All files have been committed");
    }
}
