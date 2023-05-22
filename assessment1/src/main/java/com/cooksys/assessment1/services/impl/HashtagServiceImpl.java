package com.cooksys.assessment1.services.impl;

import com.cooksys.assessment1.services.HashtagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private HashtagRepoitory hashtagRepoitory;

}
