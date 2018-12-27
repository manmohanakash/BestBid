package com.example.BestBid.BestBid.Models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( columnDefinition="INTEGER(10) UNSIGNED")
	private Integer projectId;

	@Column(length=25,nullable=false)
	private String projectName;
	
	@Column
	private Integer ownerId;
	
    @Column(columnDefinition="TEXT")
    private String description;

    @Column
	private String workType;
    
    @Column
    private Integer maximumBudget;
    
    @Column
    private Integer lowestBid;
    
    @Column
    private Integer lowestBidder;
    
    @Column
    private Date deadline;

    @Column
    private Date createdAt;
    
    @Column
    private Integer totalBids;
   
    public Project() {}

	public Project(Integer projectId, String projectName, Integer ownerId, String description, String workType,
			Integer maximumBudget, Integer lowestBid, Integer lowestBidder, Date deadline, Date createdAt,
			Integer totalBids) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.ownerId = ownerId;
		this.description = description;
		this.workType = workType;
		this.maximumBudget = maximumBudget;
		this.lowestBid = lowestBid;
		this.lowestBidder = lowestBidder;
		this.deadline = deadline;
		this.createdAt = createdAt;
		this.totalBids = totalBids;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public Integer getMaximumBudget() {
		return maximumBudget;
	}

	public void setMaximumBudget(Integer maximumBudget) {
		this.maximumBudget = maximumBudget;
	}

	public Integer getLowestBid() {
		return lowestBid;
	}

	public void setLowestBid(Integer lowestBid) {
		this.lowestBid = lowestBid;
	}

	public Integer getlowestBidder() {
		return lowestBidder;
	}

	public void setlowestBidder(Integer lowestBidder) {
		this.lowestBidder = lowestBidder;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getTotalBids() {
		return totalBids;
	}

	public void setTotalBids(Integer totalBids) {
		this.totalBids = totalBids;
	}


	@Override
	public String toString() {
		return "Project{" +
				"projectId=" + projectId +
				", projectName='" + projectName + '\'' +
				", ownerId=" + ownerId +
				", description='" + description + '\'' +
				", workType='" + workType + '\'' +
				", maximumBudget=" + maximumBudget +
				", lowestBid=" + lowestBid +
				", lowestBidder=" + lowestBidder +
				", deadline=" + deadline +
				", createdAt=" + createdAt +
				", totalBids=" + totalBids +
				'}';
	}
}
