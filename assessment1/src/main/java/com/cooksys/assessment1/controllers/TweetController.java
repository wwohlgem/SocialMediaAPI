package com.cooksys.assessment1.controllers;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.assessment1.model.ContextDto;
import com.cooksys.assessment1.model.CredentialsDto;
import com.cooksys.assessment1.model.HashtagDto;
import com.cooksys.assessment1.model.TweetRequestDto;
import com.cooksys.assessment1.model.TweetResponseDto;
import com.cooksys.assessment1.model.UserResponseDto;
import com.cooksys.assessment1.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {
	
	private final TweetService tweetService;
	
	@GetMapping
	public List<TweetResponseDto> getAllTweets() {
		return tweetService.getAllTweets();
	}
	
	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}
	
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweetById(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.deleteTweetById(id, credentialsDto);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweet(tweetRequestDto);
	}
	
	@PostMapping("/{id}/like")
	@ResponseStatus(HttpStatus.CREATED)
	public void addLikeToTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		tweetService.addLikeToTweet(id, credentialsDto);
	}

	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getUsersMentioned(@PathVariable Long id){

		return tweetService.getUsersMentioned(id);

	}

	@PostMapping("/{id}/repost")
	public TweetResponseDto repostTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto){

		return tweetService.repostTweet(id, credentialsDto);

	}

	@GetMapping("/{id}/tags")
	public List<HashtagDto> getHashtags(@PathVariable Long id){

		return tweetService.getHashtags(id);

	}

	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getReposts(@PathVariable Long id){

		return tweetService.getReposts(id);

	}

	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getReplies(@PathVariable Long id){

		return tweetService.getReplies(id);

	}

	@GetMapping("/{id}/context")
	public ContextDto getContext(@PathVariable Long id){

		return tweetService.getContext(id);

	}

	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getLikers(@PathVariable Long id){

		return tweetService.getLikers(id);

	}

	@PostMapping("/{id}/reply")
	public TweetResponseDto postReply(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto){

		return tweetService.postReply(id, tweetRequestDto);

	}




}
