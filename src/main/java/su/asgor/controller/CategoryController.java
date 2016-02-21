package su.asgor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import su.asgor.dao.CategoryRepository;
import su.asgor.dao.PurchaseRepository;
import su.asgor.model.Category;
import su.asgor.model.Purchase;


@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryRepository repository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    @RequestMapping(value = "/all",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Iterable<Category> getAll() {
        Iterable<Category> categories = repository.findAll();
        categories.forEach(Category::setupCount);
        return categories;
    }
    @RequestMapping(value = "/{id}/purchases",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<Purchase> getPurchasesPage(@PathVariable long id, @RequestParam(required = true) int page,
    		@RequestParam(required = true) int pageSize) {
        Pageable pageable = new PageRequest(page, pageSize);
        return purchaseRepository.findByCategories(repository.findOne(id),pageable);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Category getByID(@PathVariable long id) {
        return repository.findOne(id);
    }
}
