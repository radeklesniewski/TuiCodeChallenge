package com.example.tuicodechallenge.exceptions;

import java.io.IOException;

public class NotFoundException extends IOException {
    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
