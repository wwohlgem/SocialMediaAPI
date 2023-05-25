package com.cooksys.assessment1.services;

import com.cooksys.assessment1.model.*;

import java.util.List;

public interface TweetService {

    List<UserResponseDto> getUsersMentioned(Long id);

    TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto);

    List<HashtagDto> getHashtags(Long id);

    List<TweetResponseDto> getReposts(Long id);

    List<TweetResponseDto> getReplies(Long id);

    ContextDto getContext(Long id);

    List<UserResponseDto> getLikers(Long id);

    TweetResponseDto postReply(Long id, TweetRequestDto tweetRequestDto);
    
    List<TweetResponseDto> getAllTweets();

	TweetResponseDto getTweetById(Long id);

	TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	void addLikeToTweet(Long id, CredentialsDto credentialsDto);

}
