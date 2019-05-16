package com.paraett.usersservice.repository;

import com.paraett.usersservice.model.entities.Project;
import com.paraett.usersservice.model.entities.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectSpecifications {
    public static Specification<Project> findAllFiltered(Long companyId, Long responsibleId, Long userId) {
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

                if (userId != null) {
                    Expression<Collection<User>> projectUsers = root.get("users");
                    Root<User> user = criteriaQuery.from(User.class);

                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(user.get("id"), userId), criteriaBuilder.isMember(user, projectUsers)));
                }

                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}