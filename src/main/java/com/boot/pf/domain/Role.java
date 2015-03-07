package com.boot.pf.domain;

import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Maximos on 3/6/2015.
 */
@javax.persistence.Entity
public class Role extends _AbstractEntity implements GrantedAuthority {

    @NotNull
    private String role;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    //@Transient
    private Set<User> users;

    public Role(String role) {
        this.role = role;
    }

    public Role() {}

    @Override
    public String getAuthority() {
        return role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        if(user != null && this.getUsers() != null) {
            if(!this.getUsers().contains(user)) {
                this.getUsers().add(user);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role1 = (Role) o;

        if (!role.equals(role1.role)) return false;
        if (users != null ? !users.equals(role1.users) : role1.users != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = role.hashCode();
        result = 31 * result + (users != null ? users.hashCode() : 0);
        return result;
    }
}
