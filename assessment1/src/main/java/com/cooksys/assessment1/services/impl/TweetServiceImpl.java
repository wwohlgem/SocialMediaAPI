package com.cooksys.assessment1.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.exceptions.BadRequestException;
import com.cooksys.assessment1.exceptions.NotAuthorizedException;
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
	// private final UserMapper userMapper;

	private final ValidateService validateService;

	private Tweet getTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("Tweet could not be found");
		}
		return optionalTweet.get();
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

	private void parseHashtagsAndMentions(Tweet tweet) {
		String[] wordsInContent = tweet.getContent().split("\\s+");
		for (String word : wordsInContent) {
			if (word.startsWith("#")) {
				String tag = word.substring(1);
				Optional<Hashtag> optionalHashtag = hashtagRepository
						.findHashtagByLabel(tag);
				if (optionalHashtag.isEmpty()) {
					Hashtag hashtag = new Hashtag();
					hashtag.setLabel(tag);
					hashtag.setFirstUsed(Timestamp.valueOf(LocalDateTime.now()));
					hashtagRepository.saveAndFlush(hashtag);
					tweet.getHashtags().add(hashtag);
					tweetRepository.saveAndFlush(tweet);
				} else {
					optionalHashtag.get().setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
					hashtagRepository.saveAndFlush(optionalHashtag.get());
				}
			}
			if (word.startsWith("@")) {
				String username = word.substring(1);
				Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
				if (optionalUser.isPresent()) {
					tweet.getMentions().add(optionalUser.get());
					tweetRepository.saveAndFlush(tweet);
				}
			}
		}
	}

	@Override
	@Transactional
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto == null || tweetRequestDto.getContent() == null
				|| tweetRequestDto.getCredentials() == null) {
			throw new BadRequestException("The content and credentials are required fields");
		}
		User author = getUserByCredentials(tweetRequestDto.getCredentials());
		Tweet tweetToSave = new Tweet();
		tweetToSave.setAuthor(author);
		tweetToSave.setContent(tweetRequestDto.getContent());
		parseHashtagsAndMentions(tweetToSave);
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
			return;
		}
		tweetToLike.getLikes().add(currentUser);
		currentUser.getLikedTweets().add(tweetToLike);
		tweetRepository.saveAndFlush(tweetToLike);
		userRepository.saveAndFlush(currentUser);
	}

	private Tweet validateAndGetTweetById(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("No tweet with that id found");
		}
		return optionalTweet.get();
	}

	private User getUserByCredentials(CredentialsDto credentialsDto) {
		if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new NotAuthorizedException("Credentials are required");
		}
		Optional<User> optionalUser = userRepository
				.findByCredentialsUsernameAndDeletedFalse(credentialsDto.getUsername());
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User not found");
		}
		if (!optionalUser.get().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			throw new NotAuthorizedException("Password is invalid");
		}
		return optionalUser.get();
	}

	@Override
	public List<UserResponseDto> getUsersMentioned(Long id) {
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
		Tweet tweetToRepost = validateAndGetTweetById(id);
		User author = getUserByCredentials(credentialsDto);
		Tweet repostedTweet = new Tweet();

		repostedTweet.setAuthor(author);
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
		Tweet tweet = validateAndGetTweetById(id);
		List<Tweet> reposts = new ArrayList<>();
		for (Tweet repost : tweet.getReposts()) {
			if (!repost.isDeleted()) {
				reposts.add(repost);
			}
		}
		return tweetMapper.entitiesToDtos(reposts);
	}

	@Override
	public List<TweetResponseDto> getReplies(Long id) {
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
		// want to return the ContextDto for the given tweet
		// send error if tweet doesn't exists
		// we want to exclude deleted replies, but want to still include the replies to
		// the deleted reply (if they aren't deleted)
		Tweet targetTweet = validateAndGetTweetById(id);
		List<Tweet> tweetsBefore = new ArrayList<>();
		List<Tweet> tweetsAfter = new ArrayList<>();

		// if the reply is not deleted add it
		// if it is deleted, add the non-deleted replies to the replies
		// maybe need a while loop here?
		List<Tweet> replies = targetTweet.getReplies();
		for (Tweet t : replies) {
			if (!t.isDeleted()) {
				tweetsAfter.add(t);
			}
			replies.addAll(t.getReplies());
		}

		// need to get all the tweets that came before it, starting at the beginning
		// tweet in the thread
		for (Tweet inReplyTo = targetTweet.getInReplyTo(); inReplyTo != null; inReplyTo = inReplyTo.getInReplyTo()) {
			if (!inReplyTo.isDeleted()) {
				tweetsBefore.add(inReplyTo);
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
		Tweet tweet = validateAndGetTweetById(id);
		Set<User> likers = tweet.getLikes();
		return userMapper.entityToDtos(likers.stream().filter(u -> !u.isDeleted()).collect(Collectors.toList()));
	}

	@Override
	public TweetResponseDto postReply(Long id, TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto == null || tweetRequestDto.getContent() == null
				|| tweetRequestDto.getCredentials() == null) {
			throw new BadRequestException("The content and credentials are required fields");
		}
		User author = getUserByCredentials(tweetRequestDto.getCredentials());
		Tweet tweetToSave = new Tweet();
		tweetToSave.setInReplyTo(validateAndGetTweetById(id));
		tweetToSave.setAuthor(author);
		tweetToSave.setContent(tweetRequestDto.getContent());
		parseHashtagsAndMentions(tweetToSave);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToSave));
	}
}
