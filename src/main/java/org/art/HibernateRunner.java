package org.art;

import lombok.SneakyThrows;
import org.art.converter.BirthdayConverter;
import org.art.entity.Birthday;
import org.art.entity.Role;
import org.art.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {

    @SneakyThrows
    public static void main(String[] args) {

        // Этот класс служит для создания SessionFactory
        Configuration configuration = new Configuration();

        // преобразовать название колонок из java-нэйминга в sql(xx_xxx)
        // не юзаем. Заюзали анноташку над полем
//        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        // указали, что это сущность и за ней надо теперь следить
        // не стали юзать так, т.к. указали маппинг в xml
//        configuration.addAnnotatedClass(User.class);

        // указали Хиберу, автоматом юзать этот конвертер. Вторым параметров нужно указать true, чтоб работало автоматом
        // и не нужно было ставить над полями, а можно тут не писать, а поставить анноташку над конвертером
        // @Converter(autoApply = true)
        configuration.addAttributeConverter(new BirthdayConverter());

        // configure() принимает путь к xml. Если не указывать - ищет в рутовых ресурсах проекта
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {   // теперь у SF мы можем получить сессию - что-то вроде обертки вокруг нашего Connection

            session.beginTransaction();
            User user = User.builder()
                    .username("ivan1@gmail.com")
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
                    .role(Role.ADMIN)
                    .build();
            session.save(user);
            session.getTransaction().commit();
        }
    }
}
