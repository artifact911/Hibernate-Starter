package org.art;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.art.dao.CompanyRepository;
import org.art.dao.PaymentRepository;
import org.art.dao.UserRepository;
import org.art.dto.UserCreateDto;
import org.art.entity.*;
import org.art.interceptor.TransactionInterceptor;
import org.art.mapper.CompanyReadMapper;
import org.art.mapper.UserCreateMapper;
import org.art.mapper.UserReadMapper;
import org.art.service.UserService;
import org.art.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;

import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
public class HibernateRunner {

    @SneakyThrows
    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                    new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
//            session.beginTransaction();

            var companyRepository = new CompanyRepository(session);

            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
            var userCreateMapper = new UserCreateMapper(companyRepository);

            var userRepository = new UserRepository(session);
//            var userService = new UserService(userRepository, userReadMapper, userCreateMapper);

            // из прошлого урока
//            var paymentRepository = new PaymentRepository(session);
//            paymentRepository.findById(1L).ifPresent(System.out::println);

            var transactionInterceptor = new TransactionInterceptor(sessionFactory);

            // это мы прокси содали. Охуеть. Это блять шоб не открывать транзакции ручками. И не коммитить их.
            // 2 строчки написать надо было, блять! Нахуй это все?! Кто-то ебанулся однозначно
            var userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

//            userService.findById(1L).ifPresent(System.out::println);

            UserCreateDto userCreateDto = new UserCreateDto(
                    PersonalInfo.builder()
                            .firstname("Liza")
                            .lastname("Stepanova")
                            .birthDate(LocalDate.now())
                            .build(),
                    "liza2@gmail.com",
                    null,
                    Role.USER,
                    1);

            userService.create(userCreateDto);

//            session.getTransaction().commit();
        }
    }

    private static void before13main() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
//            TestDataImporter.importData(sessionFactory);

            User user = null;

            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                user = session.find(User.class, 1L);
                user.getCompany().getName();
                user.getUserChats().size();
                var user1 = session.find(User.class, 1L);

                var payments = session.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        // хотим искать запрос в кэше
                        .setCacheable(true)
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
                session.getTransaction().commit();
            }

            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                var user2 = session.find(User.class, 1L);
                user2.getCompany().getName();
                user.getUserChats().size();

                var payments = session.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
                session.getTransaction().commit();
            }
        }
    }

    private static void before10main() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.setDefaultReadOnly(true);

            session.beginTransaction();

            // такие hints можно найти в классе:
            // org.hibernate.cfg.AvailableSettings
//            session.createQuery("select p from Payment p", Payment.class)
//                    .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
//                    .setHint("javax.persistence.lock.timeout", 5000)
////                    .setTimeout(5000)
//                    .list();

            var payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }

    private static void before9main() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();
//            session.enableFetchProfile("withCompanyAndPayment");
//            RootGraph<?> graph = session.getEntityGraph("withCompanyAndChat");

            var userRootGraph = createGraph(session);


            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJakartaHintName(), userRootGraph
            );
            var user = session.find(User.class, 1L, properties);

            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());


            // get не стработает с Граф
//            var user = session.get(User.class, 1L);
//            var company = user.getCompany();
//            var payments = user.getPayments();

            var users = session.createQuery(
                            "select u from User u " +
                                    "where 1 = 1", User.class)
                    .setHint(GraphSemantic.LOAD.getJakartaHintName(), userRootGraph)
                    .list();

            users.forEach(it -> System.out.println(it.getUserChats().size()));
            users.forEach(it -> System.out.println(it.getCompany().getName()));

            session.getTransaction().commit();
        }
    }

    private static RootGraph<User> createGraph(Session session) {
        var userGraph = session.createEntityGraph(User.class);
        userGraph.addAttributeNodes("company", "userChats");
        var userChatSubGraph = userGraph.addSubgraph("userChats", UserChat.class);
        userChatSubGraph.addAttributeNodes("chat");

        return userGraph;
    }
}
