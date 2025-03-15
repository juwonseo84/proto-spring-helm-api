package com.demo.protospringhelmapi.application.cotroller;

import com.demo.protospringhelmapi.application.dto.HelmRequest;
import com.demo.protospringhelmapi.application.service.HelmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/helm")
public class HelmController {

    @Autowired
    private HelmService helmService;

    @PostMapping("/install")
    public String install(@RequestBody HelmRequest req) throws Exception {
        return helmService.installFromGit(
                req.getReleaseName(), req.getGitUrl(), req.getBranch(), req.getToken(), req.getNamespace(), req.getChartPath()
        );
    }

    @PostMapping("/upgrade")
    public String upgrade(@RequestBody HelmRequest req) throws Exception {
        return helmService.upgradeFromGit(
                req.getReleaseName(), req.getGitUrl(), req.getBranch(), req.getToken(), req.getNamespace(), req.getChartPath(), req.getImageTag()
        );
    }
}