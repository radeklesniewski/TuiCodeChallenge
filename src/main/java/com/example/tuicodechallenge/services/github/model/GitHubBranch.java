package com.example.tuicodechallenge.services.github.model;

public record GitHubBranch(String name, Commit commit) {
    public record Commit(String sha) {
    }
}
