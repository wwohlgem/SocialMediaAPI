package com.cooksys.assessment1.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@Entity
@NoArgsConstructor
public class Hashtag {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String label;

	@Column(nullable = false)
	private LocalDateTime firstUsed;

	@Column(nullable = false)
	private LocalDateTime lastUsed;

}
