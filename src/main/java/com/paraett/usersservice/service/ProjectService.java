package com.paraett.usersservice.service;

import com.paraett.usersservice.exception.NotFoundException;
import com.paraett.usersservice.model.dtos.ProjectDto;
import com.paraett.usersservice.model.entities.Project;
import com.paraett.usersservice.model.entities.User;
import com.paraett.usersservice.repository.ProjectRepository;
import com.paraett.usersservice.repository.ProjectSpecifications;
import com.paraett.usersservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project addProject(ProjectDto projectDto) {
        if (!userRepository.findById(projectDto.getResponsibleId()).isPresent()) {
            throw new NotFoundException("User not found! Id: " + projectDto.getResponsibleId());
        }
        Project project = new Project(projectDto.getCompanyId(), projectDto.getResponsibleId(), projectDto.getName());

        Set<User> users = project.getUsers();
        if (users == null) {
            users = new HashSet<>();
        }
        for (Long userId: projectDto.getUsers()) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                users.add(user.get());
            }
        }
        project.setUsers(users);

        return this.projectRepository.save(project);
    }

    public Project updateProject(Long id, ProjectDto projectDto) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (!optionalProject.isPresent()) {
            throw new NotFoundException("Project not found! Id: " + id);
        }
        Project project = optionalProject.get();
        if (!userRepository.findById(projectDto.getResponsibleId()).isPresent()) {
            throw new NotFoundException("User not found! Id: " + projectDto.getResponsibleId());
        } else {
            project.setResponsibleId(projectDto.getResponsibleId());
        }
        if (projectDto.getName() != null) {
            project.setName(projectDto.getName());
        }
        if (projectDto.getUsers() != null && !projectDto.getUsers().isEmpty()) {
            Set<User> users = new HashSet<>();
            for (Long userId : projectDto.getUsers()) {
                Optional<User> user = userRepository.findById(userId);
                if (user.isPresent()) {
                    users.add(user.get());
                }
            }
            project.setUsers(users);
        }
        return projectRepository.save(project);
    }

    public Project getProject(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (!optionalProject.isPresent()) {
            throw new NotFoundException("Project not found! Id: " + id);
        }
        return optionalProject.get();
    }

    public List<Project> getProjects(Long companyId, Long responsibleId) {
        return projectRepository.findAll(ProjectSpecifications.findAllFiltered(companyId, responsibleId));
    }

    public void deleteProject(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (!optionalProject.isPresent()) {
            throw new NotFoundException("Project not found! Id: " + id);
        }
        projectRepository.deleteById(id);
    }

    public void deleteProjects(Long companyId) {
        projectRepository.deleteAllByCompanyId(companyId);
    }
}
