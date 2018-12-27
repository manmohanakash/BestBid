package com.example.BestBid.BestBid.Controllers;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.BestBid.BestBid.Models.Project;
import com.example.BestBid.BestBid.Models.User;
import com.example.BestBid.BestBid.Models.Bid;

import com.example.BestBid.BestBid.Services.BidService;
import com.example.BestBid.BestBid.Services.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import ch.qos.logback.core.net.SyslogOutputStream;


@CrossOrigin(allowCredentials="true")
@RestController
public class BidController {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private BidService BidService;
	
	@Autowired
	private ProjectService ProjectService;

	@RequestMapping(method=RequestMethod.GET,value="/getMyBids", produces = "application/json")
	public String getMyBids(Pageable pageable) throws JSONException, JsonProcessingException {

		User currentUser = (User) session.getAttribute("User");
		Page<Bid> bids = BidService.getBidsByUserId(currentUser.getUserId(), pageable);
		
		JsonElement jsonElement = new Gson().toJsonTree(bids);
		jsonElement.getAsJsonObject().addProperty("totalPages",bids.getTotalPages());
		jsonElement.getAsJsonObject().addProperty("type","success");
		
		return jsonElement.toString();
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value="/placeMyBid", produces = "application/json")
	public String placeMyBid(@RequestBody String body) throws JSONException {
		
		JSONObject obj = (JSONObject) new JSONTokener(body).nextValue();
		int projectId = Integer.parseInt(obj.get("projectId").toString());
		int bidAmount = Integer.parseInt(obj.get("bidAmount").toString());

		JSONObject response = new JSONObject();
		
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		User currentUser = (User) session.getAttribute("User");
		int userId = currentUser.getUserId();
		
		Optional<Project> projectFound = ProjectService.getProjectByProjectId(projectId);
		
		if(projectFound.isPresent()) {
			Project project = projectFound.get();
			
			if(bidAmount<=project.getMaximumBudget()) 
			{
				Bid bid = new Bid();
				bid.setBidAmount(bidAmount);
				bid.setBidAt(new Date());
				bid.setUserId(userId);
				bid.setProjectId(projectId);
				BidService.addBid(bid);

				project.setTotalBids(project.getTotalBids()+1);			
				if(project.getLowestBid()==null || project.getLowestBid()>bidAmount) {
					project.setLowestBid(bidAmount);
					project.setlowestBidder(userId);
				}
				response.put("message","BID ADDED!");
				ProjectService.updateProject(project);	
				response.put("type","success" );
			}else {
				response.put("type","fail");
				response.put("message","bid cannot be higher than budget");
			}	
		}else {
			response.put("type","fail");
			response.put("message","project does not exist");
		}

		return response.toString();
	}
	
	
	@RequestMapping(method=RequestMethod.DELETE,value="/deleteMyBid/{bidId}", produces = "application/json")
	public String deleteMyBid(@PathVariable Integer bidId) throws JSONException {
		
		JSONObject response = new JSONObject();
		
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		User currentUser = (User) session.getAttribute("User");
		int userId = currentUser.getUserId();	
		Optional<Bid> bidFound = BidService.getbid(bidId);
		
		if(bidFound.isPresent() && bidFound.get().getUserId()==userId) {
			
			Bid bid = bidFound.get();
			Optional<Project> projectFound = ProjectService.getProjectByProjectId(bid.getProjectId());	
			BidService.deleteBid(bid);
			
			if(!projectFound.isPresent()) {
				response.put("type","fail");
				response.put("message","Could not find Project!");	
				return response.toString();
			}
			
			Project project=projectFound.get();

			List<Bid> leastBidders = BidService.getLowestBidForProject(project.getProjectId());
			
			if(leastBidders.size()==0) {
				project.setlowestBidder(null);
				project.setLowestBid(null);
			}
			else {
				Collections.sort(leastBidders,(b1,b2)-> b1.getBidAt().compareTo(b2.getBidAt()));
				Bid leastBid = leastBidders.get(0);
				project.setlowestBidder(leastBid.getUserId());
				project.setLowestBid(leastBid.getBidAmount());
			}
			

			project.setTotalBids(project.getTotalBids()-1);
			ProjectService.updateProject(project);
			response.put("type","success" );
			response.put("message","Bid Deleted!");
		}else if(bidFound.isPresent()){
			response.put("type","fail" );
			response.put("message","Cannot delete other bids!");
		}
		else {
			response.put("type","fail");
			response.put("message","Bid ID doesnot exist!");	
		}
		
		
		return response.toString();
	}
	
	
	@RequestMapping(method=RequestMethod.GET,value="/getBidsForProject/{projectId}", produces = "application/json")
	public String getBidforProject(@PathVariable Integer projectId,Pageable pageable) throws JSONException, JsonProcessingException {
		
		JSONObject response = new JSONObject();
		
		if (session.getAttribute("User")==null)  {
			response.put("type","fail");
			response.put("message","Not Logged In");
			return response.toString();
		}
		
		
		Page<Bid> bids = BidService.getBidsByProjectId(projectId, pageable);
		
		JsonElement jsonElement = new Gson().toJsonTree(bids);
		jsonElement.getAsJsonObject().addProperty("type","success");
		
		return jsonElement.toString();
		
	}
	
	
}
