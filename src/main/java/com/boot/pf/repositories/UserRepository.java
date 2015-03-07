package com.boot.pf.repositories;

import com.boot.pf.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by dasanderl on 07.09.14.
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

    List<User> findByUsername(String username);
    
    User findOneByUsername(String username);
}
