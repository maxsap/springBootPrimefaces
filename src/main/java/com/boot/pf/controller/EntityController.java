package com.boot.pf.controller;

import com.boot.pf.domain.NestedEntity;
import com.boot.pf.repositories.EntityRepository;
import com.boot.pf.domain.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import com.boot.pf.service.UserService;

import java.util.*;

/**
 * Created by dasanderl on 07.09.14.
 */
@Controller
@Scope("session")
public class EntityController {


    @Autowired
    EntityRepository entityRepository;
    @Autowired
    UserService userService;

    private List<Entity> entities = new ArrayList<>();
    
    
    //@PersistenceContext
    //private EntityManager entityManager;


    @Transactional
    @PreAuthorize("isFullyAuthenticated() and hasRole('ADMIN')")
    public void saveTestEntity() {
        System.out.println("saving new entity");
        Entity entity = EntityController.getRandomEntity();//DomainProvider.getRandomEntity();
        entityRepository.save(entity);
    }


    public String getUserName() {
        return userService.getUserNameFromService();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }



    public static Entity getRandomEntity() {
        Entity entity = new Entity();
        entity.setName(UUID.randomUUID().toString());
        entity.setAge(new Random().nextInt(100));

        NestedEntity nestedEntity = new NestedEntity();

        nestedEntity.setNestedName(UUID.randomUUID().toString());
        nestedEntity.setNestedAge(new Random().nextInt(100));

        entity.setNestedEntitiesBatch10(Arrays.asList(nestedEntity));

        return entity;
    }
}
