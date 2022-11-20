package org.art;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.QueryHint;
import org.art.entity.User;
import org.art.util.HibernateTestUtil;
import org.hibernate.jpa.AvailableHints;
import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.Test;

public class Hib7_HQLTest {

    @Test
    void checkHQL() {
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

    @Test
    void namedQuery() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             final var session = sessionFactory.openSession()) {
            session.beginTransaction();

            // можем тут отправить обычный SQL запрос
//            session.createNativeQuery(select u from User u where u.firstname = 'Ivan'", User.class)

            var result = session.createNamedQuery("findUserByName", User.class)
                    .setParameter("firstname", "Ivan")
                    .setFlushMode(FlushModeType.AUTO)
                    .setHint(AvailableHints.HINT_FETCH_SIZE, "50")
                    .list();

            session.getTransaction().commit();
        }
    }
}
