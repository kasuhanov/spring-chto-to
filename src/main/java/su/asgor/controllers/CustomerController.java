package su.asgor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import su.asgor.Dao.CustomerRepository;
import su.asgor.Dao.PurchaseRepository;
import su.asgor.model.Customer;
import su.asgor.model.Purchase;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @RequestMapping(value = "/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Customer getCustomer(@PathVariable long id) {
        return repository.findOne(id);
    }
    @RequestMapping(value = "/{id}/purchases",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<Purchase> getPurchases(@PathVariable long id, int page, int pageSize) {
        Pageable pageable = new PageRequest(page, pageSize);
        return purchaseRepository.findByCustomer(repository.findOne(id),pageable);
    }
}