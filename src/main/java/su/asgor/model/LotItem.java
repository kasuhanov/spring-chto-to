package su.asgor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "lot_item")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LotItem {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="lot_item_seq_gen")
    @SequenceGenerator(name="lot_item_seq_gen", sequenceName="lot_item_seq")
    private long id;
    private String name;
    @Column(name="ordinal_number")
    private int ordinalNumber;
    @Column(name="code_okdp")
    private String codeOkdp;
    @Column(name="code_okved")
    private String codeOkved;
    private BigDecimal qty;
    @Column(name="okei_name")
    private String okeiName;
    @Column(name="additional_info")
    private String additionalInfo;
    @ManyToOne
    @JoinColumn(name = "lot_id")
    @JsonIgnore
    private Lot lot;
    public LotItem() { }
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrdinalNumber() {
		return ordinalNumber;
	}
	public void setOrdinalNumber(int ordinalNumber) {
		this.ordinalNumber = ordinalNumber;
	}
	public String getCodeOkdp() {
		return codeOkdp;
	}
	public void setCodeOkdp(String codeOkdp) {
		this.codeOkdp = codeOkdp;
	}
	public String getCodeOkved() {
		return codeOkved;
	}
	public void setCodeOkved(String codeOkved) {
		this.codeOkved = codeOkved;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public String getOkeiName() {
		return okeiName;
	}
	public void setOkeiName(String okeiName) {
		this.okeiName = okeiName;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	@Override
	public String toString() {
		return "LotItem [id=" + id + ", name=" + name + ", ordinalNumber=" + ordinalNumber + ", codeOkdp=" + codeOkdp
				+ ", codeOkved=" + codeOkved + ", qty=" + qty + ", okeiName=" + okeiName + ", additionalInfo="
				+ additionalInfo + "]";
	}
	
}
