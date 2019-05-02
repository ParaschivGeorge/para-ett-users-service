package com.paraett.usersservice.repository;

import com.paraett.usersservice.model.entities.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {
    public static Specification<User> findAllFiltered(Long companyId, Long managerId) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> predicateList = new ArrayList<>();

                if (companyId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("companyId"), companyId));
                }

                if (managerId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("managerId"), managerId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
