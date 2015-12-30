package su.asgor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "customer")
@JsonIgnoreProperties(value = "purchases")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="customer_seq_gen")
    @SequenceGenerator(name="customer_seq_gen", sequenceName="customer_seq")
    private long id;
    @NotNull
    private String name;
    @OneToMany(mappedBy = "customer")
    private List<Purchase> purchases;
    public Customer() { }

    public Customer(long id) {
        this.id = id;
    }

    public Customer(String name) {
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

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }
}
