package su.asgor.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import su.asgor.parser.generated.PurchaseNoticeStatusType;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "purchase")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Purchase {
    @Id
    private long id;
    private PurchaseType type;
    @Transient
    @JsonIgnore
    private PurchaseNoticeStatusType status;
    private Boolean completed = Boolean.FALSE;
    @Column(length = 1000)
    private String name;
    @Column(name = "submission_close_date")
    @Temporal(TemporalType.DATE)
    private Date submissionCloseDate;
    @Column(name = "start_price")
    private double startPrice;
    @Column(name = "url_oos")
    private String urlOOS;
    @Column(name = "purchase_code_name")
    private String purchaseCodeName;
    @ManyToMany(mappedBy="purchases")
    @JsonBackReference
    private List<Category> categories;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @OneToMany(mappedBy = "purchase", cascade={CascadeType.ALL})
    private List<Document> document;
    @OneToMany(mappedBy = "purchase", cascade={CascadeType.ALL})
    private List<Lot> lots;
    // AE
    @Column(name = "auction_time")
    @Temporal(TemporalType.DATE)
    private Date auctionTime;
    @Column(name = "documentation_delivery_procedure", length = 2000)
    private String documentationDeliveryProcedure;
    @Column(name = "documentation_delivery_place", length = 1000)
    private String documentationDeliveryPlace;
    @Column(name = "electronic_place_name")
    private String electronicPlaceName;
    @Column(name = "electronic_place_url")
    private String electronicPlaceUrl;
    // OA
    @Column(name="auction_place")
    private String auctionPlace;
    // ZK
    @Column(name="quotation_examination_place")
    private String quotationExaminationPlace;
    @Column(name = "quotation_examination_time")
    @Temporal(TemporalType.DATE)
    private Date quotationExaminationTime;
    //OK
    @Column(name="envelope_opening_place")
    private String envelopeOpeningPlace;
    @Column(name = "envelope_opening_time")
    @Temporal(TemporalType.DATE)
    private Date envelopeOpeningTime;
    @Column(name="examination_place")
    private String examinationPlace;
    @Column(name = "examination_date_time")
    @Temporal(TemporalType.DATE)
    private Date examinationDateTime;
    @Column(name="summingup_place")
    private String summingupPlace;
    @Column(name = "summingup_time")
    @Temporal(TemporalType.DATE)
    private Date summingupTime;
    // Contact
    @Column(name = "contact_first_name")
    private String contactFirstName;
    @Column(name = "contact_middle_name")
    private String contactMiddleName;
    @Column(name = "contact_last_name")
    private String contactLastName;
    @Column(name = "contact_email")
    private String contactEmail;
    @Column(name = "contact_phone")
    private String contactPhone;
    //    
    public Purchase() { }

    public Purchase(long id) {
        this.id = id;
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

    public Date getSubmissionCloseDate() {
        return submissionCloseDate;
    }

    public void setSubmissionCloseDate(Date submissionCloseDate) {
        this.submissionCloseDate = submissionCloseDate;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

	public PurchaseType getType() {
		return type;
	}

	public void setType(PurchaseType type) {
		this.type = type;
	}

	public PurchaseNoticeStatusType getStatus() {
		return status;
	}

	public String getPurchaseCodeName() {
		return purchaseCodeName;
	}

	public void setPurchaseCodeName(String purchaseCodeName) {
		this.purchaseCodeName = purchaseCodeName;
	}

	public void setStatus(PurchaseNoticeStatusType status) {
		this.status = status;
	}
	
	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Date getAuctionTime() {
		return auctionTime;
	}

	public void setAuctionTime(Date auctionTime) {
		this.auctionTime = auctionTime;
	}

	public String getDocumentationDeliveryPlace() {
		return documentationDeliveryPlace;
	}

	public void setDocumentationDeliveryPlace(String documentationDeliveryPlace) {
		this.documentationDeliveryPlace = documentationDeliveryPlace;
	}

	public String getDocumentationDeliveryProcedure() {
		return documentationDeliveryProcedure;
	}

	public void setDocumentationDeliveryProcedure(String documentationDeliveryProcedure) {
		this.documentationDeliveryProcedure = documentationDeliveryProcedure;
	}

	public String getElectronicPlaceName() {
		return electronicPlaceName;
	}

	public void setElectronicPlaceName(String electronicPlaceName) {
		this.electronicPlaceName = electronicPlaceName;
	}

	public String getElectronicPlaceUrl() {
		return electronicPlaceUrl;
	}

	public void setElectronicPlaceUrl(String electronicPlaceUrl) {
		this.electronicPlaceUrl = electronicPlaceUrl;
	}

	public String getContactFirstName() {
		return contactFirstName;
	}

	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}

	public String getContactMiddleName() {
		return contactMiddleName;
	}

	public void setContactMiddleName(String contactMiddleName) {
		this.contactMiddleName = contactMiddleName;
	}

	public String getContactLastName() {
		return contactLastName;
	}

	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getUrlOOS() {
		return urlOOS;
	}

	public void setUrlOOS(String urlOOS) {
		this.urlOOS = urlOOS;
	}

	public List<Document> getDocument() {
		return document;
	}

	public void setDocument(List<Document> document) {
		this.document = document;
	}

	public String getAuctionPlace() {
		return auctionPlace;
	}

	public void setAuctionPlace(String auctionPlace) {
		this.auctionPlace = auctionPlace;
	}

	public String getQuotationExaminationPlace() {
		return quotationExaminationPlace;
	}

	public void setQuotationExaminationPlace(String quotationExaminationPlace) {
		this.quotationExaminationPlace = quotationExaminationPlace;
	}

	public Date getQuotationExaminationTime() {
		return quotationExaminationTime;
	}

	public void setQuotationExaminationTime(Date quotationExaminationTime) {
		this.quotationExaminationTime = quotationExaminationTime;
	}

	public String getEnvelopeOpeningPlace() {
		return envelopeOpeningPlace;
	}

	public void setEnvelopeOpeningPlace(String envelopeOpeningPlace) {
		this.envelopeOpeningPlace = envelopeOpeningPlace;
	}

	public Date getEnvelopeOpeningTime() {
		return envelopeOpeningTime;
	}

	public void setEnvelopeOpeningTime(Date envelopeOpeningTime) {
		this.envelopeOpeningTime = envelopeOpeningTime;
	}

	public String getExaminationPlace() {
		return examinationPlace;
	}

	public void setExaminationPlace(String examinationPlace) {
		this.examinationPlace = examinationPlace;
	}

	public Date getExaminationDateTime() {
		return examinationDateTime;
	}

	public void setExaminationDateTime(Date examinationDateTime) {
		this.examinationDateTime = examinationDateTime;
	}

	public String getSummingupPlace() {
		return summingupPlace;
	}

	public void setSummingupPlace(String summingupPlace) {
		this.summingupPlace = summingupPlace;
	}

	public Date getSummingupTime() {
		return summingupTime;
	}

	public void setSummingupTime(Date summingupTime) {
		this.summingupTime = summingupTime;
	}

	public List<Lot> getLots() {
		return lots;
	}

	public void setLots(List<Lot> lots) {
		this.lots = lots;
	} 
	
}
