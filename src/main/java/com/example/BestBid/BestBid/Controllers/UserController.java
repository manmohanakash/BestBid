package com.example.BestBid.BestBid.Controllers;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.BestBid.BestBid.Models.User;
import com.example.BestBid.BestBid.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class UserController {
	
	@Autowired
	private UserService UserService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping(method=RequestMethod.POST,value="/login", produces = "application/json")
	public String loginUser(@RequestBody String body) throws JSONException {

		JSONObject response = new JSONObject();

		User user = new Gson().fromJson(body,User.class);
		Optional<User> currentUser = UserService.getUserByUserName(user.getUserName());

		if(!currentUser.isPresent()){
			response.put("type","fail");
			response.put("message","Username doesnot exist");
			return response.toString();
		}

		User knownUser = currentUser.get();

		if(!bCryptPasswordEncoder.matches(user.getPassword(),knownUser.getPassword()))
		{
			response.put("type","fail");
			response.put("message","Invalid Password");
			return response.toString();
		}

		knownUser.setLastLogin(new Date());
		UserService.updateUser(knownUser);

		if(!knownUser.getAccountStatus().equalsIgnoreCase("active")) {
			response.put("type","fail");
			response.put("message","ACCOUNT "+knownUser.getAccountStatus());
			return response.toString();
		}
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(knownUser, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		response.put("type","success");
		response.put("message","logged in");
		return response.toString();
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value="/register", produces = "application/json")
	public String addUser(@RequestBody String body) throws JSONException {
		JSONObject response = new JSONObject();
		User user = new Gson().fromJson(body,User.class);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		String status = UserService.addUser(user);
		if(status.equals("success")) {
			response.put("type","success");
			response.put("message","Account created");
		}
		else {
			response.put("type","fail");
			response.put("message",status);
		}
		return response.toString();	
	}


	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.GET,value="/getAllUsers", produces = "application/json")
	public String getAllUsers( ){
		JsonElement jsonElement = new Gson().toJsonTree(UserService.getAllUsers());
		return jsonElement.toString();
	}

	@RequestMapping(method=RequestMethod.GET,value="/logout", produces = "application/json")
	public String logout() throws JSONException {
			
		JSONObject response = new JSONObject();

//		if (session.getAttribute("User")==null)  {
//			response.put("type","fail");
//			response.put("message","Not Logged In");
//		}
//		else {
//			response.put("type","success");
//			response.put("message","Logged out");
//		}

		response.put("redirect","login");			
		return response.toString();		
	}

	@Secured("ROLE_USER")
	@RequestMapping(method=RequestMethod.GET,value="/getUserDetails", produces = "application/json")
	public String userDetails(Principal userDetails) throws JSONException, JsonProcessingException {
		JSONObject response = new JSONObject();
		ObjectWriter write = new ObjectMapper().writer();
		response.put("type","success");
		response.put("user",new JSONObject(write.writeValueAsString(userDetails)));
		return response.toString();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.DELETE,value="/admin/deleteUser/{id}", produces = "application/json")
	public String userDetails(@PathVariable Integer id) throws JSONException {
		UserService.deleteByUserId(id);
		JSONObject response = new JSONObject();
		response.put("type","success");
		return response.toString();
	}
}
