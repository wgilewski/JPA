package com.app.repository.impl;

import com.app.converter.MovieJsonConverter;
import com.app.exceptions.MyException;
import com.app.model.Movie;
import com.app.repository.MovieRepository;
import com.app.repository.generic.AbstractGenericRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class MovieRepositoryImpl extends AbstractGenericRepository<Movie> implements MovieRepository {
    @Override
    public Optional<Movie> findByTitle(String title) {
        Optional<Movie> movie = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            TypedQuery<Movie> query = entityManager.createQuery(
                "select m from Movie m where m.title = :title",Movie.class);
            query.setParameter("title",title);
            
            movie = query.getResultList().stream().findFirst();
            
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
            return movie;
        }
    }
}
