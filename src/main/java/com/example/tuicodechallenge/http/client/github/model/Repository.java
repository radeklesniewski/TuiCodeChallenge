package com.example.tuicodechallenge.http.client.github.model;

import java.util.List;

public record Repository(String name, Owner owner, boolean fork) {
    public record Owner(String login) {
    }
}