package com.example.BestBid.BestBid.Controllers;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.BestBid.BestBid.Models.User;
import com.example.BestBid.BestBid.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


@CrossOrigin(allowCredentials="true")
@RestController
public class UserController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserService UserService;
	
	
	@RequestMapping(method=RequestMethod.POST,value="/login", produces = "application/json")
	public String loginUser(@RequestBody String body) throws JSONException {
		
		JSONObject obj = (JSONObject) new JSONTokener(body).nextValue();
		String userName=obj.get("userName").toString();
		String password=obj.get("password").toString();
		
		JSONObject response = new JSONObject();
		
		Optional<User> currentUser = UserService.getUserByUserName(userName);	
		
		if(currentUser.isPresent()) {
			
			User loggedUser = currentUser.get();
			
			if(loggedUser.getPassword().equals(password)) {
					
				loggedUser.setLastLogin(new Date());
				UserService.updateUser(loggedUser);
					
				
				if(!loggedUser.getAccountStatus().equalsIgnoreCase("active")) {
					response.put("type","fail");
					response.put("message","ACCOUNT "+loggedUser.getAccountStatus());
					return response.toString();
				}
					
				session.setMaxInactiveInterval(60*15);
				session.setAttribute("User", loggedUser);
				
				response.put("type","success");
				response.put("message","logged in");
				
			}
			else{
				response.put("type","fail");
				response.put("message","Invalid Password");
			}
		}
		else {
			response.put("type","fail");
			response.put("message","Username doesnot exist");
		}
		return response.toString();
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value="/register", produces = "application/json")
	public String addUser(@RequestBody String body) throws JSONException {
		
		JSONObject obj = (JSONObject) new JSONTokener(body).nextValue();
		String userName=obj.get("userName").toString();
		String password=obj.get("password").toString();
		String email = obj.get("email").toString();
		
		JSONObject response = new JSONObject();
		
		User user = new User();
		
		if(userName!=null || email!=null || password!=null){
			user.setUserName(userName);
			user.setEmail(email);
			user.setPassword(password);
		}
		else
		{
			response.put("type","fail");
			response.put("message","Null user object");	
			return response.toString();
		}
			
		String validationMessage = UserService.validateFields(user);
			
		user.setAccountStatus("ACTIVE");
		user.setLastLogin(new Date());
		
		if(validationMessage.equals("success")) {		
			UserService.addUser(user);		
			response.put("type","success");
			response.put("message","Account created");	
			
		}
		else {
			response.put("type","fail");
			response.put("message",validationMessage);	
		}		
		
		return response.toString();	
	}
		

	@RequestMapping(method=RequestMethod.POST,value="/logout", produces = "application/json")
	public String logout() throws JSONException {
			
		JSONObject response = new JSONObject();
		
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
		}
		else {
			session.invalidate();
			response.put("type","success");
			response.put("message","Logged out");
		}

		response.put("redirect","login");			
		return response.toString();		
	}


	@RequestMapping(method=RequestMethod.GET,value="/getUserDetails", produces = "application/json")
	public String userDetails() throws JSONException, JsonProcessingException {
		JSONObject response = new JSONObject();
		
		if(session.getAttribute("loggedUser")==null) {
			response.put("type","fail");
			response.put("message","Login needed");
		}
		else {
			response.put("type","success");
			User userDetails = (User) session.getAttribute("loggedUser");
			userDetails.setPassword("removed");
			ObjectWriter write = new ObjectMapper().writer();
			response.put("user",new JSONObject(write.writeValueAsString(userDetails)));			
				
		}				
		
		return response.toString();
	}
	

}
