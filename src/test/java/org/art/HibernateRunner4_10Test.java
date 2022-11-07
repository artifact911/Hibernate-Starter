package org.art;

import lombok.Cleanup;
import org.art.entity.Company;
import org.art.entity.User;
import org.art.util.HibernateUtil;
import org.junit.jupiter.api.Test;

public class HibernateRunner4_10Test {

    @Test
    void checkOrphanRemoval() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 1);
        company.getUsers().removeIf(user -> user.getId().equals(2L));


        session.getTransaction().commit();
    }
}
