package org.art;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.art.entity.Birthday;
import org.art.entity.Company;
import org.art.entity.PersonalInfo;
import org.art.entity.User;
import org.art.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    @SneakyThrows
    public static void main(String[] args) {
        Company company = Company.builder()
                .name("Google")
                .build();

        User user = User.builder()
                .username("ivan1@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .lastname("Ivanov")
                        .firstname("Ivan")
//                        .birthDate(new Birthday(LocalDate.of(2000, 1, 2)))
                        .birthDate(LocalDate.of(2000, 1, 2))
                        .build())
                .company(company)
                .build();


        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();

            session1.persist(company);
            session1.persist(user);

            session1.getTransaction().commit();
        }
    }
}
