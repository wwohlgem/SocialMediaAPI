package com.cooksys.assessment1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException{
    private static final long serialVersionUID = 6528930232146124623L;
    private String message;
}
