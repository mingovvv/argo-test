package com.argotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@SpringBootApplication
public class ArgoTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArgoTestApplication.class, args);
    }

    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        try {
            // Git 정보를 수동으로 가져오기
            ProcessBuilder processBuilder = new ProcessBuilder();
            Properties gitProps = new Properties();

            // Git 커밋 ID
            processBuilder.command("git", "rev-parse", "--short", "HEAD");
            Process process = processBuilder.start();
            String commitId = new String(process.getInputStream().readAllBytes()).trim();
            gitProps.setProperty("git.commit.id.abbrev", commitId.isEmpty() ? "unknown" : commitId);

            // Git 브랜치
            processBuilder.command("git", "rev-parse", "--abbrev-ref", "HEAD");
            process = processBuilder.start();
            String branch = new String(process.getInputStream().readAllBytes()).trim();
            gitProps.setProperty("git.branch", branch.isEmpty() ? "unknown" : branch);

            // Git 커밋 메시지
            processBuilder.command("git", "log", "-1", "--pretty=%s");
            process = processBuilder.start();
            String message = new String(process.getInputStream().readAllBytes()).trim();
            gitProps.setProperty("git.commit.message.short", message.isEmpty() ? "unknown" : message);

            // Git 커밋 시간
            processBuilder.command("git", "log", "-1", "--pretty=%cd", "--date=format:%Y-%m-%d %H:%M:%S");
            process = processBuilder.start();
            String time = new String(process.getInputStream().readAllBytes()).trim();
            gitProps.setProperty("git.commit.time", time.isEmpty() ? "unknown" : time);

            // 정보 출력
            System.out.println("==================================================");
            System.out.println("Git 정보 (수동으로 가져옴):");
            System.out.println("커밋 ID: " + gitProps.getProperty("git.commit.id.abbrev"));
            System.out.println("브랜치: " + gitProps.getProperty("git.branch"));
            System.out.println("커밋 메시지: " + gitProps.getProperty("git.commit.message.short"));
            System.out.println("커밋 시간: " + gitProps.getProperty("git.commit.time"));
            System.out.println("==================================================");

            // git.properties 파일 수동 생성
            Path resourcesPath = Paths.get("build/resources/main");
            Files.createDirectories(resourcesPath);
            File gitProperties = resourcesPath.resolve("git.properties").toFile();

            try (OutputStream out = new FileOutputStream(gitProperties)) {
                gitProps.store(out, "Git 속성 (수동으로 생성됨)");
                System.out.println("git.properties 파일이 생성됨: " + gitProperties.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("Git 정보를 가져오는 중 오류 발생: " + e.getMessage());
        }
    }

}
