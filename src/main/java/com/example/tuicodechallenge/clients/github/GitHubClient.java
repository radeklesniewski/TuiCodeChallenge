package com.example.tuicodechallenge.clients.github;

import com.example.tuicodechallenge.model.client.Branch;
import com.example.tuicodechallenge.model.client.Repository;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Data
@Service
public class GitHubClient {

    private static final String ACCEPT_HEADER_VALUE = "application/vnd.github+json";
    @Value("${github.api.host}")
    private String githubApiHost;

    @Value("${github.api.users.repos.endpoint}")
    private String githubApiUsersReposEndpoint;

    @Value("${github.api.repo.branches}")
    private String githubApiRepoBranchesEndpoint;

    RestClient restClient = RestClient.create();

    public List<Repository> getUsersRepositories(@NonNull String username) {
        return restClient.get()
                .uri(githubApiHost + githubApiUsersReposEndpoint.formatted(username))
                .accept(MediaType.valueOf(ACCEPT_HEADER_VALUE))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<Branch> getRepositoryBranches(@NonNull String username, @NonNull String repository) {
        return restClient.get()
                .uri(githubApiHost + githubApiRepoBranchesEndpoint.formatted(username, repository))
                .accept(MediaType.valueOf(ACCEPT_HEADER_VALUE))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
