package ru.kasuhanov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kasuhanov.Dao.PurchaseRepository;
import ru.kasuhanov.model.Purchase;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseRepository repository;

    @RequestMapping(value = "/getall",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Iterable<Purchase> findAll() {
        return repository.findAll();
    }
    @RequestMapping(value = "/getbyid",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Purchase getbyid(long id) {
        return repository.findOne(id);
    }
}