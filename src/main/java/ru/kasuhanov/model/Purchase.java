package ru.kasuhanov.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="purchase_seq_gen")
    @SequenceGenerator(name="purchase_seq_gen", sequenceName="purchase_seq")
    private long id;
    @NotNull
    private String name;
    @Column(name = "startTime")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date startDate;

    public Purchase() { }

    public Purchase(long id) {
        this.id = id;
    }

    public Purchase(String name, Date startDate) {
        this.name = name;
        this.startDate = startDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
