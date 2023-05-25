package com.cooksys.assessment1.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.assessment1.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByCredentials_UsernameAndDeletedFalse(String username);

	Optional<User> findByCredentialsUsername(String username);

//	Optional<User> findByCredentialsUsernameAndDeletedFalse(String username);

//	List<User> findAllByNotDeleted();

//	List<User> findAllUsers();

}
