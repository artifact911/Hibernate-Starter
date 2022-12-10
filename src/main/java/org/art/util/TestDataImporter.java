package org.art.util;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.art.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

@UtilityClass
public class TestDataImporter {

    public void importData(SessionFactory sessionFactory) {

        Company microsoft = saveCompany(sessionFactory, "Microsoft");
        Company apple = saveCompany(sessionFactory, "Apple");
        Company google = saveCompany(sessionFactory, "Google");

        User billGates = saveUser(sessionFactory, "Bill", "Gates",
                LocalDate.of(1955, Month.OCTOBER, 28), microsoft);
        User steveJobs = saveUser(sessionFactory, "Steve", "Jobs",
                LocalDate.of(1955, Month.FEBRUARY, 24), apple);
        User sergeyBrin = saveUser(sessionFactory, "Sergey", "Brin",
                LocalDate.of(1973, Month.AUGUST, 21), google);
        User timCook = saveUser(sessionFactory, "Tim", "Cook",
                LocalDate.of(1960, Month.NOVEMBER, 1), apple);
        User dianeGreene = saveUser(sessionFactory, "Diane", "Greene",
                LocalDate.of(1955, Month.JANUARY, 1), google);

        savePayment(sessionFactory, billGates, 100);
        savePayment(sessionFactory, billGates, 300);
        savePayment(sessionFactory, billGates, 500);

        savePayment(sessionFactory, steveJobs, 250);
        savePayment(sessionFactory, steveJobs, 600);
        savePayment(sessionFactory, steveJobs, 500);

        savePayment(sessionFactory, timCook, 400);
        savePayment(sessionFactory, timCook, 300);

        savePayment(sessionFactory, sergeyBrin, 500);
        savePayment(sessionFactory, sergeyBrin, 500);
        savePayment(sessionFactory, sergeyBrin, 500);

        savePayment(sessionFactory, dianeGreene, 300);
        savePayment(sessionFactory, dianeGreene, 300);
        savePayment(sessionFactory, dianeGreene, 300);

        Chat dmdev = saveChat(sessionFactory, "dmdev");
        Chat java = saveChat(sessionFactory, "java");
        Chat youtubeMembers = saveChat(sessionFactory, "youtube-members");

        addToChat(sessionFactory, dmdev, billGates, steveJobs, sergeyBrin);
        addToChat(sessionFactory, java, billGates, steveJobs, timCook, dianeGreene);
        addToChat(sessionFactory, youtubeMembers, billGates, steveJobs, timCook, dianeGreene);
    }

    private void addToChat(SessionFactory sessionFactory, Chat chat, User... users) {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Arrays.stream(users)
                .map(user -> UserChat.builder()
                        .chat(chat)
                        .user(user)
                        .build())
                .forEach(session::persist);

        session.getTransaction().commit();
    }

    private Chat saveChat(SessionFactory sessionFactory, String chatName) {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Chat chat = Chat.builder()
                .name(chatName)
                .build();
        session.persist(chat);

        session.getTransaction().commit();

        return chat;
    }

    private Company saveCompany(SessionFactory sessionFactory, String name) {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name(name)
                .build();
        session.persist(company);

        session.getTransaction().commit();

        return company;
    }

    private User saveUser(SessionFactory sessionFactory,
                          String firstName,
                          String lastName,
                          LocalDate birthday,
                          Company company) {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .username(firstName + lastName)
                .personalInfo(PersonalInfo.builder()
                        .firstname(firstName)
                        .lastname(lastName)
                        .birthDate(birthday)
                        .build())
                .company(company)
                .build();
        session.persist(user);

        session.getTransaction().commit();

        return user;
    }

    private void savePayment(SessionFactory sessionFactory, User user, Integer amount) {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Payment payment = Payment.builder()
                .receiver(user)
                .amount(amount)
                .build();
        session.persist(payment);

        session.getTransaction().commit();
    }
}
