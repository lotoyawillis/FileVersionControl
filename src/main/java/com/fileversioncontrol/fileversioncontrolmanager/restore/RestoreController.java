package com.fileversioncontrol.fileversioncontrolmanager.restore;

import com.fileversioncontrol.fileversioncontrolmanager.shared.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restore")
public class RestoreController {
    private final RestoreService restoreService;
    public RestoreController(RestoreService restoreService) {
        this.restoreService = restoreService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response postRestoreFiles(@RequestBody List<String> paths){
        return restoreService.restoreFiles(paths);
    }

}
