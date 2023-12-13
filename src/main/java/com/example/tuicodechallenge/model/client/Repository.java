package com.example.tuicodechallenge.model.client;

import java.util.List;

public record Repository(String name, Owner owner, boolean fork) {
    public record Owner(String login) {
    }
}