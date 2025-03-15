package com.demo.protospringhelmapi.application.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Slf4j
public class GitCloneUtil {

    public static Git cloneRepository(String repoUrl, String branch, String token, String clonePath) throws Exception {
        File cloneDir = new File(clonePath);

        // 디렉토리 존재 시 삭제
        if (cloneDir.exists()) {
            log.info("Existing directory found, deleting: {}", clonePath);
            deleteDirectoryRecursively(cloneDir.toPath());
        }

        log.info("Cloning repository from URL: {} to path: {}", repoUrl, clonePath);
        try {
            Git git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setBranch(branch)
                    .setDirectory(cloneDir)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("x-token-auth", token))
                    .call();

            log.info("Repository cloned successfully: {}", clonePath);
            return git;
        } catch (Exception e) {
            log.error("Failed to clone repository from URL: {}", repoUrl, e);
            throw e;
        }
    }

    private static void deleteDirectoryRecursively(Path path) throws IOException {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                            log.info("Deleted file: {}", p);
                        } catch (IOException e) {
                            log.error("Failed to delete file: {}", p, e);
                            throw new RuntimeException("Failed to delete " + p, e);
                        }
                    });
        } catch (IOException e) {
            log.error("Failed to walk through directory: {}", path, e);
            throw e;
        }
    }
}


