package com.cooksys.assessment1.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class Profile {

	private String firstName;

	private String lastName;

	@Column(nullable = false)
	private String email;

	private String phone;

}