package com.paraett.usersservice.repository;

import com.paraett.usersservice.model.entities.Project;
import com.paraett.usersservice.model.entities.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ProjectSpecifications {
    public static Specification<Project> findAllFiltered(Long companyId, Long responsibleId) {
        return new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> predicateList = new ArrayList<>();

                if (companyId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("companyId"), companyId));
                }

                if (responsibleId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("responsibleId"), responsibleId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}