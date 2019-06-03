package com.paraett.usersservice.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.Set;

@Entity
@Table(name = "project_tbl")
public class Project {

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="company_id")
    private Long companyId;

    @Column(name="responsible_id")
    private Long responsibleId;

    @Length(max=50)
    private String name;

//    @JsonManagedReference
    @ManyToMany
    private Set<User> users;

    public Project() {
    }

    public Project(Long companyId, Long responsibleId, String name) {
        this.companyId = companyId;
        this.responsibleId = responsibleId;
        this.name = name;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(Long responsibleId) {
        this.responsibleId = responsibleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
