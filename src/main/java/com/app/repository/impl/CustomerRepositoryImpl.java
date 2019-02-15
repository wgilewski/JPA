package com.app.repository.impl;

import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.repository.CustomerRepository;
import com.app.repository.generic.AbstractGenericRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.Optional;

public class CustomerRepositoryImpl extends AbstractGenericRepository<Customer> implements CustomerRepository {

    @Override
    public Optional<Customer> findByEmail(String email) {
        Optional<Customer> items = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            items = entityManager
                    .createQuery("select c from Customer c ", Customer.class)
                    .getResultList()
                    .stream()
                    .findFirst();
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(e.getMessage());
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            return items;
        }
    }


    public Optional<Customer> findOneByEmail(String email) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Optional<Customer> optional = Optional.empty();
        if (entityManager != null && email != null) {
            Query query = entityManager.createQuery("select p from Customer p where p.email = :email");
            query.setParameter("email", email);
            optional = Optional.of((Customer) query.getSingleResult());

        }
        return optional;
    }
}
