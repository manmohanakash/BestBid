package com.example.BestBid.BestBid.Models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name="email",columnNames = "email"),@UniqueConstraint(name="user_name",columnNames = "userName")})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( columnDefinition="INTEGER(10) UNSIGNED")
	@JsonIgnore
	private Integer userId;

	@Column(length=25,nullable=false)
	private String userName;
	
	@JsonIgnore
	@Column(length=25,nullable=false)
	private String password;
	
	@Email
	@Column(length=50,nullable=false)
	private String email;
	
	@Column( length=10,nullable=false)
	private String accountStatus;
	
	@Column(nullable=false)
	private Date lastLogin;
	
	
	public User(){}


	public User(Integer userId, String userName, String password, String email, String userType, String accountStatus,
			Date lastLogin) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.accountStatus = accountStatus;
		this.lastLogin = lastLogin;
	}


	public Integer getUserId() {
		return userId;
	}

	public void setUserID(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	
	public Date getLastLoginAsDate(Date lastLogin) {
		return lastLogin;
	}
	
	public String getLastLogin() {
		return lastLogin.toString();
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}