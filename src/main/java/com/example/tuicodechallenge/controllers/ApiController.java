package com.example.tuicodechallenge.controllers;

import com.example.tuicodechallenge.clients.github.GitHubClient;
import com.example.tuicodechallenge.model.ApiResponse;
import com.example.tuicodechallenge.model.client.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/github")
public class ApiController {

    private final GitHubClient gitHubClient;

    public ApiController(GitHubClient githubClient) {
        this.gitHubClient = githubClient;
    }

    @GetMapping("/repositories")
    public List<ApiResponse> repositories() {
        List<Repository> repositoryList = gitHubClient.getUsersRepositories("radeklesniewski")
                .stream()
                .filter(repository -> !repository.fork())
                .toList();

        return repositoryList.parallelStream()
                .map(repository -> new ApiResponse(repository.name(), repository.owner().login(), getRepositoryBranches(repository.name())))
                .toList();
    }

    private List<ApiResponse.ApiBranch> getRepositoryBranches(String repositoryName) {
        return gitHubClient.getRepositoryBranches("radeklesniewski", repositoryName)
                .stream()
                .map(branch -> new ApiResponse.ApiBranch(branch.name(), branch.commit().sha()))
                .toList();
    }
}
