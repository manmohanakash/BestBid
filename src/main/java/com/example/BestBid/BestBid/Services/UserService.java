package com.example.BestBid.BestBid.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BestBid.BestBid.Models.User;
import com.example.BestBid.BestBid.Repositorys.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository UserRepository;
	

	public User addUser(User user) {
			return UserRepository.save(user);
	}
	
	public User updateUser(User user) {
		return UserRepository.save(user);	
	}
	
	public Optional<User> getUserByUserId(Integer userId) {
		return UserRepository.getUserByUserId(userId);
	}
	
	public Optional<User> getUserByUserName(String userName) {
		return UserRepository.getUserByUserName(userName);
	}
	
	public Optional<User> getUserByEmail(String userEmail) {
		return UserRepository.getUserByEmail(userEmail);
	}
	
	
	/**
	 * Validates if any important fields(UserName,FirstName,LastName,Password) are missing.
	 * @param user
	 * @return true or false
	 */
	public String validateFields(User user) {
		

		String emailRegex = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.] {0,1}[a-zA-Z]+";
		
		if(user==null ||   
				user.getUserName()==null ||	user.getUserName().equals("") ||
				user.getPassword()==null || user.getPassword().equals("") ||
				user.getEmail()==null || user.getEmail().equals(""))
			return "Fields cannot be empty";
		else if(!user.getUserName().matches("^[A-Za-z_][A-Za-z\\d_]*$")) 
			return "Username should be alphabets";
		else if(getUserByUserName(user.getUserName()).isPresent())
			return "Username already exists";
		else if(user.getEmail().matches(emailRegex))
			return "Enter Valid Email";
		else if(getUserByEmail(user.getEmail()).isPresent())
				return "Email already exists";
		
		return "success";
	}


	

}