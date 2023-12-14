package com.example.tuicodechallenge.services.github;

import com.example.tuicodechallenge.model.Repository;
import com.example.tuicodechallenge.services.UserRepositoriesService;
import com.example.tuicodechallenge.services.github.client.GitHubClient;
import com.example.tuicodechallenge.services.github.model.GithubRepository;
import com.example.tuicodechallenge.services.github.utils.RepositoryMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.tuicodechallenge.services.github.utils.RepositoryMapper.mapToRepository;

@Service
@AllArgsConstructor
public class GitHubRepositoriesService implements UserRepositoriesService {

    private GitHubClient gitHubClient;

    @Override
    public List<Repository> getUserNonForkedRepositories(@NonNull String username) {
        return listUserNonForkedRepositories(username).parallelStream()
                .map(githubRepository -> mapToRepository(githubRepository, listRepositoryBranches(username, githubRepository.name())))
                .toList();
    }

    private List<GithubRepository> listUserNonForkedRepositories(@NonNull String username) {
        return gitHubClient.listUserRepositories(username)
                .stream()
                .filter(githubRepository -> !githubRepository.fork())
                .toList();
    }

    private List<Repository.Branch> listRepositoryBranches(@NonNull String username, @NonNull String repository) {
        return gitHubClient.listRepositoryBranches(username, repository).stream()
                .map(RepositoryMapper::mapToBranch)
                .toList();
    }

}