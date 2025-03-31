package com.argotest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class GitInfoController {

    // Spring Boot가 자동으로 생성하고 주입해주는 GitProperties 빈
    // git.properties 파일이 있어야 주입됩니다.
    @Autowired
    private Optional<GitProperties> gitProperties; // Optional로 안전하게 처리

    @GetMapping("/git-info")
    public Map<String, String> getGitInfo() {
        Map<String, String> gitInfo = new HashMap<>();

        if (gitProperties.isPresent()) {
            GitProperties properties = gitProperties.get();
            gitInfo.put("branch", properties.getBranch());
            gitInfo.put("commitId", properties.getCommitId());
            gitInfo.put("shortCommitId", properties.getShortCommitId());
            gitInfo.put("commitTime", String.valueOf(properties.getCommitTime())); // Instant -> String
            gitInfo.put("commitMessage", properties.get("commit.message.full")); // get(key) 사용
            gitInfo.put("buildTime", String.valueOf(properties.get("build.time"))); // Instant -> String
            gitInfo.put("buildVersion", properties.get("build.version")); // build.gradle의 version과 연동될 수 있음

            // 필요에 따라 다른 정보 추가 (gitProperties.keys 설정에 따라 사용 가능한 속성이 달라짐)
            // gitInfo.put("commitUserName", properties.get("commit.user.name"));
            // gitInfo.put("dirty", properties.get("dirty"));
            // gitInfo.put("remoteOriginUrl", properties.get("remote.origin.url"));

        } else {
            gitInfo.put("error", "Git information not available. Was the application built with Git info and a .git directory?");
        }

        return gitInfo;
    }
}
