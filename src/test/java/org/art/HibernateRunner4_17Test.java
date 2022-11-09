package org.art;

import org.art.entity.Company;
import org.art.util.HibernateUtil;
import org.junit.jupiter.api.Test;

public class HibernateRunner4_17Test {

    @Test
    void checkOrdering() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             final var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var company = session.get(Company.class, 1);

            company.getUsers().forEach(System.out::println);

            session.getTransaction().commit();
        }
    }
}

