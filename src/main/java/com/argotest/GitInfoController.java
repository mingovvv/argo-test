package com.argotest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GitInfoController {

    @Value("${git.commit.id.abbrev:unknown}")
    private String commitId;

    @Value("${git.commit.message.short:unknown}")
    private String commitMessage;

    @Value("${git.branch:unknown}")
    private String branch;

    @Value("${git.commit.time:unknown}")
    private String commitTime;

    @GetMapping("/git-info")
    public Map<String, String> getGitInfo() {
        Map<String, String> gitInfo = new HashMap<>();
        gitInfo.put("commitId", commitId);
        gitInfo.put("commitMessage", commitMessage);
        gitInfo.put("branch", branch);
        gitInfo.put("commitTime", commitTime);

        // 로그에도 출력
        System.out.println("현재 Git 정보:");
        System.out.println("커밋 ID: " + commitId);
        System.out.println("커밋 메시지: " + commitMessage);
        System.out.println("브랜치: " + branch);
        System.out.println("커밋 시간: " + commitTime);

        return gitInfo;
    }
}
