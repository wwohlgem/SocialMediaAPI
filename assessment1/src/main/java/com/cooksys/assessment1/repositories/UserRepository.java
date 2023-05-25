package com.cooksys.assessment1.repositories;

import com.cooksys.assessment1.entities.Credentials;
import com.cooksys.assessment1.model.CredentialsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.assessment1.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	Optional<User> findByCredentialsAndDeletedFalse(Credentials credentials);

	Optional<User> findByCredentials_UsernameAndDeletedFalse(String username);
	Optional<User> findByIdAndDeletedFalse(Long id);

	Optional<User> findById(Long id);

	List<User> findAllByDeletedFalse();

}
