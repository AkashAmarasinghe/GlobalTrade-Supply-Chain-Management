package com.globaltrade.bean;

import com.globaltrade.entity.Role;
import com.globaltrade.entity.User;
import com.globaltrade.util.PasswordUtil;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AuthBean {
    @PersistenceContext(unitName = "GlobalTradePU")
    private EntityManager em;

    public User authenticate(String username, String password) {
        List<User> users = em.createQuery(
                        "select u from User u join fetch u.role where u.username=:u and u.active=true",
                        User.class)
                .setParameter("u", username)
                .setMaxResults(1)
                .getResultList();
        if (users.isEmpty()) {
            return null;
        }
        User user = users.get(0);
        return PasswordUtil.matches(password, user.getPasswordHash()) ? user : null;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User findById(Long id) {
        return id == null ? null : em.find(User.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Role> findRoles() {
        return em.createQuery("select r from Role r order by r.id", Role.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long countActiveUsers() {
        return em.createQuery("select count(u) from User u where u.active=true", Long.class)
                .getSingleResult();
    }
}
