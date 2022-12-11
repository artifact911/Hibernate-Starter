package org.art.dao;

import org.art.entity.Payment;
import org.hibernate.SessionFactory;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

    public PaymentRepository(SessionFactory sessionFactory) {
        super(Payment.class, sessionFactory);
    }
}
