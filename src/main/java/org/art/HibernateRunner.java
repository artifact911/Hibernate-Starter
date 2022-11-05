package org.art;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.art.entity.PersonalInfo;
import org.art.entity.User;
import org.art.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class HibernateRunner {

    @SneakyThrows
    public static void main(String[] args) {

        // тут этот user находится в transient для двух сессий.
        User user = User.builder()
                .username("ivan123@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .lastname("Ivanov")
                        .firstname("Ivan")
                        .build())
                .build();
        log.info("User entity is in transient state, object: {}", user);


        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();

            // теперь user в persistent для session1
            session1.persist(user);

            session1.getTransaction().commit();
        }

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();

//            user.getPersonalInfo().setFirstname("Sveta");

            // тут наша сущность не с контексте еще нашей сесии, а значит хибер выполнит селект в БД и обновит user
            // до стояния user БД (имя Светы сотрется)
            session2.merge(user);

            session2.getTransaction().commit();
        }
    }
}
