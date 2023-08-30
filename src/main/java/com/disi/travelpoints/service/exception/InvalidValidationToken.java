package com.disi.travelpoints.service.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidValidationToken extends RuntimeException {

    public InvalidValidationToken(String message) {
        super(message);
    }
}
