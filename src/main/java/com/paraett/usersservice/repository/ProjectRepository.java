package com.paraett.usersservice.repository;

import com.paraett.usersservice.model.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Transactional
    @Modifying
    void deleteAllByCompanyId(Long companyId);
}
