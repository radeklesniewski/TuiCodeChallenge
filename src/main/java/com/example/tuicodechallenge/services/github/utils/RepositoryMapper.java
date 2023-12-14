package com.example.tuicodechallenge.services.github.utils;

import com.example.tuicodechallenge.model.Repository;
import com.example.tuicodechallenge.services.github.model.GitHubBranch;
import com.example.tuicodechallenge.services.github.model.GithubRepository;

import java.util.List;

public class RepositoryMapper {

    public static Repository mapToRepository(GithubRepository githubRepository, List<Repository.Branch> branchList) {
        return new Repository(githubRepository.name(), githubRepository.owner().login(), branchList);
    }

    public static Repository.Branch mapToBranch(GitHubBranch branch) {
        return new Repository.Branch(branch.name(), branch.commit().sha());
    }

}
