package su.asgor.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="purchase_seq_gen")
    @SequenceGenerator(name="purchase_seq_gen", sequenceName="purchase_seq")
    private long id;
    @NotNull
    private String name;
    @Column(name = "start_time")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date startDate;
    @Column(name = "start_price")
    @NotNull
    private double startPrice;

    @ManyToMany(mappedBy="purchases")
    @JsonBackReference
    private List<Category> categories;

    public Purchase() { }

    public Purchase(long id) {
        this.id = id;
    }

    public Purchase(String name, Date startDate, double startPrice) {
        this.name = name;
        this.startDate = startDate;
        this.startPrice = startPrice;
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

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
