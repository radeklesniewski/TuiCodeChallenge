package com.example.tuicodechallenge.controllers;

import com.example.tuicodechallenge.services.UserRepositoriesService;
import com.example.tuicodechallenge.services.github.http.client.GitHubClient;
import com.example.tuicodechallenge.model.Repository;
import com.example.tuicodechallenge.services.github.model.GithubRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/github")
public class ApiController {

    private final UserRepositoriesService userRepositoriesService;

    public ApiController(UserRepositoriesService userRepositoriesService) {
        this.userRepositoriesService = userRepositoriesService;
    }

    @GetMapping("/repositories")
    public List<Repository> repositories() {
        return userRepositoriesService.getUserNonForkedRepositories("radeklesniewski");
    }

}
