package com.example.BestBid.BestBid.Services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.BestBid.BestBid.Models.Project;
import com.example.BestBid.BestBid.Repositorys.ProjectRepository;


@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository ProjectRepository;
	
	
	public String addProject(Project project) {

		String validate = validate(project);

		if(validate.equals("valid")){
			project.setTotalBids(0);
			project.setCreatedAt(new Date());
			ProjectRepository.save(project);
			return "created";
		}
		else
			return validate;

	}

	public void deleteProject(Integer projectId) {
		Project p = new Project();
		p.setProjectId(projectId);
		ProjectRepository.delete(p);
	}

	public Project updateProject(Project project) {
		return ProjectRepository.save(project);	
	}

	public Optional<Project> getProjectByProjectId(Integer projectId) {
		return ProjectRepository.getProjectByProjectId(projectId);
	}

	public Page<Project> getProjectByOwnerId(Integer ownerId,Pageable pageable) {
		return ProjectRepository.findAllByOwnerId(ownerId,pageable);
	}

	public Page<Project> getProjects(Pageable pageable) {
		return  ProjectRepository.findAll(pageable);
	}

	public Page<Project> getProjectByWorkType(String workType,Pageable pageable) {
		return ProjectRepository.findAllByWorkTypeContaining(workType,pageable);
	}

	public Page<Project> getProjectByName(String projectName, Pageable pageable) {
		return ProjectRepository.findAllByProjectNameContaining(projectName,pageable);
	}

	public Page<Project> getProjectsWithDealineRemaining(Pageable pageable) {
		return ProjectRepository.findByDeadlineAfter(new Date(),pageable);
	}

	public String validate(Project project){
		if(project.getProjectName() == null) return "Project name(projectName) cannot be null";
		else if(project.getProjectName().equals("")) return "Project name(projectName) cannot be empty";

		else if(project.getMaximumBudget()==null) return "maximumBudget cannot be null or empty";
		else if(project.getDescription() == null) return "Project Description(description) cannot be null";
		else if(project.getDescription().equals("")) return "Project Description(description) cannot be null";

		else if(project.getWorkType() == null) return "Project work type(workType) cannot be null";
		else if(project.getWorkType().equals("")) return "Project work type(workType) cannot be null";

		else return "valid";
	}

}