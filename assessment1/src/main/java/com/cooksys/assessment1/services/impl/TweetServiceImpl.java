package com.cooksys.assessment1.services.impl;

import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.CredentialsMapper;
import com.cooksys.assessment1.mappers.HashtagMapper;
import com.cooksys.assessment1.mappers.UserMapper;
import com.cooksys.assessment1.model.*;
import com.cooksys.assessment1.repositories.HashtagRepository;
import com.cooksys.assessment1.repositories.UserRepository;
import org.springframework.stereotype.Service;

import com.cooksys.assessment1.mappers.TweetMapper;
import com.cooksys.assessment1.repositories.TweetRepository;
import com.cooksys.assessment1.services.TweetService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final HashtagMapper hashtagMapper;
	private final CredentialsMapper credentialsMapper;

	private final HashtagRepository hashtagRepository;



	private Tweet validateAndGetTweetById(Long id){

		if(tweetRepository.findByIdAndDeletedFalse(id).isEmpty()){
			throw new NotFoundException("No tweet with that id found");
		}
		else return tweetRepository.findByIdAndDeletedFalse(id).get();

	}

	private void validateUserByCredentials(CredentialsDto credentialsDto){

		//get the optional user. If it is empty, throw Not Found Exception
		if(userRepository.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(credentialsDto)).isEmpty()){
			throw new NotFoundException("User not found");
		}
	}

	@Override
	public List<UserResponseDto> getUsersMentioned(Long id) {
		//retrieves the users mentioned in the tweet that has the given id
		//if that tweet is deleted or doesn't exist, an error should be sent
		//deleted users should not be included

		//make sure the relationship between tweet and mentions is set up correctly

		Tweet tweetToParse = validateAndGetTweetById(id);

		List<User> usersMentioned = new ArrayList<>();

		for(User u : tweetToParse.getMentions()){

			if(!u.isDeleted()) {
				usersMentioned.add(u);
			}
		}
		return userMapper.entityToDtos(usersMentioned);
	}

	@Override
	public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
		//creates a repost of the tweet with given id
		//the author of the repost should match the user in the credentials provided ( so setAuthor() to our user )
		//if tweet doesn't exist or credentials do not match a user send error

		//content is not allowed. RepostOf property must be set.

		//respond with the new tweet

		validateUserByCredentials(credentialsDto);
		Tweet tweetToRepost = validateAndGetTweetById(id);
		Tweet repostedTweet = new Tweet();

		User userAuthor = userRepository.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(credentialsDto)).get();

//		repostedTweet.setContent(tweetToRepost.getContent());
		repostedTweet.setAuthor(userAuthor);
		repostedTweet.setRepostOf(tweetToRepost);

		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(repostedTweet));
	}

	@Override
	public List<HashtagDto> getHashtags(Long id) {

		Tweet tweet = validateAndGetTweetById(id);

		List<Hashtag> hashtags = tweet.getHashtags();

		return hashtagMapper.entitiesToDtos(hashtags);
	}

	@Override
	public List<TweetResponseDto> getReposts(Long id) {
		//retrieves the direct reposts as a list of tweets
		//if the given tweet doesn't exist, respond with error message
		//deleted reposts should be excluded

		Tweet tweet = validateAndGetTweetById(id);

		List<Tweet> reposts = new ArrayList<>();

		for(Tweet t : tweet.getReposts()){

			if(!t.isDeleted()){

				reposts.add(t);

			}
		}

		return tweetMapper.entitiesToDtos(reposts);
	}

	@Override
	public List<TweetResponseDto> getReplies(Long id) {
		//retrieves a list of the replies to the given tweet
		//deleted replies should be excluded

		Tweet tweet = validateAndGetTweetById(id);

		List<Tweet> replies = new ArrayList<>();

		for(Tweet t : tweet.getReplies()){

			if(!t.isDeleted()){

				replies.add(t);

			}
		}

		return tweetMapper.entitiesToDtos(replies);
	}

	@Override
	public ContextDto getContext(Long id) {

		//want to return the ContextDto for the given tweet
		//send error if tweet doesn't exists
		//we want to exclude deleted replies, but want to still include the replies to the deleted reply (if they aren't deleted)

		Tweet targetTweet = validateAndGetTweetById(id);
		List<Tweet> tweetsBefore = new ArrayList<>();
		List<Tweet> tweetsAfter = new ArrayList<>();

		//if the reply is not deleted add it
		//if it is deleted, add the non-deleted replies to the replies
		//maybe need a while loop here?
		for(Tweet t : targetTweet.getReplies()){

			if(!t.isDeleted()){

				tweetsAfter.add(t);

			}
			else{

				for(Tweet tw : t.getReplies()){

					if(!tw.isDeleted()){
						tweetsAfter.add(tw);
					}
				}
			}
		}

		//need to get all the tweets that came before it, starting at the beginning tweet in the thread
		Tweet tweetTargetIsReplyingTo = targetTweet.getInReplyTo();

		for(Tweet t : tweetTargetIsReplyingTo.getReplies()){

			if(t.getPosted().before(targetTweet.getPosted()) && !t.isDeleted()){
				tweetsBefore.add(t);
			}
		}

		ContextDto contextDto = new ContextDto();
		contextDto.setTarget(tweetMapper.entityToDto(targetTweet));
		contextDto.setBefore(tweetMapper.entitiesToDtos(tweetsBefore));
		contextDto.setAfter(tweetMapper.entitiesToDtos(tweetsAfter));

		return contextDto;
	}

	@Override
	public List<UserResponseDto> getLikers(Long id) {
		//return a list of users who have like the tweet
		Tweet tweet = validateAndGetTweetById(id);

		List<User> likers = tweet.getLikes();
		return userMapper.entityToDtos(likers);
	}

	@Override
	public TweetResponseDto postReply(Long id, TweetRequestDto tweetRequestDto) {

		Tweet tweetToPost = new Tweet();
		validateUserByCredentials(tweetRequestDto.getCredentials());

		User user = userRepository.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials())).get();

		List<User> allUsers = userRepository.findAllByDeletedFalse();
		List<Hashtag> allHashtags = hashtagRepository.findAll();

		List<User> mentionedUsers = new ArrayList<>();
		List<Hashtag> hashtags = new ArrayList<>();
		List<Hashtag> newTags = new ArrayList<>();

		for(User u : allUsers) {

			if (tweetRequestDto.getContent().contains("@" + u.getCredentials().getUsername())) {

				mentionedUsers.add(u);

			}
		}

		for(Hashtag h : allHashtags){

			if (tweetRequestDto.getContent().contains("#" + h.getLabel())){

				hashtags.add(h);

			}
		}

		String[] wordsInContent = tweetRequestDto.getContent().split("\\s+");

		for(String word : wordsInContent){
			if(word.startsWith("#")){

				Hashtag hashtag = new Hashtag();
				hashtag.setLabel(word);
				hashtag.setFirstUsed(tweetToPost.getPosted());
				newTags.add(hashtag);

				if(!allHashtags.contains(hashtag)){
					hashtagRepository.saveAndFlush(hashtag);
				}

			}
		}

		List<Hashtag> allTagsTogether = new ArrayList<>();
		allTagsTogether.addAll(hashtags);
		allTagsTogether.addAll(newTags);

		tweetToPost.setHashtags(allTagsTogether);
		tweetToPost.setMentions(mentionedUsers);
		tweetToPost.setAuthor(user);
		tweetToPost.setInReplyTo(validateAndGetTweetById(id));//need to think about how to retrieve this info. We only get the user credentials and the tweet content


		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToPost));

	}
}
