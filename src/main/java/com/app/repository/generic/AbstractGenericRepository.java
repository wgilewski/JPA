package com.app.repository.generic;

import com.app.exceptions.MyException;
import com.app.repository.connection.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class AbstractGenericRepository<T> implements GenericRepository<T> {

    protected final EntityManagerFactory entityManagerFactory
            = DbConnection.getInstance().getEntityManagerFactory();

    private final Class<T> type = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public T saveOrUpdate(T t) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        T item = null;
        try {
            if (t == null) {
                throw new NullPointerException("OBJECT IS NULL");
            }

            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            item = entityManager.merge(t);
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
            return item;
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            if (id == null) {
                throw new NullPointerException("OBJECT IS NULL");
            }

            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            T item = entityManager.find(type, id);
            entityManager.remove(item);
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
        }
    }

    @Override
    public void deleteAll() {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            entityManager
                    .createQuery("select i from " + type.getCanonicalName() + " i", type)
                    .getResultList()
                    .stream()
                    .forEach(entityManager::remove);
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
        }
    }

    @Override
    public Optional<T> findById(Long id) {
        Optional<T> item = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            if (id == null) {
                throw new NullPointerException("OBJECT IS NULL");
            }

            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            item = Optional.of(entityManager.find(type, id));
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
            return item;
        }
    }

    @Override
    public List<T> findAll() {
        List<T> items = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            items = entityManager
                    .createQuery("select i from " + type.getCanonicalName() + " i", type)
                    .getResultList();
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
}
