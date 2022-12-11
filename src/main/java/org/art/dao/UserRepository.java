package org.art.dao;

import jakarta.persistence.EntityManager;
import org.art.entity.User;

public class UserRepository extends RepositoryBase<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
