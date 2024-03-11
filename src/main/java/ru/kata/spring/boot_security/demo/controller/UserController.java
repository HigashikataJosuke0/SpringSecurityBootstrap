package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserDetService userDetService;


    @Autowired
    public UserController(UserDetService userDetService) {
        this.userDetService = userDetService;
    }



    @GetMapping
    public String user(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userDetService.findByUSerName(userName);
        model.addAttribute("user", user);
        return "user";
    }
}
