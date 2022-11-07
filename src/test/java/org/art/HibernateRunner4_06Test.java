package org.art;

import lombok.Cleanup;
import org.art.entity.Company;
import org.art.entity.User;
import org.art.util.HibernateUtil;
import org.junit.jupiter.api.Test;

public class HibernateRunner4_06Test {

    @Test
    void addUserToNewCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = Company.builder()
                .name("Facebook")
                .build();

        var user = User.builder()
                .username("sveta@gmail.com")
                .build();

        // мы написали метод, поэтому вместо этих строк, напишем одну
//        user.setCompany(company);
//        company.getUsers().add(user);

        company.addUser(user);

        session.persist(company);

        session.getTransaction().commit();
    }

    /**
     * Происходит сначала селект запрос на компанию, затем на пользователей в ней.
     * После этого удаляются все юзеры и потом компания.
     */
    @Test
    void deleteCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        // что бы компанию удалить, нам нужно сначала ее получить из БД. Хибер бы сделал это и сам, но лучше мы сами
        // и она будет в persistenceState и она гарантированно существует
        var company = session.get(Company.class, 3);

        session.remove(company);

        session.getTransaction().commit();
    }
}
