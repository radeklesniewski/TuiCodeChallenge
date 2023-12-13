package com.example.tuicodechallenge.http.client.github;

import com.example.tuicodechallenge.http.client.github.model.Branch;
import com.example.tuicodechallenge.http.client.github.model.Repository;
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

    @Value("${github.api.host}")
    private String githubApiHost;

    @Value("${github.api.users.repos.path}")
    private String githubApiUsersReposPath;

    @Value("${github.api.repo.branches.path}")
    private String githubApiRepoBranchesPath;

    //TODO add error handling https://www.baeldung.com/spring-boot-restclient
    private final RestClient restClient;

    public GitHubClient(RestTemplateBuilder restTemplateBuilder) {
        this.restClient = RestClient.create(restTemplateBuilder.build());
    }

    public List<Repository> listUserRepositories(@NonNull String username) {
        return restClient.get()
                .uri(githubApiHost + githubApiUsersReposPath.formatted(username))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<Branch> listRepositoryBranches(@NonNull String username, @NonNull String repository) {
        return restClient.get()
                .uri(githubApiHost + githubApiRepoBranchesPath.formatted(username, repository))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
