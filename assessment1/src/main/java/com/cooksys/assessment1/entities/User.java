package com.cooksys.assessment1.entities;

import java.sql.Timestamp;
import java.util.List;

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
@Table(name="user_table")
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
	private List<User> followers;
	
	@ManyToMany
	@JoinTable(name = "followers")
	private List<User> following;
	
	@ManyToMany
	@JoinTable(name = "user_likes",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private List<Tweet> likedTweets;
	
	@ManyToMany(mappedBy = "mentions")
	private List<Tweet> userMentions;
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void addFollower(User follower) {
		followers.add(follower);
	}
	
	public void addFollowing(User follow) {
		following.add(follow);
	}
	
	public void removeFollower(User follower) {
		followers.remove(follower);
	}
	
	public void removeFollowing(User follow) {
		followers.remove(follow);
	}

}