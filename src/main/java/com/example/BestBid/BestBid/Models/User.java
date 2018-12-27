package com.example.BestBid.BestBid.Models;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Cacheable
@Table(uniqueConstraints = {@UniqueConstraint(name="email",columnNames = "email"),@UniqueConstraint(name="user_name",columnNames = "userName")})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "INTEGER(10) UNSIGNED")
	@JsonIgnore
	private Integer userId;

	@Column(length = 25, nullable = false)
	private String userName;

	@JsonIgnore
	@Column(length = 256, nullable = false)
	private String password;

	@Email
	@Column(length = 50, nullable = false)
	private String email;

	@Column(length = 10, nullable = false)
	private String accountStatus;

	@Column(nullable = false)
	private Date lastLogin;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_name"))
	private Set<Role> roles;


	public User() {
	}

	public User(String userName, String password, @Email String email, String accountStatus, Date lastLogin, Set<Role> roles) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.accountStatus = accountStatus;
		this.lastLogin = lastLogin;
		this.roles = roles;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}