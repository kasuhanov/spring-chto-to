package su.asgor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "document")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="document_seq_gen")
    @SequenceGenerator(name="document_seq_gen", sequenceName="document_seq")
    private long id;
    private String name;
    private String url;
    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    @JsonIgnore
    private Purchase purchase;
    public Document() { }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
    
}
