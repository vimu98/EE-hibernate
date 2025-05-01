package org.example.demohibernate.service;

import org.example.demohibernate.dto.UserDTO;
import org.example.demohibernate.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");

    public UserDTO createUser(UserDTO userDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = new User(userDTO.getName(), userDTO.getEmail());
            em.persist(user);
            tx.commit();
            return new UserDTO(user.getId(), user.getName(), user.getEmail());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to create user", e);
        } finally {
            em.close();
        }
    }

    public UserDTO findUser(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.find(User.class, id);
            return user != null ? new UserDTO(user.getId(), user.getName(), user.getEmail()) : null;
        } finally {
            em.close();
        }
    }

    public List<UserDTO> findAllUsers() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u", User.class)
                    .getResultList()
                    .stream()
                    .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, id);
            if (user != null) {
                user.setName(userDTO.getName());
                user.setEmail(userDTO.getEmail());
                em.merge(user);
                tx.commit();
                return new UserDTO(user.getId(), user.getName(), user.getEmail());
            }
            return null;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to update user", e);
        } finally {
            em.close();
        }
    }

    public boolean deleteUser(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to delete user", e);
        } finally {
            em.close();
        }
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}