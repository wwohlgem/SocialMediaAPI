package com.cooksys.assessment1.model;

import com.cooksys.assessment1.entities.Tweet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ContextDto {
    private Tweet target;
    private List<Tweet> before;
    private List<Tweet> after;

}
