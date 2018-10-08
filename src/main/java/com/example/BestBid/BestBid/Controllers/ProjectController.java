package com.example.BestBid.BestBid.Controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BestBid.BestBid.Models.Project;
import com.example.BestBid.BestBid.Models.User;
import com.example.BestBid.BestBid.Services.BidService;
import com.example.BestBid.BestBid.Services.ProjectService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;



@CrossOrigin(allowCredentials="true")
@RestController
public class ProjectController {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private ProjectService ProjectService;
	
	@Autowired
	private BidService BidService;
	
	
	@RequestMapping(method=RequestMethod.POST,value="/addProject", produces = "application/json")
	public String addProject(HttpSession session,@RequestBody String body) throws JSONException, ParseException {

		
		JSONObject obj = (JSONObject) new JSONTokener(body).nextValue();	
		String projectName = obj.get("projectName").toString();
		String description = obj.get("description").toString();
		String workType = obj.get("workType").toString();
		String maximumBudget = obj.get("maximumBudget").toString();
		String deadline = obj.get("deadline").toString();
		
		JSONObject response = new JSONObject();
		
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		User currentUser = (User) session.getAttribute("User");
			
		Project newProject = new Project();
		newProject.setCreatedAt(new Date());
		newProject.setProjectName(projectName);
		newProject.setDescription(description);
		newProject.setWorkType(workType);
		newProject.setMaximumBudget(Integer.parseInt(maximumBudget));
		newProject.setOwnerId(currentUser.getUserId());
		newProject.setTotalBids(0);
		
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		newProject.setDeadline(formatter.parse(deadline));
		
		ProjectService.addProject(newProject); 
		response.put("type","success");
		response.put("message","Project Added");
		return response.toString();
	}
	
	
	@RequestMapping(method=RequestMethod.GET,value="/getProject/{projectId}", produces = "application/json")
	public String getProject(HttpSession session,@PathVariable Integer projectId ) throws JSONException, ParseException {

		JSONObject response = new JSONObject();
		
		Optional<Project> project = ProjectService.getProjectByProjectId(projectId);
		
		if(project.isPresent())
			return new Gson().toJson(project);
		else
		{
			response.put("type", "fail");
			return response.toString();
		}
		
	}

	
	@RequestMapping(method=RequestMethod.DELETE,value="/deleteMyProject/{projectId}", produces = "application/json")
	public String deleteProject(HttpSession session,@PathVariable Integer projectId) throws JSONException, ParseException {
		
		JSONObject response = new JSONObject();
		
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		User currentUser = (User) session.getAttribute("User");
		
		Optional<Project> projectDb = ProjectService.getProjectByProjectId(projectId);
		
		if(projectDb.isPresent()) {
			if(projectDb.get().getOwnerId()==currentUser.getUserId()) {
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
	

	@RequestMapping(method=RequestMethod.GET,value="/getMyProjects", produces = "application/json")
	public String getMyProjects(Pageable page) throws JSONException {
		
		JSONObject response = new JSONObject();	
		
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		User currentUser = (User) session.getAttribute("User");
		
		Page<Project> projects = ProjectService.getProjectByOwnerId(currentUser.getUserId(),page);
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
		
	}

	
	@RequestMapping(method=RequestMethod.GET,value="/getProjects", produces = "application/json")
	public String getProjects(Pageable page) throws JSONException {

		JSONObject response = new JSONObject();	
		
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		Page<Project> projects = ProjectService.getProjects(page);
		
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
		
	}

	
	@RequestMapping(method=RequestMethod.GET,value="/getProjectsByType", produces = "application/json")
	public String getProjectsByType(@RequestParam String workType,Pageable page) throws JSONException {
		
		JSONObject response = new JSONObject();
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		Page<Project> projects = ProjectService.getProjectByWorkType(workType,page);
		
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/getProjectsByName", produces = "application/json")
	public String getProjectsByName(@RequestParam String projectName,Pageable pageable) throws JSONException {
		
		JSONObject response = new JSONObject();
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		Page<Project> projects = ProjectService.getProjectByName(projectName,pageable);
		
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/getProjectsWithDealineRemaining", produces = "application/json")
	public String getProjectsWithDealineRemaining(Pageable pageable) throws JSONException {
		
		JSONObject response = new JSONObject();
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		Page<Project> projects = ProjectService.getProjectsWithDealineRemaining(pageable);
		
		JsonElement jsonElement = new Gson().toJsonTree(projects);
		jsonElement.getAsJsonObject().addProperty("totalPages",projects.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");	
		return jsonElement.toString();
	}
	
}
