package com.cooksys.assessment1.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private LocalDateTime joined;

	@Column(nullable = false)
	private boolean deleted = false;

	@Embedded
	Credentials credentials;

	@Embedded
	Profile profile;

}