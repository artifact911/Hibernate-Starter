package org.art;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.art.entity.Payment;
import org.art.entity.User;
import org.art.entity.UserChat;
import org.art.util.HibernateUtil;
import org.art.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;

import java.util.Map;

@Slf4j
public class HibernateRunner {

    @SneakyThrows
    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            TestDataImporter.importData(sessionFactory);

           session.beginTransaction();

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
