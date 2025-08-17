package com.fileversioncontrol.fileversioncontrolmanager.commit;

import com.fileversioncontrol.fileversioncontrolmanager.shared.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/commit")
public class CommitController {
    private final CommitService commitService;
    public CommitController(CommitService commitService) {
        this.commitService = commitService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response postCommitFiles(@RequestBody String path){
        return commitService.commitFiles(path);
    }


}
