package com.cooksys.assessment1.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.HashtagMapper;
import com.cooksys.assessment1.mappers.TweetMapper;
import com.cooksys.assessment1.model.HashtagDto;
import com.cooksys.assessment1.model.TweetResponseDto;
import com.cooksys.assessment1.repositories.HashtagRepository;
import com.cooksys.assessment1.repositories.TweetRepository;
import com.cooksys.assessment1.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    private final TweetRepository tweetRepository;

    private final HashtagMapper hashtagMapper;

    private final TweetMapper tweetMapper;

    @Override
    public List<HashtagDto> getAllHashtags() {
        List<Hashtag> allHashtags = hashtagRepository.findAll();
        return hashtagMapper.entitiesToDtos(allHashtags);
    }

    @Override
    public List<TweetResponseDto> getTagsByLabel(String label) {
        Optional<Hashtag> queriedHashtag = hashtagRepository.findHashtagByLabel(label);

        if(queriedHashtag.isEmpty()) {
            throw new NotFoundException("The requested Hashtag doesn't exist");
        }

        List<Tweet> tweetsWithTag = new ArrayList<>();

        for(Tweet tweet : tweetRepository.findAll()){
            if(!tweet.isDeleted() && tweet.getHashtags().contains(queriedHashtag.get())){
                tweetsWithTag.add(tweet);
            }
        }

        return tweetMapper.entitiesToDtos(tweetsWithTag);
    }
}
