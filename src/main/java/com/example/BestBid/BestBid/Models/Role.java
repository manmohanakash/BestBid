package com.example.BestBid.BestBid.Models;

import javax.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String roleName;

    public Integer getId() {
        return roleId;
    }

    public void setId(Integer id) {
        this.roleId = id;
    }

    public String getRole() {
        return roleName;
    }

    public void setRole(String role) {
        this.roleName = role;
    }

}