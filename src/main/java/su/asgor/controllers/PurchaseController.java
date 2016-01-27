package su.asgor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import su.asgor.dao.PurchaseRepository;
import su.asgor.model.Purchase;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseRepository repository;
    @RequestMapping(value = "/all",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Iterable<Purchase> getAll() {
        return repository.findAll();
    }
    @RequestMapping(value = "/page",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<Purchase> getPurchasesPage(@RequestParam(required = true) int page,
    		@RequestParam(required = true) int pageSize){
        Pageable pageable = new PageRequest(page, pageSize);
        return repository.findAllByOrderById(pageable);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Purchase getPurchase(@PathVariable long id) {
        return repository.findOne(id);
    }
}