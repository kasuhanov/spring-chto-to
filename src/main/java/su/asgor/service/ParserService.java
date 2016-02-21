package su.asgor.service;

import su.asgor.dao.CustomerRepository;
import su.asgor.dao.PurchaseRepository;
import su.asgor.model.Customer;
import su.asgor.model.Document;
import su.asgor.model.Lot;
import su.asgor.model.LotItem;
import su.asgor.model.Purchase;
import su.asgor.model.PurchaseType;
import su.asgor.parser.generated.CustomerMainInfoType;
import su.asgor.parser.generated.DocumentListType;
import su.asgor.parser.generated.LotListType;
import su.asgor.parser.generated.PurchaseNoticeAE;
import su.asgor.parser.generated.PurchaseNoticeAEDataType;
import su.asgor.parser.generated.PurchaseNoticeEP;
import su.asgor.parser.generated.PurchaseNoticeEPDataType;
import su.asgor.parser.generated.PurchaseNoticeOA;
import su.asgor.parser.generated.PurchaseNoticeOADataType;
import su.asgor.parser.generated.PurchaseNoticeOK;
import su.asgor.parser.generated.PurchaseNoticeOKDataType;
import su.asgor.parser.generated.PurchaseNoticeZK;
import su.asgor.parser.generated.PurchaseNoticeZKDataType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ParserService {
	@Autowired
	private PurchaseRepository purchaseRepository;
	@Autowired
	private CustomerRepository customerRepository;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
    public void parse(String pathToFile, String fileName) throws Exception {
        boolean correctFileName = false;
        Purchase purchase = null;
        try {
        	if(fileName.startsWith("purchaseNoticeAE")){
                correctFileName = true;
                purchase = parseAE(pathToFile,fileName);
            }
            if(fileName.startsWith("purchaseNoticeEP")){
                correctFileName = true;
                purchase = parseEP(pathToFile,fileName);
            }
            if(fileName.startsWith("purchaseNoticeOA")){
                correctFileName = true;
                purchase = parseOA(pathToFile,fileName);
            }
            if(fileName.startsWith("purchaseNoticeZK")){
                correctFileName = true;
                purchase = parseZK(pathToFile,fileName);
            }
            if(fileName.startsWith("purchaseNoticeOK")){
                correctFileName = true;
                purchase = parseOK(pathToFile,fileName);
            }
            if(!correctFileName){
                throw new Exception("invalid fileName");
            }
            switch (purchase.getStatus()) {
			case I:
				log.info("I type purchase notice");
            	Purchase purchaseInDB = purchaseRepository.findOne(purchase.getId());
            	purchaseInDB.setCompleted(true);
            	purchaseRepository.save(purchaseInDB);
				break;
			case P:
				log.info("P type purchase notice");
				customerRepository.save(purchase.getCustomer());
				if(purchaseRepository.exists(purchase.getId()))
					throw new Exception("Purchase with id = "+purchase.getId()+", already exists");
				purchaseRepository.save(purchase);
				break;
			case F:
			case M:	
				log.info("F or M type purchase notice");
				break;
			}
		} catch (Exception e) {
			log.error("exception in file:"+fileName+" Messaage:"+e.getMessage(),e);
			throw new Exception(e);
		}
    }
    private Purchase parseAE(String pathToFile, String fileName) throws JAXBException {
        log.info("Извещения об аукционах в электронном виде - "+fileName);
        JAXBContext jc = JAXBContext.newInstance(PurchaseNoticeAE.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        PurchaseNoticeAE purchaseNoticeAE = (PurchaseNoticeAE)unmarshaller.unmarshal(new File(pathToFile+fileName));
        PurchaseNoticeAEDataType data = purchaseNoticeAE.getBody().getItem().getPurchaseNoticeAEData();
        Customer customer = parseCustomer(data.getCustomer().getMainInfo());        
        Purchase purchase = new Purchase();
        purchase.setId(Long.parseLong(data.getRegistrationNumber()));
        purchase.setType(PurchaseType.AE);
        purchase.setStatus(data.getStatus());
        purchase.setName(data.getName());
        purchase.setStartPrice(0);
        purchase.setSubmissionCloseDate(data.getSubmissionCloseDateTime().toGregorianCalendar().getTime());
        purchase.setAuctionTime(data.getAuctionTime().toGregorianCalendar().getTime());
        if(purchase.getAuctionTime().before(new Date()))
        	purchase.setCompleted(true);
        purchase.setDocumentationDeliveryPlace(data.getDocumentationDelivery().getPlace());
        purchase.setDocumentationDeliveryProcedure(data.getDocumentationDelivery().getProcedure());
        purchase.setElectronicPlaceName(data.getElectronicPlaceInfo().getName());
        purchase.setElectronicPlaceUrl(data.getElectronicPlaceInfo().getUrl());
        purchase.setContactFirstName(data.getContact().getFirstName());
        purchase.setContactMiddleName(data.getContact().getMiddleName());
        purchase.setContactLastName(data.getContact().getLastName());
        purchase.setContactPhone(data.getContact().getPhone());
        purchase.setContactEmail(data.getContact().getEmail());
        purchase.setUrlOOS(data.getUrlOOS());
        purchase.setPurchaseCodeName(data.getPurchaseCodeName());
        purchase.setLots(parseLots(data.getLots(), purchase));
        purchase.setDocument(parseDocs(data.getAttachments(), purchase));
        purchase.setCustomer(customer);
        return purchase;
    }
    private Purchase parseEP(String pathToFile, String fileName) throws JAXBException {
    	log.info("Извещения о закупках у единственного поставщика - "+fileName);
        JAXBContext jc = JAXBContext.newInstance(PurchaseNoticeEP.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        PurchaseNoticeEP purchaseNoticeEP = (PurchaseNoticeEP)unmarshaller.unmarshal(new File(pathToFile+fileName));
        PurchaseNoticeEPDataType data = purchaseNoticeEP.getBody().getItem().getPurchaseNoticeEPData();
        Customer customer = parseCustomer(data.getCustomer().getMainInfo());  
        Purchase purchase = new Purchase();
        purchase.setId(Long.parseLong(data.getRegistrationNumber()));
        purchase.setType(PurchaseType.EP);
        purchase.setStatus(data.getStatus());
        purchase.setCompleted(true);
        purchase.setName(data.getName());
        purchase.setStartPrice(0);
        purchase.setContactFirstName(data.getContact().getFirstName());
        purchase.setContactMiddleName(data.getContact().getMiddleName());
        purchase.setContactLastName(data.getContact().getLastName());
        purchase.setContactPhone(data.getContact().getPhone());
        purchase.setContactEmail(data.getContact().getEmail());
        purchase.setUrlOOS(data.getUrlOOS());
        purchase.setPurchaseCodeName(data.getPurchaseCodeName());
        purchase.setLots(parseLots(data.getLots(), purchase));
        purchase.setDocument(parseDocs(data.getAttachments(), purchase));
        purchase.setCustomer(customer);
        return purchase;
    }
    private Purchase parseOA(String pathToFile, String fileName) throws JAXBException {
    	log.info("Извещения об открытых аукционах - "+fileName);
        JAXBContext jc = JAXBContext.newInstance(PurchaseNoticeOA.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        PurchaseNoticeOA purchaseNoticeOA = (PurchaseNoticeOA)unmarshaller.unmarshal(new File(pathToFile+fileName));
        PurchaseNoticeOADataType data = purchaseNoticeOA.getBody().getItem().getPurchaseNoticeOAData();
        Customer customer = parseCustomer(data.getCustomer().getMainInfo());  
        Purchase purchase = new Purchase();
        purchase.setId(Long.parseLong(data.getRegistrationNumber()));
        purchase.setType(PurchaseType.OA);
        purchase.setStatus(data.getStatus());
        purchase.setName(data.getName());
        purchase.setStartPrice(0);
        purchase.setSubmissionCloseDate(data.getSubmissionCloseDateTime().toGregorianCalendar().getTime());
        purchase.setAuctionPlace(data.getAuctionPlace());
        purchase.setAuctionTime(data.getAuctionTime().toGregorianCalendar().getTime());
        if(purchase.getAuctionTime().before(new Date()))
        	purchase.setCompleted(true);
        purchase.setContactFirstName(data.getContact().getFirstName());
        purchase.setContactMiddleName(data.getContact().getMiddleName());
        purchase.setContactLastName(data.getContact().getLastName());
        purchase.setContactPhone(data.getContact().getPhone());
        purchase.setContactEmail(data.getContact().getEmail());
        purchase.setUrlOOS(data.getUrlOOS());
        purchase.setPurchaseCodeName(data.getPurchaseCodeName());
        purchase.setLots(parseLots(data.getLots(), purchase));
        purchase.setDocument(parseDocs(data.getAttachments(), purchase));
        purchase.setCustomer(customer);
        return purchase;
    }
    private Purchase parseOK(String pathToFile, String fileName) throws JAXBException {
    	log.info("Извещения об открытых конкурсах - "+fileName);
        JAXBContext jc = JAXBContext.newInstance(PurchaseNoticeOK.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        PurchaseNoticeOK purchaseNoticeOK = (PurchaseNoticeOK)unmarshaller.unmarshal(new File(pathToFile+fileName));
        PurchaseNoticeOKDataType data = purchaseNoticeOK.getBody().getItem().getPurchaseNoticeOKData();
        Customer customer = parseCustomer(data.getCustomer().getMainInfo());  
        Purchase purchase = new Purchase();
        purchase.setId(Long.parseLong(data.getRegistrationNumber()));
        purchase.setType(PurchaseType.OK);
        purchase.setStatus(data.getStatus());
        purchase.setName(data.getName());
        purchase.setStartPrice(0);
        purchase.setSubmissionCloseDate(data.getSubmissionCloseDateTime().toGregorianCalendar().getTime());
        purchase.setEnvelopeOpeningPlace(data.getEnvelopeOpeningPlace());
        purchase.setExaminationPlace(data.getExaminationPlace());
        purchase.setSummingupPlace(data.getSummingupPlace());
        purchase.setExaminationDateTime(data.getExaminationDateTime().toGregorianCalendar().getTime());
        purchase.setEnvelopeOpeningTime(data.getEnvelopeOpeningTime().toGregorianCalendar().getTime());
        purchase.setSummingupTime(data.getSummingupTime().toGregorianCalendar().getTime());
        if(purchase.getSummingupTime().before(new Date()))
        	purchase.setCompleted(true);
        purchase.setContactFirstName(data.getContact().getFirstName());
        purchase.setContactMiddleName(data.getContact().getMiddleName());
        purchase.setContactLastName(data.getContact().getLastName());
        purchase.setContactPhone(data.getContact().getPhone());
        purchase.setContactEmail(data.getContact().getEmail());
        purchase.setUrlOOS(data.getUrlOOS());
        purchase.setPurchaseCodeName(data.getPurchaseCodeName());
        purchase.setLots(parseLots(data.getLots(), purchase));
        purchase.setDocument(parseDocs(data.getAttachments(), purchase));
        purchase.setCustomer(customer);
        return purchase;
    }
    private Purchase parseZK(String pathToFile, String fileName) throws JAXBException {
    	log.info("Извещения о запросах котировок - "+fileName);
        JAXBContext jc = JAXBContext.newInstance(PurchaseNoticeZK.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        PurchaseNoticeZK purchaseNoticeZK = (PurchaseNoticeZK)unmarshaller.unmarshal(new File(pathToFile+fileName));
        PurchaseNoticeZKDataType data = purchaseNoticeZK.getBody().getItem().getPurchaseNoticeZKData();
        Customer customer = parseCustomer(data.getCustomer().getMainInfo());  
        Purchase purchase = new Purchase();
        purchase.setId(Long.parseLong(data.getRegistrationNumber()));
        purchase.setType(PurchaseType.ZK);
        purchase.setStatus(data.getStatus());
        purchase.setName(data.getName());
        purchase.setStartPrice(0);
        purchase.setSubmissionCloseDate(data.getSubmissionCloseDateTime().toGregorianCalendar().getTime());
        purchase.setQuotationExaminationPlace(data.getQuotationExaminationPlace());
        purchase.setQuotationExaminationTime(data.getQuotationExaminationTime().toGregorianCalendar().getTime());
        if(purchase.getQuotationExaminationTime().before(new Date()))
        	purchase.setCompleted(true);
        purchase.setContactFirstName(data.getContact().getFirstName());
        purchase.setContactMiddleName(data.getContact().getMiddleName());
        purchase.setContactLastName(data.getContact().getLastName());
        purchase.setContactPhone(data.getContact().getPhone());
        purchase.setContactEmail(data.getContact().getEmail());
        purchase.setUrlOOS(data.getUrlOOS());
        purchase.setPurchaseCodeName(data.getPurchaseCodeName());
        purchase.setLots(parseLots(data.getLots(), purchase));
        purchase.setDocument(parseDocs(data.getAttachments(), purchase));
        purchase.setCustomer(customer);
        return purchase;
    }
    private Customer parseCustomer(CustomerMainInfoType customerInfo){
    	Customer customer = new Customer();
    	customer.setId(Long.parseLong(customerInfo.getInn()));
        if((customerInfo.getFullName()==null)||(customerInfo.getFullName().isEmpty())){
        	customer.setName(customerInfo.getShortName());
        }else{
        	customer.setName(customerInfo.getFullName());
        }
        customer.setPhone(customerInfo.getPhone());
        if((customerInfo.getPostalAddress()==null)||(customerInfo.getPostalAddress().isEmpty())){
        	customer.setPostalAddress(customerInfo.getLegalAddress());
        }else{
        	customer.setPostalAddress(customerInfo.getPostalAddress());
        }
        customer.setEmail(customerInfo.getEmail());
        customer.setFax(customerInfo.getFax());
    	return customer;
    }
    private List<Lot> parseLots(LotListType lotData, Purchase purchase){
    	List<Lot> lotList = new ArrayList<Lot>();
    	lotData.getLot().forEach(lt -> {
    		Lot lot = new Lot();
    		lot.setPurchase(purchase);
    		lot.setName(lt.getLotData().getSubject());
        	lot.setAddress(lt.getLotData().getDeliveryPlace().getAddress());
        	lot.setStartPrice(lt.getLotData().getInitialSum().doubleValue());
        	purchase.setStartPrice(purchase.getStartPrice()+lot.getStartPrice());
        	lot.setForSMP(lt.getLotData().isForSmallOrMiddle()||lt.getLotData().isSubcontractorsRequirement());
        	lot.setCurrency(lt.getLotData().getCurrency().getCode());
        	List<LotItem> lotItems = new ArrayList<>();
        	lt.getLotData().getLotItems().getLotItem().forEach(li->{
        		LotItem  lotItem = new LotItem();
        		lotItem.setCodeOkdp(li.getOkdp().getCode());
        		lotItem.setCodeOkved(li.getOkved().getCode());
        		lotItem.setLot(lot);
        		lotItem.setOkeiName(li.getOkei().getName());
        		lotItem.setOrdinalNumber(li.getOrdinalNumber());
        		lotItem.setQty(li.getQty());
        		lotItem.setAdditionalInfo(li.getAdditionalInfo());
        		lotItems.add(lotItem);
        	});
        	lot.setLotItems(lotItems);
        	lotList.add(lot);
    	});
    	return lotList;
    }
    private List<Document> parseDocs(DocumentListType data, Purchase purchase){
    	List<Document> documents = new ArrayList<>();
        data.getDocument().forEach(doc->{
        	Document document = new Document();
        	document.setName(doc.getFileName());
        	document.setUrl(doc.getUrl());
        	document.setPurchase(purchase);
        	documents.add(document);
        });
        return documents;
    }
}
