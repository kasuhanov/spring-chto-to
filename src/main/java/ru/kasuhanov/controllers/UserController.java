package ru.kasuhanov.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kasuhanov.model.User;
import ru.kasuhanov.model.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository repository;

    @RequestMapping(value = "/users",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<User> findAll() {
        List<User> users;
        try {
            users = new ArrayList<>();
            repository.findAll().forEach(users::add);
            return users;
        }
        catch (Exception ex) {
            return null;
        }
    }
    @RequestMapping(value = "/getbyid",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User getById(long id) {
        try {
            User user = repository.findOne(id);
            return user;
        }
        catch (Exception ex) {
            return null;
        }
    }
    @RequestMapping(value = "/create",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User create(String email, String name) {
        User user;
        try {
            user = new User(email, name);
            repository.save(user);
            return user;
        }
        catch (Exception ex) {
            return null;
        }
    }
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User delete(long id) {
        try {
            User user = new User(id);
            repository.delete(user);
            return user;
        }
        catch (Exception ex) {
            return null;
        }
    }
    @RequestMapping(value = "/get-by-email",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getByEmail(String email) {
        String userId;
        try {
            User user = repository.findByEmail(email);
            userId = String.valueOf(user.getId());
        }
        catch (Exception ex) {
            return "User not found";
        }
        return "The user id is: " + userId;
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User updateUser(long id, String email, String name) {
        try {
            User user = repository.findOne(id);
            user.setEmail(email);
            user.setName(name);
            repository.save(user);
            return user;
        }
        catch (Exception ex) {
            return null;
        }
    }
}