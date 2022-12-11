package org.art.dao;

import jakarta.persistence.EntityManager;
import org.art.entity.Payment;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

    public PaymentRepository(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }
}
