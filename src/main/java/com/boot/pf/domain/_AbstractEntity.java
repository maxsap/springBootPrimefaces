package com.boot.pf.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by dasanderl on 07.09.14.
 */
@MappedSuperclass
public abstract class _AbstractEntity {

    @Id
    @Column(unique = true, nullable = false, insertable = true, updatable = false, length = 128, precision = 0)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Version
    @NotNull
    private Date lastModifiedDate;

    public String getId() {
        return id;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
