package com.cooksys.assessment1.controllers;

import com.cooksys.assessment1.model.*;
import org.springframework.web.bind.annotation.*;

import com.cooksys.assessment1.services.TweetService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {
	private final TweetService tweetService;

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
