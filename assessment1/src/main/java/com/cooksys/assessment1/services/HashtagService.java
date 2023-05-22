package com.cooksys.assessment1.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.assessment1.entities.Hashtag;

public interface HashtagService extends JpaRepository<Hashtag, Long> {
}
