package com.cooksys.assessment1.entities;

import java.time.LocalDateTime;

<<<<<<< HEAD
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
=======
import jakarta.persistence.*;
>>>>>>> 65a9b34619d64fb3b5061a4284042ef3a383ecc1
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
<<<<<<< HEAD
@Table(name="user_table")
=======
@Table(name = "user_table")
>>>>>>> 65a9b34619d64fb3b5061a4284042ef3a383ecc1
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