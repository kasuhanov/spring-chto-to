package su.asgor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import su.asgor.dao.CustomerRepository;
import su.asgor.dao.PurchaseRepository;
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
    public Page<Purchase> getPurchasesPage(@PathVariable long id, @RequestParam(required = true) int page,
    		@RequestParam(required = true) int pageSize) {
        Pageable pageable = new PageRequest(page, pageSize);
        return purchaseRepository.findByCustomer(repository.findOne(id),pageable);
    }
}