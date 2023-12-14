package com.example.tuicodechallenge.services.github.client;

import com.example.tuicodechallenge.exceptions.NotFoundException;
import com.example.tuicodechallenge.services.github.model.GitHubBranch;
import com.example.tuicodechallenge.services.github.model.GithubRepository;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Getter
@Service
public class GitHubClient {

    private static final String USERNAME_COULD_NOT_BE_FOUND_MSG = "Provided username could not be found.";

    @Value("${github.api.host}")
    private String githubApiHost;

    @Value("${github.api.users.repos.path}")
    private String githubApiUsersReposPath;

    @Value("${github.api.repo.branches.path}")
    private String githubApiRepoBranchesPath;

    private final RestClient restClient;

    public GitHubClient(RestTemplateBuilder restTemplateBuilder) {
        this.restClient = RestClient.create(restTemplateBuilder.build());
    }

    public List<GithubRepository> listUserRepositories(@NonNull String username) {
        return restClient.get()
                .uri(githubApiHost + githubApiUsersReposPath.formatted(username))
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new NotFoundException(USERNAME_COULD_NOT_BE_FOUND_MSG);
                })
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<GitHubBranch> listRepositoryBranches(@NonNull String username, @NonNull String repository) {
        return restClient.get()
                .uri(githubApiHost + githubApiRepoBranchesPath.formatted(username, repository))
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new NotFoundException(response.getStatusText());
                })
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
