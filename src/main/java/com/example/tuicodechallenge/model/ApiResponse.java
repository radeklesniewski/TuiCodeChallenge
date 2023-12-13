package com.example.tuicodechallenge.model;

import java.util.List;

public record ApiResponse(String name, String ownerLogin, List<ApiBranch> branchList) {
    //TODO name of object
    public record ApiBranch(String branch, String lastCommitSha) {

    }
}
