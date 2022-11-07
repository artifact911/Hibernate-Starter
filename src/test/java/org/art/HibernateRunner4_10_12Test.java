package org.art;

import lombok.Cleanup;
import org.art.entity.Company;
import org.art.entity.Profile;
import org.art.entity.User;
import org.art.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

public class HibernateRunner4_10_12Test {

    @Test
    void checkOneToOneSave() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             final var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var user = User.builder()
                    .username("test3@gmail.com")
                    .build();

            var profile = Profile.builder()
                    .language("ru")
                    .street("Chapaeva 10")
                    .build();

            profile.setUser(user);

            session.persist(user);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOneGet() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             final var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var user = session.get(User.class, 4L);


        }
    }

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
