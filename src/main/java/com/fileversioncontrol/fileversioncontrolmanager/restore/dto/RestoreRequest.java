package com.fileversioncontrol.fileversioncontrolmanager.restore.dto;

public class RestoreRequest {
    private String vcPath;

    private String destinationPath;

    public String getVcPath() {
        return vcPath;
    }

    public void setVcPath(String vcPath) {
        this.vcPath = vcPath;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }
}
