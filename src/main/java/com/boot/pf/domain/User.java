package com.boot.pf.domain;

import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by dasanderl on 07.09.14.
 */
@javax.persistence.Entity
public class User extends _AuditableEntity implements UserDetails {


    @NotNull
    private String username;
    
    @NotNull
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @BatchSize(size = 10)
    //@Transient
    private Set<Role> authorities;


    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = (Set<Role>) authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
        
    }

    @Override
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
        
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
