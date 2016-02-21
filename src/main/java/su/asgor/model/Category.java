package su.asgor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "category")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = "purchases")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="categoty_seq_gen")
    @SequenceGenerator(name="categoty_seq_gen", sequenceName="category_seq")
    private long id;
    @NotNull
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "purchase_category",  joinColumns = {
            @JoinColumn(name = "category_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "purchase_id",
                    nullable = false, updatable = false) })
    @JsonManagedReference
    private List<Purchase> purchases;
    @Transient
    public int purchaseCount;
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

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Category setupCount() {
        this.purchaseCount = getPurchases().size();
        return this;
    }
}
