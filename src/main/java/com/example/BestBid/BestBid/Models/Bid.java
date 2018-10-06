package com.example.BestBid.BestBid.Models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;


@Entity
public class Bid {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( columnDefinition="INTEGER(10) UNSIGNED")
	private Integer bidId;

	@JoinColumn(name="project_id",columnDefinition="INTEGER(10) UNSIGNED",foreignKey=@ForeignKey(name="project_projectid"))
	private Integer projectId;
	
	@Column( columnDefinition="INTEGER(10) UNSIGNED")
	private Integer userId;
	
	@Column
	@Min(0)
	private Integer bidAmount;
	
	@Column
    private Date bidAt;
	
	public Bid() {}

	public Bid(Integer bidId, Integer projectId, Integer userId, @Min(0) Integer bidAmount, Date bidAt) {
		super();
		this.bidId = bidId;
		this.projectId = projectId;
		this.userId = userId;
		this.bidAmount = bidAmount;
		this.bidAt = bidAt;
	}

	public Integer getBidId() {
		return bidId;
	}

	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(Integer bidAmount) {
		this.bidAmount = bidAmount;
	}

	public Date getBidAt() {
		return bidAt;
	}

	public void setBidAt(Date bidAt) {
		this.bidAt = bidAt;
	};

	
	
	
}
