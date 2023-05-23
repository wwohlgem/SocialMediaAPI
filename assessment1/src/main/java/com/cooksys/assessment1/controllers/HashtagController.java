package com.cooksys.assessment1.controllers;

import com.cooksys.assessment1.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Mapping Should be the path on the API. Changed to /tags
@RequestMapping("/tags")
@RequiredArgsConstructor
public class HashtagController {
    private final HashtagService hashtagService;

}
