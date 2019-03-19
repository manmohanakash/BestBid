package com.example.BestBid.BestBid.Services;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import com.example.BestBid.BestBid.Models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BestBid.BestBid.Models.User;
import com.example.BestBid.BestBid.Repositorys.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository UserRepository;

	@Autowired
	private RoleService RoleService;
	

	public String addUser(User user) {

		String validate = validate(user);

		if(!validate.equals("valid")) return validate;

		HashSet<Role> role = new HashSet<>();
		Role userRole = RoleService.getRolebyRole("ROLE_USER");
		role.add(userRole);
		user.setRoles(role);
		user.setAccountStatus("ACTIVE");
		user.setLastLogin(new Date());
		UserRepository.save(user);
		return "success";
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

	public void deleteByUserId(int id) {
		UserRepository.deleteById(id);
	}
	
	/**
	 * Validates if any important fields(UserName,FirstName,LastName,Password) are missing,
	 * Checks for exists username or email and does a regex check on username and email.
	 * @param user
	 * @return Message in String
	 */
	public String validate(User user) {

		System.out.println(user.toString());

		String emailRegex = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.] {0,1}[a-zA-Z]+";
		String userNameRegex = "^[A-Za-z_][A-Za-z\\d_]*$";

		if(user==null) return "User cannot be null";
		else if(user.getUserName()==null) return "User name cannot be null";
		else if(user.getPassword()==null) return "User password cannot be null";
		else if(user.getEmail()==null) return "Email cannot be null";

		else if(user.getEmail().equals(""))	return "Email cannot be empty";
		else if(user.getUserName().equals("")) return "Username cannot be empty";
		else if(user.getPassword().equals("")) return "Password cannot be empty";

		else if(!user.getUserName().matches(userNameRegex)) return "Username should be alphabets";
		else if(!user.getEmail().matches(emailRegex)) return "Enter Valid Email";

		else if(getUserByUserName(user.getUserName()).isPresent()) return "Username already exists";
		else if(getUserByEmail(user.getEmail()).isPresent()) return "Email already exists";
		
		return "valid";
	}


}