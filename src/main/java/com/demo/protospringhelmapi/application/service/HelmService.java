package com.demo.protospringhelmapi.application.service;

import com.demo.protospringhelmapi.application.common.utils.GitCloneUtil;
import com.demo.protospringhelmapi.application.common.utils.HelmCommandUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HelmService {

    public String installFromGit(String release, String gitUrl, String branch, String token, String namespace, String chartPath) throws Exception {
        String clonePath = "/tmp/helm-chart/" + release;

        GitCloneUtil.cloneRepository(gitUrl, branch, token, clonePath);

        return HelmCommandUtil.runHelmCommand(List.of(
                "helm", "install", release,
                clonePath + "/" + chartPath,
                "--namespace", namespace
        ));
    }

    public String upgradeFromGit(String release, String gitUrl, String branch, String token,
                                 String namespace, String chartPath, String imageTag) throws Exception {
        String clonePath = "/tmp/helm-chart/" + release;

        // Git 리포지터리 클론
        Git git = GitCloneUtil.cloneRepository(gitUrl, branch, token, clonePath);

        // values.yaml 수정
        String valuesPath = clonePath + "/" + chartPath + "/values.yaml";
        updateImageTagInValuesYaml(valuesPath, imageTag);

        // Git 커밋 및 푸시
        commitAndPushChanges(git, "Update image.tag to " + imageTag, token);

        // Helm upgrade 실행
        return HelmCommandUtil.runHelmCommand(List.of(
                "helm", "upgrade", release,
                clonePath + "/" + chartPath,
                "--install",
                "--namespace", namespace,
                "--set", "image.tag=" + imageTag
        ));
    }


    private void updateImageTagInValuesYaml(String filePath, String imageTag) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        File file = new File(filePath);
        Map<String, Object> yamlData = mapper.readValue(file, Map.class);

        Map<String, Object> image = (Map<String, Object>) yamlData.get("image");
        if (image == null) {
            image = new LinkedHashMap<>();
            yamlData.put("image", image);
        }
        image.put("tag", imageTag);

        mapper.writeValue(file, yamlData);
    }

    public static void commitAndPushChanges(Git git, String commitMessage, String token) throws Exception {
        try {
            git.add().addFilepattern(".").call();
            git.commit().setMessage(commitMessage).call();

            log.info("Committed changes to local repo: {}", commitMessage);

            git.push()
                    .setRemote("origin")
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("x-token-auth", token))
                    .call();

            log.info("Pushed changes to remote repository.");
        } catch (Exception e) {
            log.error("Failed to commit and push changes", e);
            throw e;
        }
    }


}
