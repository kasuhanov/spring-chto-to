package su.asgor.controllers;

import com.mysema.query.types.expr.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import su.asgor.dao.PurchaseRepository;
import su.asgor.model.Purchase;
import su.asgor.model.QPurchase;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private PurchaseRepository purchaseRepository;
    @RequestMapping(value = "/simple",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Purchase> simpleSearch(@RequestParam(required = true)String text) {
        return purchaseRepository.findByNameContainingIgnoreCase(text);
    }
    @RequestMapping(value = "/simple_paged",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<Purchase> simplePagedSearch(@RequestParam(required = true)String text,
    		@RequestParam(required = true) int page, @RequestParam(required = true) int pageSize) {
    	Pageable pageable = new PageRequest(page, pageSize);
        return purchaseRepository.findByNameContainingIgnoreCase(text,pageable);
    }
    @RequestMapping(value = "/advanced_paged",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<Purchase> advancedPagedSearch(String purchaseName, String customer, Long startDate, Long endDate,
                Double minPrice, Double maxPrice, Long category,@RequestParam(required = true) int page,
                                              @RequestParam(required = true) int pageSize) {
        Pageable pageable = new PageRequest(page, pageSize);
        QPurchase qPurchase = QPurchase.purchase;
        BooleanExpression predicate = qPurchase.isNotNull();
        boolean resFlag = false;
        if(purchaseName!=null){
            predicate = predicate.and(qPurchase.name.containsIgnoreCase(purchaseName));
            resFlag = true;
        }
        if(customer!=null){
            predicate = predicate.and(qPurchase.customer.name.containsIgnoreCase(customer));
            resFlag = true;
        }
        if(category!=null){
            predicate = predicate.and(qPurchase.categories.any().id.eq(category));
            resFlag = true;
        }
        if(minPrice!=null){
            predicate = predicate.and(qPurchase.startPrice.goe(minPrice));
            resFlag = true;
        }
        if(maxPrice!=null){
            predicate = predicate.and(qPurchase.startPrice.loe(maxPrice));
            resFlag = true;
        }
        if(startDate!=null){
            Date date = new Date(startDate);
            predicate = predicate.and(qPurchase.submissionCloseDate.goe(date));
            resFlag = true;
        }
        if(endDate!=null){
            Date date = new Date(endDate);
            predicate = predicate.and(qPurchase.submissionCloseDate.loe(date));
            resFlag = true;
        }
        if(resFlag){
            return purchaseRepository.findAll(predicate,pageable);
        }else{
            return null;
        }
    }
}