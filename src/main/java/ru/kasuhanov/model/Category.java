package ru.kasuhanov.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="categoty_seq_gen")
    @SequenceGenerator(name="categoty_seq_gen", sequenceName="category_seq")
    private long id;

    @NotNull
    private String name;
    public Category() { }

    public Category(long id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }
}
