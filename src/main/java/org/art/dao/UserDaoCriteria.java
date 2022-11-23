package org.art.dao;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.art.dto.CompanyDto;
import org.art.entity.*;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.hibernate.query.criteria.JpaSubQuery;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDaoCriteria {

    private static final UserDaoCriteria INSTANCE = new UserDaoCriteria();

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user).where(
                cb.equal(
                        user.get(User_.personalInfo).get(PersonalInfo_.firstname),
                        firstName
                ));

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user).orderBy(
                cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.birthDate))
        );

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
        var cb = session.getCriteriaBuilder();

        // что возвращаем
        var criteria = cb.createQuery(User.class);
        // это прежний from
        var company = criteria.from(Company.class);
        // это join
        var users = company.join(Company_.users);

        criteria.select(users).where(
                cb.equal(company.get(Company_.name), companyName)
        );

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Payment.class);
        var payment = criteria.from(Payment.class);
        var user = payment.join(Payment_.receiver);
        var company = user.join(User_.company);

        criteria.select(payment).where(
                        cb.equal(company.get(Company_.name), companyName)
                )
                .orderBy(
                        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
                        cb.asc(payment.get(Payment_.amount))
                );

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Double.class);
        var payment = criteria.from(Payment.class);
        var user = payment.join(Payment_.receiver);

        List<Predicate> predicates = new ArrayList<>();
        if (firstName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
        }
        if (lastName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName));
        }

        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
                predicates.toArray(Predicate[]::new)
        );

        return session.createQuery(criteria).uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Object[].class);
        var company = criteria.from(Company.class);
        var user = company.join(Company_.users, JoinType.INNER);
        var payment = user.join(User_.payments);

        criteria.multiselect(
                        company.get(Company_.name),
                        cb.avg(payment.get(Payment_.amount))
                )
                .groupBy(company.get(Company_.name))
                .orderBy(cb.asc(company.get(Company_.name)));

        return session.createQuery(criteria).list();
    }

    // перепил findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName на кастомную DTO
    // TODO не сработал. Разобраться
    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyNameDTO(Session session) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(CompanyDto.class);
        var company = criteria.from(Company.class);
        var user = company.join(Company_.users, JoinType.INNER);
        var payment = user.join(User_.payments);

//        criteria.select(
//                        cb.construct(CompanyDto.class,
//                                company.get(Company_.name),
//                                cb.avg(payment.get(Payment_.amount)))
//                )
//                .groupBy(company.get(Company_.name))
//                .orderBy(cb.asc(company.get(Company_.name)));


        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Object[]> isItPossible(Session session) {
        return session.createQuery("select u, avg(p.amount) from User u " +
                        "join u.payments p " +
                        "group by u " +
                        "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
                        "order by u.personalInfo.firstname", Object[].class)
                .list();
    }

    // перепил isItPossible на Tuple
    // TODO не сработал. Разобраться
    public List<Tuple> isItPossibleTuple(Session session) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Tuple.class);
        var user = criteria.from(User.class);
        var payment = user.join(User_.payments);

        var subquery = criteria.subquery(Double.class);
        var paymentSubquery = subquery.from(Payment.class);

//        criteria.select(
//                cb.tuple(
//                        user,
//                        cb.avg(payment.get(Payment_.amount))
//                )
//        )
//                .groupBy(user.get(User_.id))
//                .having(cb.gt(
//                        cb.avg(payment.get(Payment_.amount)),
//                        subquery.select(cb.avg(paymentSubquery.get(Payment_.amount)))
//                ))
//                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));

        return session.createQuery(criteria).list();
    }

    public static UserDaoCriteria getInstance() {
        return INSTANCE;
    }
}
