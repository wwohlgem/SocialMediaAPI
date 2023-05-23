package com.cooksys.assessment1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException{
    private static final long serialVersionUID = 5142716791293670859L;
    private String message;
}
