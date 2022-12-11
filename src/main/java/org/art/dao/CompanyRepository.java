package org.art.dao;

import jakarta.persistence.EntityManager;
import org.art.entity.Company;

public class CompanyRepository extends RepositoryBase<Integer, Company> {

    public CompanyRepository(EntityManager entityManager) {
        super(Company.class, entityManager);
    }
}
