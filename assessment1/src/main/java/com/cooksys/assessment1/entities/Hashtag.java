package com.cooksys.assessment1.entities;

import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Hashtag {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String label;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp firstUsed;

	@UpdateTimestamp
	@Column(nullable = false)
	private Timestamp lastUsed;

	@ManyToMany
	@JoinTable(
			name = "tweet_hashtags",
			joinColumns = @JoinColumn(name = "hashtag_id"),
			inverseJoinColumns = @JoinColumn(name = "tweet_id"))

	private Set<Tweet> hashtaggedTweets;

}
