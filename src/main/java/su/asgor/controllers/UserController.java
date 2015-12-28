package su.asgor.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import su.asgor.Dao.UserRepository;
import su.asgor.model.User;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository repository;

    @RequestMapping(value = "/getall",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        repository.findAll().forEach(users::add);
        return users;
    }
    @RequestMapping(value = "/getbyid",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User getById(long id) {
        return repository.findOne(id);
    }
    @RequestMapping(value = "/create",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User create(String email, String name, long age) {
        User user = new User(email, name, age);
        repository.save(user);
        return user;
    }
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User delete(long id) {
        User user = new User(id);
        repository.delete(user);
        return user;
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User updateUser(long id, String email, String name, long age) {
        User user = repository.findOne(id);
        user.setEmail(email);
        user.setName(name);
        user.setAge(age);
        repository.save(user);
        return user;
    }
}