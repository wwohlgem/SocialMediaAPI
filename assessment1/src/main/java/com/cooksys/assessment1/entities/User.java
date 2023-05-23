package com.cooksys.assessment1.entities;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="user_table")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private LocalDateTime joined;

	@Column(nullable = false)
	private boolean deleted = false;

	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets;

	@Embedded
	Credentials credentials;

	@Embedded
	Profile profile;



}