package org.art.util;

import lombok.experimental.UtilityClass;
import org.art.converter.BirthdayConverter;
import org.art.listener.AuditTableListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();

        // configure() принимает путь к xml. Если не указывать - ищет в рутовых ресурсах проекта
        configuration.configure();

        var sessionFactory = configuration.buildSessionFactory();

        registerListeners(sessionFactory);

        return sessionFactory;
    }

    private static void registerListeners(SessionFactory sessionFactory) {
        // Привели интерфейс к его реализации, чтоб добраться до метода по добавлению нашего лисенера в группу
        var sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        var listenerRegistry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);

        var auditTableListener = new AuditTableListener();
        listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
        listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);
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
