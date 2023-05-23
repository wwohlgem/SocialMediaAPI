package com.cooksys.assessment1.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequestDto {
	
	private LocalDateTime joined;
	
	private boolean deleted;

}
