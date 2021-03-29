package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
    * fetch details of user by the user name
    *
    * @param userName -> User name of the user whose details need to fetched.
    * @return details of the user if found else null
    * */
    public UserEntity getUserByUserName(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).
                    setParameter("userName", userName).getSingleResult();
        }
        catch (NoResultException nre) {
            return null;
        }
    }

    /**
    * fetch the details of user by the email id
    *
    * @param email -> Email ID of the user whose details need to be fetched
    * @return details of the user if found else null
    * */
    public UserEntity getUserByEmailId(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmailId", UserEntity.class).
                    setParameter("email", email).getSingleResult();
        }
        catch (NoResultException nre) {
            return null;
        }
    }

    /**
    * create the user details and persist it to the database
    *
    * @param userEntity -> user details of the user that to be persisted
    * @return user details of the user that persisted
    * */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("userByUUID", UserEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
