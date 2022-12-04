package org.art;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.art.entity.User;
import org.art.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class HibernateRunner {

    @SneakyThrows
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

//            var user = session.get(User.class, 1L);
//            var company = user.getCompany();
//            var payments = user.getPayments();

            var users = session.createQuery("select u from User u " +
                    "join fetch u.payments " +
                    "join fetch u.company " +
                    "where 1 = 1", User.class).list();

            users.forEach(user -> System.out.println(user.getPayments().size()));
            users.forEach(user -> System.out.println(user.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}
