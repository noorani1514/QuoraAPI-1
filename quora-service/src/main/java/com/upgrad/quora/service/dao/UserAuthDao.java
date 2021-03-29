package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserAuthDao {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Persist UserAuthEntity object in DB.
     *
     * @param userAuthEntity to be persisted in the DB.
     * @return Persisted UserAuthEntity object
     */
    public UserAuthEntity createUserAuth(UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }


    /**
     * Gets the user auth information based on the access token.
     *
     * @param accessToken access token of the user auth whose details is to be fetched.
     * @return A single user auth object or null
     */
    public UserAuthEntity getUserAuthByAccessToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    /**
     * Updates the UserAuthEntity object's logout time present in the DB.
     *
     * @param userAuthEntity user that needs to be logged out
     * @return the logged out user
     */
    public UserAuthEntity updateLogoutAt(UserAuthEntity userAuthEntity) {
        entityManager.merge(userAuthEntity);
        return userAuthEntity;
    }
}
