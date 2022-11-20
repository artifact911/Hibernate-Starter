package org.art;

import org.art.entity.Chat;
import org.art.entity.User;
import org.art.entity.UserChat;
import org.art.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class HibernateRunner4_13_14Test {

    @Test
    void checkAddManyToMany() {
//        try (var sessionFactory = HibernateUtil.buildSessionFactory();
//             final var session = sessionFactory.openSession()) {
//            session.beginTransaction();
//
//            var user = session.get(User.class, 5L);
//
//            var chat = Chat.builder()
//                    .name("dmdev")
//                    .build();
//
//            user.addChat(chat);
//            session.persist(chat);
//
//            session.getTransaction().commit();
//        }
    }

    @Test
    void checkDelManyToMany() {
//        try (var sessionFactory = HibernateUtil.buildSessionFactory();
//             final var session = sessionFactory.openSession()) {
//            session.beginTransaction();
//
//            var user = session.get(User.class, 5L);
//            // удалили все чаты узера, а могли по id удалить конкретный
//            user.getChats().clear();
//
//            session.getTransaction().commit();
//        }
    }

    @Test
    void checkAddManyToManySeparateEntity() {
//        try (var sessionFactory = HibernateUtil.buildSessionFactory();
//             final var session = sessionFactory.openSession()) {
//            session.beginTransaction();
//
//            var user = session.get(User.class, 5L);
//            var chat = session.get(Chat.class, 1L);
//
//            var userChat = UserChat.builder().createdAt(Instant.now())
//                    .createdBy(user.getUsername())
//                    .build();
//            userChat.setUser(user);
//            userChat.setChat(chat);
//
//            session.persist(userChat);
//            session.getTransaction().commit();
//        }
    }
}
