package com.cooksys.assessment1.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	
	private Long id;
	
	private LocalDateTime joined;
	
	

}
