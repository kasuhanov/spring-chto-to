package ru.kasuhanov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kasuhanov.Dao.PurchaseRepository;
import ru.kasuhanov.model.Purchase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseRepository repository;

    @RequestMapping(value = "/getall",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Purchase> findAll() {
        List<Purchase> purchases = new ArrayList<>();
        repository.findAll().forEach(purchases::add);
        return purchases;
    }
    @RequestMapping(value = "/getbyid",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Purchase getbyid(long id) {
        return repository.findOne(id);
    }
    @RequestMapping(value = "/create",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Purchase create(String name,Date date) {
        Purchase purchase = new Purchase(name,date);
        repository.save(purchase);
        return purchase;
    }
}