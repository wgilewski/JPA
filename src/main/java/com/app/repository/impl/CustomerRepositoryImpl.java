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
        Optional<Customer> customer = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            
            TypedQuery<Customer> query = entityManager.createQuery(
                "select c from Customer c where c.email = :email",Customer.Class);
            
            query.setParameter("email",email);
            
            customer = query.getResultList().stream().findFirtst();

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
            return customer;
        }
    }
}
