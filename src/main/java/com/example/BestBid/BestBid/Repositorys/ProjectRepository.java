package com.example.BestBid.BestBid.Repositorys;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.BestBid.BestBid.Models.Project;


public interface ProjectRepository extends PagingAndSortingRepository<Project,Integer>{

	public Optional<Project> getProjectByProjectId(Integer projectId);

	public Page<Project> findAllByOwnerId(Integer ownerId,Pageable pageable);
	
	public Page<Project> findAllByWorkTypeContaining(String workType,Pageable pageable);

	
}