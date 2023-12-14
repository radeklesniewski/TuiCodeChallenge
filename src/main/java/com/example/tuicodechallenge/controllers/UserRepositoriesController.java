package com.example.tuicodechallenge.controllers;

import com.example.tuicodechallenge.exceptions.NotFoundException;
import com.example.tuicodechallenge.model.ErrorMessage;
import com.example.tuicodechallenge.model.Repository;
import com.example.tuicodechallenge.services.UserRepositoriesService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

//    @ExceptionHandler({ Exception.class, NotFoundException.class })
//    public void handleException((HttpServletResponse response, Exception exception) {
//        return new ErrorMessage(response.getStatus(), exception.getMessage());
//    }

}
