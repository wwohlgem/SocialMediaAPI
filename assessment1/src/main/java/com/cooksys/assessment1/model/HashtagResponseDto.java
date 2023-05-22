package com.cooksys.assessment1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HashtagResponseDto {

	private Long id;

	private String label;

	private LocalDateTime firstUsed;

	private LocalDateTime lastUsed;

}
