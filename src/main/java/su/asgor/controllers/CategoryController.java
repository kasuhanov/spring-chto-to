package su.asgor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import su.asgor.Dao.CategoryRepository;
import su.asgor.model.Category;
import su.asgor.model.Purchase;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryRepository repository;

    @RequestMapping(value = "/getall",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Iterable<Category> findAll() {
        Iterable<Category> categories = repository.findAll();
        categories.forEach(Category::setupCount);
        return categories;
    }
    @RequestMapping(value = "/getpurchases",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Purchase> getPurchases(long id) {
        return repository.findOne(id).getPurchases();
    }
}
