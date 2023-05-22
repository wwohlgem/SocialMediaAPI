package com.cooksys.assessment1.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class Credentials {
	
	@Column(nullable=false)
	private String username;

	@Column(nullable=false)
	private String password;
	
}