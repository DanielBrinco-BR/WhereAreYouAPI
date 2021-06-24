package com.whereareyou.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PhonerNotFoundException extends Exception {

    public PhonerNotFoundException(String phoneNumber) {
        super(String.format("Phone with number %s not found in the system.", phoneNumber));
    }

    public PhonerNotFoundException(Long id) {
        super(String.format("Phone with id %s not found in the system.", id));
    }
}
