package org.art.util;

import lombok.experimental.UtilityClass;
import org.art.converter.BirthdayConverter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();

        // configure() принимает путь к xml. Если не указывать - ищет в рутовых ресурсах проекта
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
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
        return configuration;
    }
}
