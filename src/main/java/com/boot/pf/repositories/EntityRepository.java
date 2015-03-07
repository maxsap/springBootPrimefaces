package com.boot.pf.repositories;

import com.boot.pf.domain.Entity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by dasanderl on 07.09.14.
 */
@Repository
public interface EntityRepository extends CrudRepository<Entity, String> {

    List<Entity> findByName(String name);

    List<Entity> findByAge(int age);

    List<Entity> findByNameAndAge(String name, int age);

    List<Entity> findByNameOrAge(String name, int age);


}
