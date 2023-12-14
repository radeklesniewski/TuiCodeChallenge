package com.example.tuicodechallenge.model;

import java.util.List;

public record Repository(String name, String ownerLogin, List<Branch> branchList) {

    public record Branch(String branch, String lastCommitSha) {

    }
}
