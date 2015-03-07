package com.boot.pf.rest;


import com.boot.pf.controller.EntityController;
import com.boot.pf.domain.Entity;
import com.boot.pf.repositories.EntityRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dasanderl on 13.09.14.
 */
@Controller
@RequestMapping("/rest/entity")
public class TestEntityRestEndpoint {

    @Autowired
    private EntityRepository entityRepository;

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Entity> findAll() {
        return Lists.newArrayList(entityRepository.findAll());
    }

    @RequestMapping(value = "/getByAgePath/{age}", method = RequestMethod.GET)
    public ResponseEntity<Entity> getByAgePath(@PathVariable int age) {

        List<Entity> testEntities = entityRepository.findByAge(age);
        Entity entity = testEntities.isEmpty() ? null : testEntities.get(0);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @RequestMapping(value = "/addTestEntity", method = RequestMethod.GET, params = {"name", "age"})
    public ResponseEntity<String> addTestEntity(@RequestParam String name, @RequestParam int age) {
        Entity entity = EntityController.getRandomEntity();
        entity = entityRepository.save(entity);
        return new ResponseEntity<>("created new TestEntity with id "+entity.getId(), HttpStatus.OK);
    }
}
