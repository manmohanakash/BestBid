package com.example.BestBid.BestBid.Repositorys;

import com.example.BestBid.BestBid.Models.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    public Optional<Role> getRoleByRoleName(String role);
}
