package com.cooksys.assessment1.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ContextDto {
    private TweetResponseDto target;
    private List<TweetResponseDto> before;
    private List<TweetResponseDto> after;
    
    // custom constuctor for getContext()
    public ContextDto(TweetResponseDto target, List<TweetResponseDto> before, List<TweetResponseDto> after) {
        this.target = target;
        this.before = before;
        this.after = after;
    }

}
