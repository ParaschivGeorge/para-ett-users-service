package com.paraett.usersservice.controller;

import com.paraett.usersservice.model.dtos.ProjectDto;
import com.paraett.usersservice.model.entities.Project;
import com.paraett.usersservice.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("")
    public ResponseEntity<Project> addProject(ProjectDto projectDto) {
        Project project = projectService.addProject(projectDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(project.getId())
                .toUri();

        return ResponseEntity.created(location).body(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        Project updatedProject = projectService.updateProject(id, projectDto);

        return ResponseEntity.accepted().body(updatedProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        Project project = projectService.getProject(id);

        return ResponseEntity.ok(project);
    }

    @GetMapping("")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectService.getProjects();

        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Object> deleteProjects(@RequestParam Long companyId) {
        projectService.deleteProjects(companyId);

        return ResponseEntity.noContent().build();
    }
}
