package com.example.tuicodechallenge.http.client.github.model;

public record Branch(String name, Commit commit) {
    public record Commit(String sha) {
    }
}
