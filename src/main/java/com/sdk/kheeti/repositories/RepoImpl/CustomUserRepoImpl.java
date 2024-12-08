package com.sdk.kheeti.repositories.RepoImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sdk.kheeti.model.User;
import com.sdk.kheeti.repositories.CustomUserRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
@Transactional
public class CustomUserRepoImpl implements CustomUserRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User customFindByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    // Method to delete a user by email
    
    @Override
    public User deleteUserByEmail(String email) {
        Query query = entityManager.createQuery("DELETE FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        query.executeUpdate();
        return (User) query.getSingleResult();
    }
}
