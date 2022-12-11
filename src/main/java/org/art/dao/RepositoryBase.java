package org.art.dao;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.art.entity.BaseEntity;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    private final Class<E> clazz;
    private final SessionFactory sessionFactory;

    @Override
    public E save(E entity) {
        @Cleanup var session = sessionFactory.openSession();
        session.save(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        @Cleanup var session = sessionFactory.openSession();
        session.delete(id);
        session.flush();
    }

    @Override
    public void update(E entity) {
        @Cleanup var session = sessionFactory.openSession();
        session.merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        @Cleanup var session = sessionFactory.openSession();
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        @Cleanup var session = sessionFactory.openSession();
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(clazz);
        criteria.from(clazz);

        return session.createQuery(criteria).getResultList();
    }
}
