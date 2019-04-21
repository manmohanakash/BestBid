package com.example.BestBid.BestBid.Controllers;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpSession;

import com.example.BestBid.BestBid.Models.Bid;
import com.example.BestBid.BestBid.Models.MyUserPrincipal;
import com.example.BestBid.BestBid.Services.UserService;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BestBid.BestBid.Models.Project;
import com.example.BestBid.BestBid.Services.BidService;
import com.example.BestBid.BestBid.Services.ProjectService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

@RestController
public class ProjectController {
	
	@Autowired
	private ProjectService ProjectService;
	
	@Autowired
	private BidService BidService;

	@Autowired
	private UserService UserService;

	@Secured("ROLE_USER")
	@RequestMapping(method=RequestMethod.GET,value="/project/{projectId}", produces = "application/json")
	public String getProject(@PathVariable Integer projectId ) {
		Optional<Project> project = ProjectService.getProjectByProjectId(projectId);
		JsonObject response = new JsonObject();
		if(project.isPresent())	response.add("project",new Gson().toJsonTree(project.get()));
		else response.addProperty("message","Project does not exist");
		return response.toString();
	}

	@Secured("ROLE_USER")
	@RequestMapping(method=RequestMethod.POST,value="/addProject", produces = "application/json")
	public String addProject(Principal userDetails,@RequestBody Project project) {
		project.setOwnerId(UserService.getUserByUserName(userDetails.getName()).get().getUserId());
		return ProjectService.addProject(project);
	}

	@Secured("ROLE_USER")
	@RequestMapping(method=RequestMethod.DELETE,value="/project/{projectId}", produces = "application/json")
	public String deleteProject(@PathVariable Integer projectId) throws JSONException {
		JSONObject response = new JSONObject();
		MyUserPrincipal userDetails = (MyUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<Project> projectDb = ProjectService.getProjectByProjectId(projectId);
		
		if(projectDb.isPresent()) {
			if(projectDb.get().getOwnerId()==userDetails.getUserId()) {
				ProjectService.deleteProject(projectId);
				BidService.deleteBidsForProjectId(projectId);
				response.put("type","success");
				response.put("message","Project Deleted");
			}
			else {
				response.put("type","fail");
				response.put("message","Project of others cannot be deleted.");
			}
		}else {
			response.put("type","fail");
			response.put("message","Project does not exist!");
		}
		return response.toString();
	}

	@Secured("ROLE_USER")
	@RequestMapping(method=RequestMethod.GET,value="/getMyProjects", produces = "application/json")
	public String getMyProjects(Pageable page)  {
		MyUserPrincipal userDetails = (MyUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Project> projects = ProjectService.getProjectByOwnerId(userDetails.getUserId(),page);
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
	}

	@Secured("ROLE_USER")
	@RequestMapping(method=RequestMethod.GET,value="/getProjects", produces = "application/json")
	public String getProjects(Pageable page)  {
		Page<Project> projects = ProjectService.getProjects(page);
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
	}

	@Secured("ROLE_USER")
	@RequestMapping(method=RequestMethod.GET,value="/getProjectsByType", produces = "application/json")
	public String getProjectsByType(@RequestParam String workType,Pageable page)  {
		Page<Project> projects = ProjectService.getProjectByWorkType(workType,page);
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
	}

	@Secured("ROLE_USER")
	@RequestMapping(method=RequestMethod.GET,value="/getProjectsByName", produces = "application/json")
	public String getProjectsByName(@RequestParam String projectName,Pageable pageable)  {
		Page<Project> projects = ProjectService.getProjectByName(projectName,pageable);
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
	}

	@Secured("ROLE_USER")
	@RequestMapping(method=RequestMethod.GET,value="/getProjectsWithDeadlineRemaining", produces = "application/json")
	public String getProjectsWithDeadlineRemaining(Pageable pageable) {
		Page<Project> projects = ProjectService.getProjectsWithDealineRemaining(pageable);
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
	}
}
