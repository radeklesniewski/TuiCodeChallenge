package com.example.tuicodechallenge.exceptions;

import org.springframework.web.client.RestClientException;

public class NotFoundException extends RestClientException {
    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
