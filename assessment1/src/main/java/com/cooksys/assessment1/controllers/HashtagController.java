package com.cooksys.assessment1.controllers;

import com.cooksys.assessment1.model.HashtagDto;
import com.cooksys.assessment1.services.HashtagService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Mapping Should be the path on the API. Changed to /tags
@RequestMapping("/tags")
@RequiredArgsConstructor
public class HashtagController {
	
    private final HashtagService hashtagService;
    
    @GetMapping
    public List<HashtagDto> getAllHashtags() {
    	return hashtagService.getAllHashtags();
    }
    
    @GetMapping("/{label}")
    public HashtagDto getTagByLabel(@PathVariable String label) {
    	return hashtagService.getTagByLabel(label);
    }

}
