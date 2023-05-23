package com.cooksys.assessment1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = -493217897291884563L;
    private String message;
}
