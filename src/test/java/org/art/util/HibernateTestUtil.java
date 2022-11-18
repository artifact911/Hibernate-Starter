package org.art.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class HibernateTestUtil {

    // переходим на https://hub.docker.com/_/postgres
    // смотрим версии и вставляем в контректор следующее (версия, например, 15)
    // postgres:15
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    // теперь нужно один раз стартануть за все время тестов
    // запустим в статическом блоке инициализации
    // static позволяет гарантировать один docker container на все время выполнения тестов
    static {
        postgres.start();
    }

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = HibernateUtil.buildConfiguration();
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());

        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
