package com.swesource.sample.jee.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 */
@Entity
@NamedQueries(value =
        @NamedQuery(name = "findUserWithUsername", query = "select u from User u where u.username = :username")
)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int USERNAME_MIN = 4;
    private static final int USERNAME_MAX = 20;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = USERNAME_MAX)
    @NotNull
    @Size(min = USERNAME_MIN, max = USERNAME_MAX)
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User@" + hashCode() + " [id = " + id + "]";
    }
}
