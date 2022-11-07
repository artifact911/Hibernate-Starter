package org.art;

import org.art.entity.Company;
import org.art.util.HibernateUtil;
import org.junit.jupiter.api.Test;

public class HibernateRunner4_05Test {

    @Test
    void oneToMany() {

        // @Cleanup закроет по типу try-with-resources
//        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
//        @Cleanup var session = sessionFactory.openSession();

        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()){

            session.beginTransaction();

            var company = session.get(Company.class, 1);
            System.out.println(company);
        }
    }
}
