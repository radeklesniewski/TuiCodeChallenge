package com.example.tuicodechallenge.services.github.model;

public record GithubRepository(String name, Owner owner, boolean fork) {
    public record Owner(String login) {
    }
}