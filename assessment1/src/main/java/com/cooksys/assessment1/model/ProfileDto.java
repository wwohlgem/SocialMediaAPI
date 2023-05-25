package com.cooksys.assessment1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProfileDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

}
