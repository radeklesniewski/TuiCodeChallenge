package com.example.tuicodechallenge.model.client;

public record Branch(String name, Commit commit) {
    public record Commit(String sha) {
    }
}
