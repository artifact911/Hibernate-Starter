package org.art;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.art.entity.Payment;
import org.art.entity.User;
import org.art.entity.UserChat;
import org.art.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;

import java.util.Map;

@Slf4j
public class HibernateRunner {

    @SneakyThrows
    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
//            TestDataImporter.importData(sessionFactory);

            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                var payment = session.find(Payment.class, 1L);
                payment.setAmount(payment.getAmount() + 10);

                session.getTransaction().commit();
            }

            try (var session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                var auditReader = AuditReaderFactory.get(session2);

                // теперь могу получить состояние сущности по айДи или по timestamp ан момент указанной ревизии
//                var OldPaymentDate = auditReader.find(Payment.class, 1L, new Date(1670759039284L));

                // на самом деле это будет не объект Payment, а Payment_aud
                var oldPayment = auditReader.find(Payment.class, 1L, 1L);

                // чтоб накатить определенное стостояние в БД:
                // !!! падает(
//                session2.replicate(oldPayment, ReplicationMode.OVERWRITE);


                var auditQuery = auditReader.createQuery()
                        .forEntitiesAtRevision(Payment.class, 400L)
                        .add(AuditEntity.property("amont").ge(450))
                        .add(AuditEntity.property("id").ge(1L))
                        .addProjection(AuditEntity.property("amount"))
                        .addProjection(AuditEntity.id());

                session2.getTransaction().commit();
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
