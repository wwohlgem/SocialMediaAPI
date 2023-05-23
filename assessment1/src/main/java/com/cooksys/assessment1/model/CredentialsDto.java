package com.cooksys.assessment1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CredentialsDto {
    private Long id;
    private String username;
    private String password;
}
