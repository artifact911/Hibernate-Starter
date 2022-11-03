package org.art;

import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {

    @SneakyThrows
    public static void main(String[] args) {

        // Этот класс служит для создания SessionFactory
        Configuration configuration = new Configuration();
        // configure() принимает путь к xml. Если не указывать - ищет в рутовых ресурсах проекта
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {   // теперь у SF мы можем получить сессию - что-то вроде обертки вокруг нашего Connection
            System.out.println("ok");
        }
    }
}
