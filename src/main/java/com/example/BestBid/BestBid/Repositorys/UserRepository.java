package com.example.BestBid.BestBid.Repositorys;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import com.example.BestBid.BestBid.Models.User;


public interface UserRepository extends CrudRepository<User,Integer>{

	public Optional<User> getUserByUserId(Integer userId);

	public Optional<User> getUserByUserName(String userName);

	public Optional<User> getUserByEmail(String email);
	
	
}