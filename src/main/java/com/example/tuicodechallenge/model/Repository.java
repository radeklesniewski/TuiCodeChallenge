package com.example.tuicodechallenge.model;

import java.util.List;

public record Repository(String repositoryName, String ownerLogin, List<Branch> branchList) {

    public record Branch(String branchName, String lastCommitSha) {

    }
}
