package com.whereareyou.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneAlreadyRegisteredException extends Exception{

    public PhoneAlreadyRegisteredException(String phoneNumber) {
        super(String.format("Phone with number %s already registered in the system.", phoneNumber));
    }
}
