package ru.kasuhanov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kasuhanov.model.Category;
import ru.kasuhanov.model.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryRepository repository;

    @RequestMapping(value = "/getall",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        repository.findAll().forEach(categories::add);
        return categories;
    }
    @RequestMapping(value = "/create",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Category create(String name) {
        Category category = new Category(name);
        repository.save(category);
        return category;
    }
}
