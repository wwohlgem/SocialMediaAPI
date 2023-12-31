package com.cooksys.assessment1.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.exceptions.BadRequestException;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.CredentialsMapper;
import com.cooksys.assessment1.mappers.HashtagMapper;
import com.cooksys.assessment1.mappers.TweetMapper;
import com.cooksys.assessment1.mappers.UserMapper;
import com.cooksys.assessment1.model.ContextDto;
import com.cooksys.assessment1.model.CredentialsDto;
import com.cooksys.assessment1.model.HashtagDto;
import com.cooksys.assessment1.model.TweetRequestDto;
import com.cooksys.assessment1.model.TweetResponseDto;
import com.cooksys.assessment1.model.UserResponseDto;
import com.cooksys.assessment1.repositories.HashtagRepository;
import com.cooksys.assessment1.repositories.TweetRepository;
import com.cooksys.assessment1.repositories.UserRepository;
import com.cooksys.assessment1.services.TweetService;
import com.cooksys.assessment1.services.ValidateService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;
	private final UserMapper userMapper;
	private final HashtagMapper hashtagMapper;
	private final CredentialsMapper credentialsMapper;
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;

	private final TweetMapper tweetMapper;
//	private final UserMapper userMapper;

	private final ValidateService validateService;

	private Tweet validateAndGetTweetById(Long id) {

		if (tweetRepository.findByIdAndDeletedFalse(id).isEmpty()) {
			throw new NotFoundException("No tweet with that id found");
		} else
			return tweetRepository.findByIdAndDeletedFalse(id).get();

	}

	private void validateUserByCredentials(CredentialsDto credentialsDto) {

		// get the optional user. If it is empty, throw Not Found Exception
		if (userRepository.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(credentialsDto)).isEmpty()) {
			throw new NotFoundException("User not found");
		}
	}

	private Tweet getTweet(Long tweetId) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("No tweet found with specified id");
		}
		Tweet tweet = optionalTweet.get();
		if (tweet.isDeleted() == true) {
			throw new BadRequestException("Tweet with specified id has already been flagged as deleted");
		}
		return tweet;
	}

	private User getUserByUsername(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user found.");
		}
		User user = optionalUser.get();
		if (user.isDeleted()) {
			throw new BadRequestException("User has been flagged as deleted.");
		}
		return user;
	}

	@Override
	public List<TweetResponseDto> getAllTweets() {

		/*
		 * Retrieves all (non-deleted) tweets. The tweets should appear in
		 * reverse-chronological order.
		 */

		List<Tweet> allTweets = new ArrayList<>();
		for (Tweet tweet : tweetRepository.findAll()) {
			if (!tweet.isDeleted()) {
				allTweets.add(tweet);
			}
		}
		return tweetMapper.entitiesToDtos(allTweets);
	}

	@Override
	public TweetResponseDto getTweetById(Long id) {

		/*
		 * Retrieves a tweet with a given id. If no such tweet exists, or the given
		 * tweet is deleted, an error should be sent in lieu of a response.
		 */

		Tweet queriedTweet = getTweet(id);
		return tweetMapper.entityToDto(queriedTweet);
	}

	@Override
	public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {

		/*
		 * "Deletes" the tweet with the given id. If no such tweet exists or the
		 * provided credentials do not match author of the tweet, an error should be
		 * sent in lieu of a response. If a tweet is successfully "deleted", the
		 * response should contain the tweet data prior to deletion.
		 *
		 * IMPORTANT: This action should not actually drop any records from the
		 * database! Instead, develop a way to keep track of "deleted" tweets so that
		 * even if a tweet is deleted, data with relationships to it (like replies and
		 * reposts) are still intact.
		 */

		Tweet queriedTweet = getTweet(id);

		if (validateService.checkUsernameExists(credentialsDto.getUsername())) {
			queriedTweet.setDeleted(true);
			tweetRepository.saveAndFlush(queriedTweet);
			return tweetMapper.entityToDto(queriedTweet);
		} else {
			throw new BadRequestException("The specified user does not exist");
		}
	}

	@Override
	@Transactional
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto.getContent() == null || tweetRequestDto.getCredentials() == null
				|| tweetRequestDto.getCredentials().getPassword() == null) {
			throw new BadRequestException("The content and credentials are required fields");
		}

		Tweet tweetToSave = tweetMapper.dtoToEntity(tweetRequestDto);
		CredentialsDto credentialsDto = tweetRequestDto.getCredentials();
		Optional<User> optionalUser = userRepository
				.findByCredentials_UsernameAndDeletedFalse(credentialsDto.getUsername());
		if (optionalUser.isEmpty() || optionalUser.get().isDeleted()) {
			throw new NotFoundException("No user found by that username");
		}

		User tweetAuthor = optionalUser.get();
		tweetToSave.setAuthor(tweetAuthor);

		List<User> mentions = new ArrayList<>();
		List<Hashtag> allHashtags = new ArrayList<>();
		String[] wordsInContent = tweetRequestDto.getContent().split("\\s+");

		for (String word : wordsInContent) {
			if (word.startsWith("#")) {

				Optional<Hashtag> optionalHashtag = hashtagRepository.findHashtagByLabel(word.substring(1));
				Hashtag hashtag = new Hashtag();
				hashtag.setLabel(word.substring(1));
				hashtag.setFirstUsed(tweetToSave.getPosted());
				if (!optionalHashtag.isPresent()) {
					hashtag.setLabel(word.substring(1));
					allHashtags.add(hashtag);

				} else {
					optionalHashtag.get().setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
				}
				tweetToSave.setHashtags(allHashtags);
				hashtagRepository.saveAndFlush(hashtag);
			}
			if (word.startsWith("@")) {
				User mentionedUser = getUserByUsername(word.substring(1));
				mentions.add(mentionedUser);
			}
			tweetToSave.setMentions(mentions);
		}
		tweetToSave.setHashtags(allHashtags);
		tweetToSave.setContent(tweetRequestDto.getContent());
		userRepository.saveAndFlush(tweetAuthor);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToSave));

	}

	@Override
	public void addLikeToTweet(Long id, CredentialsDto credentialsDto) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("The specified tweet does not exist.");
		}
		Tweet tweetToLike = optionalTweet.get();
		if (tweetToLike.isDeleted()) {
			throw new BadRequestException("The specified tweet appears to have been deleted");
		}
		User currentUser = getUserByUsername(credentialsDto.getUsername());
		if (tweetToLike.getLikes().contains(currentUser)) {
			throw new BadRequestException("You have already like this tweet");
		}
		tweetToLike.getLikes().add(currentUser);
		currentUser.getLikedTweets().add(tweetToLike);
		tweetRepository.saveAndFlush(tweetToLike);
		userRepository.saveAndFlush(currentUser);
	}

	@Override
	public List<UserResponseDto> getUsersMentioned(Long id) {
		// retrieves the users mentioned in the tweet that has the given id
		// if that tweet is deleted or doesn't exist, an error should be sent
		// deleted users should not be included

		// make sure the relationship between tweet and mentions is set up correctly

		Tweet tweetToParse = validateAndGetTweetById(id);

		List<User> usersMentioned = new ArrayList<>();

		for (User u : tweetToParse.getMentions()) {

			if (!u.isDeleted()) {
				usersMentioned.add(u);
			}
		}
		return userMapper.entityToDtos(usersMentioned);
	}

	@Override
	public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
		// creates a repost of the tweet with given id
		// the author of the repost should match the user in the credentials provided (
		// so setAuthor() to our user )
		// if tweet doesn't exist or credentials do not match a user send error

		// content is not allowed. RepostOf property must be set.

		// respond with the new tweet

		validateUserByCredentials(credentialsDto);
		Tweet tweetToRepost = validateAndGetTweetById(id);
		Tweet repostedTweet = new Tweet();

		User userAuthor = userRepository.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(credentialsDto))
				.get();

		repostedTweet.setContent(tweetToRepost.getContent());
		repostedTweet.setAuthor(userAuthor);
		repostedTweet.setRepostOf(tweetToRepost);

		tweetRepository.saveAndFlush(repostedTweet);

		tweetToRepost.getReposts().add(tweetToRepost);
		tweetRepository.saveAndFlush(tweetToRepost);

		return tweetMapper.entityToDto((repostedTweet));
	}

	@Override
	public List<HashtagDto> getHashtags(Long id) {

		Tweet tweet = validateAndGetTweetById(id);

		List<Hashtag> hashtags = tweet.getHashtags();

		return hashtagMapper.entitiesToDtos(hashtags);
	}

	@Override
	public List<TweetResponseDto> getReposts(Long id) {
		// retrieves the direct reposts as a list of tweets
		// if the given tweet doesn't exist, respond with error message
		// deleted reposts should be excluded

		Tweet tweet = validateAndGetTweetById(id);

		List<Tweet> reposts = new ArrayList<>();

		for (Tweet t : tweet.getReposts()) {

			if (!t.isDeleted()) {

				reposts.add(t);

			}
		}

		return tweetMapper.entitiesToDtos(reposts);
	}

	@Override
	public List<TweetResponseDto> getReplies(Long id) {
		// retrieves a list of the replies to the given tweet
		// deleted replies should be excluded

		Tweet tweet = validateAndGetTweetById(id);

		List<Tweet> replies = new ArrayList<>();

		for (Tweet t : tweet.getReplies()) {

			if (!t.isDeleted()) {

				replies.add(t);

			}
		}

		return tweetMapper.entitiesToDtos(replies);
	}

	@Override
	public ContextDto getContext(Long id) {

		ContextDto contextDto = new ContextDto();
		Tweet contextTweet = validateAndGetTweetById(id);

		if (contextTweet.isDeleted() || contextTweet.equals(null)) {
			throw new NotFoundException("Not Found");
		}

		Tweet beforeTweet = contextTweet.getInReplyTo();
		List<Tweet> before = new ArrayList<Tweet>();
		List<Tweet> after = contextTweet.getReplies();
		List<Tweet> afterTweets = new ArrayList<Tweet>();

		afterTweets.addAll(after);

		while (beforeTweet != null) {
			before.add(beforeTweet);
			beforeTweet = beforeTweet.getInReplyTo();
		}
		for (Tweet tweet : after) {
			if (tweet.getReplies() != null) {
				afterTweets.addAll(tweet.getReplies());
			}
		}
		for (Tweet tweet : before) {
			if (tweet.isDeleted()) {
				before.remove(tweet);
			}
		}
		for (Tweet tweet : after) {
			if (tweet.isDeleted()) {
				after.remove(tweet);
			}
		}

		contextDto.setBefore(tweetMapper.entitiesToDtos(before));
		contextDto.setTarget(tweetMapper.entityToDto(contextTweet));
		contextDto.setAfter(tweetMapper.entitiesToDtos(afterTweets));

		return contextDto;
	}

	@Override
	public List<UserResponseDto> getLikers(Long id) {
		// return a list of users who have like the tweet
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

		for(User u : allUsers) {

			if (tweetRequestDto.getContent().contains("@" + u.getCredentials().getUsername())) {

				mentionedUsers.add(u);

			}
		}


		String[] wordsInContent = tweetRequestDto.getContent().split("\\s+");

		for(String word : wordsInContent){
			if(word.startsWith("#")){

				Hashtag hashtag = new Hashtag();
				hashtag.setLabel(word.substring(1));
				hashtag.setFirstUsed(tweetToPost.getPosted());
				hashtags.add(hashtag);

				if(!allHashtags.contains(hashtag)){

					hashtagRepository.saveAndFlush(hashtag);

				}

			}
		}


		tweetToPost.setHashtags(hashtags);
		tweetToPost.setMentions(mentionedUsers);
		tweetToPost.setAuthor(user);
		tweetToPost.setContent(tweetRequestDto.getContent());
		tweetToPost.setInReplyTo(validateAndGetTweetById(id));
		tweetRepository.saveAndFlush(tweetToPost);


		//need to update the replies for the tweet i am replying to
		Tweet tweetReplyingTo = validateAndGetTweetById(id);
		tweetReplyingTo.getReplies().add(tweetToPost);
		tweetRepository.saveAndFlush(tweetReplyingTo);

		return tweetMapper.entityToDto(tweetToPost);

	}
}