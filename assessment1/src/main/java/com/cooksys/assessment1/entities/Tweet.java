package com.cooksys.assessment1.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Tweet {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn
	private User author;

	@Column(nullable = false)
	private LocalDateTime posted;

	@Column(nullable = false)
	private boolean deleted = false;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn
	private Tweet inReplyTo;

	@ManyToOne
	@JoinColumn
	private Tweet repostOf;

}
