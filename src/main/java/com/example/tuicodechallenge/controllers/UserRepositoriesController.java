package com.example.tuicodechallenge.controllers;

import com.example.tuicodechallenge.model.Repository;
import com.example.tuicodechallenge.services.UserRepositoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRepositoriesController {

    private final UserRepositoriesService userRepositoriesService;

    public UserRepositoriesController(UserRepositoriesService userRepositoriesService) {
        this.userRepositoriesService = userRepositoriesService;
    }

    @GetMapping(value = "/user/{username}/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Repository> repositories(@PathVariable String username) {
        return userRepositoriesService.getUserNonForkedRepositories(username);
    }

}
