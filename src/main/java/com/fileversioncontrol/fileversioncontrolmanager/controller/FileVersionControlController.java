package com.fileversioncontrol.fileversioncontrolmanager.controller;

import com.fileversioncontrol.fileversioncontrolmanager.service.FileVersionControlManagerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController
@Controller
//@RequestMapping("")
public class FileVersionControlController {
    /*
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/commit")
    public void getCommitFiles(){

    }

     */
    /*
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

     */

    @PostMapping("/api/v1/commit")
    public ResponseEntity<String> postCommitFiles(@RequestBody String path){
        boolean success = FileVersionControlManagerService.commitFiles(path);

        if (!success)
        {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("All files were not copied");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("All files were copied");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/restore")
    public void postRestoreFiles(){

    }

    /*
    //@ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/{path:^(?!api$)(?!.*\\..*)(?!\\/).+}/**")
    public String otherPaths() {
        return "forward:/index.htm";
    }
    */

    /*
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restore")
    public String postRestoreFiles(@RequestBody String[] body){
        FileVersionControlManagerService.restoreFiles(body);
        return "Success";
    }
    */

    /*
    @GetMapping("{path:^(?!api|public|swagger)[^\\.]*}/**")
    public String handleForward() {
        return "forward:/";
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{path:^(?!api).*}")
    public String otherPaths() {
        return "index.html";
    }

    @RequestMapping("/error")
    public String error() {
        return "";
    }

     */
}
