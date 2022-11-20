package org.art;

import org.art.entity.User;
import org.art.util.HibernateTestUtil;
import org.junit.jupiter.api.Test;

public class Hib7_HQLTest {

    @Test
    void checkDocker() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             final var session = sessionFactory.openSession()) {
            session.beginTransaction();

//            var result = session.createQuery(
//                    "select u from User u where u.personalInfo.firstname = :firstname", User.class)
//                    .setParameter("firstname", "Ivan")
//                    .list();

            var result = session.createQuery(
                    "select u from User u " +
//                            "join u.company c " +
//                            "where u.personalInfo.firstname = :firstname and c.name = :comName", User.class)
                            "where u.personalInfo.firstname = :firstname and u.company.name = :comName", User.class)
                    .setParameter("firstname", "Ivan")
                    .setParameter("comName", "Google")
                    .list();


            session.getTransaction().commit();
        }
    }
}
