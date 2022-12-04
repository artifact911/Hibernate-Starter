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

            var user = session.get(User.class, 1L);
            var company = user.getCompany();
            var payments = user.getPayments();

            session.getTransaction().commit();
        }
    }
}
