package org.art;

import org.art.entity.Company;
import org.art.entity.LocaleInfo;
import org.art.util.HibernateUtil;
import org.junit.jupiter.api.Test;

public class HibernateRunner4_16Test {

    @Test
    void localeInfo() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             final var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var company = session.get(Company.class, 1);
            var ru = LocaleInfo.of("ru", "Описание на русском");
            var en = LocaleInfo.of("en", "English description");
            company.getLocales().put(ru.getLang(), ru.getDescription());
            company.getLocales().put(en.getLang(), en.getDescription());

            session.getTransaction().commit();
        }
    }
}
