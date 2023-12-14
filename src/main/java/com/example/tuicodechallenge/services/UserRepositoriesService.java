package com.example.tuicodechallenge.services;

import com.example.tuicodechallenge.model.Repository;

import java.util.List;

public interface UserRepositoriesService {

    List<Repository> getUserNonForkedRepositories(String username);

}
