package com.demo.protospringhelmapi.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelmRequest {
    private String releaseName;
    private String gitUrl;
    private String branch;
    private String token;
    private String namespace;
    private String chartPath;
    private String imageTag; // optional
}