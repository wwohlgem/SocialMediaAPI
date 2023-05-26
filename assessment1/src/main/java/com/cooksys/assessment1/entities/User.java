package com.cooksys.assessment1.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_table")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp joined;

	@Column(nullable = false)
	private boolean deleted = false;

	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "username", column = @Column(name = "username")),
			@AttributeOverride(name = "password", column = @Column(name = "password")),
	})
	private Credentials credentials;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "firstName", column = @Column(name = "firstName")),
			@AttributeOverride(name = "lastName", column = @Column(name = "lastName")),
			@AttributeOverride(name = "phone", column = @Column(name = "phone")),
			@AttributeOverride(name = "email", column = @Column(name = "email"))
	})
	private Profile profile;

	@ManyToMany
	@JoinTable(name = "followers_following")
	private Set<User> followers = new HashSet<>();

	@ManyToMany(mappedBy = "followers")
	private Set<User> following = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "user_likes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private Set<Tweet> likedTweets = new HashSet<>();

	@ManyToMany(mappedBy = "mentions")
	private List<Tweet> userMentions = new ArrayList<>();

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User user = (User) obj;
		return this.credentials.getUsername().equals(user.getCredentials().getUsername())
				&& this.id.equals(user.getId());
	}

	@Override
	public int hashCode() {
		return this.credentials.getUsername().hashCode() + this.id.hashCode();
	}

}