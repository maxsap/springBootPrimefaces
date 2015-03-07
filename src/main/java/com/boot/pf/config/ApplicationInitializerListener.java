package com.boot.pf.config;

import com.boot.pf.domain.Role;
import com.boot.pf.domain.User;
import com.boot.pf.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maximos on 3/4/2015.
 */
@Component
public class ApplicationInitializerListener implements ApplicationListener<ContextRefreshedEvent> {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        Role userAuthority = new Role("user");
        userAuthority.addUser(user);
        authorities.add(userAuthority);
        user.setAuthorities(authorities);

        entityManager.persist(user);
        //userRepository.save(user);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        authorities = new HashSet<GrantedAuthority>();
        Role adminAuthority = new Role("ADMIN");
        userAuthority.addUser(user);
        authorities.add(adminAuthority);
        admin.setAuthorities(authorities);
        entityManager.persist(admin);
    }
}
