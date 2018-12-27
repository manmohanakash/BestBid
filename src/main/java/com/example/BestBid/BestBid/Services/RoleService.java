package com.example.BestBid.BestBid.Services;


import com.example.BestBid.BestBid.Models.Role;
import com.example.BestBid.BestBid.Repositorys.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {


    @Autowired
    RoleRepository RoleRepository;

    public Role getRolebyRole(String role) {
        return RoleRepository.getRoleByRoleName(role).get();
    }
}
