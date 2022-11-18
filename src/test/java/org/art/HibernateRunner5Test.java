package org.art;

import org.art.entity.Company;
import org.art.util.HibernateUtil;
import org.junit.jupiter.api.Test;

public class HibernateRunner5Test {

    @Test
    void checkH2() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             final var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var google = Company.builder()
                    .name("Rich")
                    .build();

            session.persist(google);

            session.getTransaction().commit();
        }
    }
}
