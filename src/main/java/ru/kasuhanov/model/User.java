package ru.kasuhanov.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="user_seq_gen")
    @SequenceGenerator(name="user_seq_gen", sequenceName="users_seq")
    private long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private long age;

    public User() { }

    public User(long id) {
        this.id = id;
    }

    public User(String email, String name,long age) {
        this.email = email;
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }
}