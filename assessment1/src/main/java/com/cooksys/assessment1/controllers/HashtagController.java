package com.cooksys.assessment1.controllers;

import com.cooksys.assessment1.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hashtags")
@RequiredArgsConstructor
public class HashtagController {
	
    private final HashtagService hashtagService;

}
