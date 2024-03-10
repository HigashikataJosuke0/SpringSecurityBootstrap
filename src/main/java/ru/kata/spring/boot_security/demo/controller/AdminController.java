package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceFind;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceFind userServiceFind;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserServiceFind userServiceFind, RoleRepository roleRepository, UserRepository userRepository) {
        this.userServiceFind = userServiceFind;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAllUsers(ModelMap model, Principal principal) {
        String user = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(user));
        model.addAttribute("allusers", userServiceFind.allUsers());
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "allusers";
    }

    @GetMapping("addnewuser")
    public String addNewUser(ModelMap model) {
        model.addAttribute("user", new User());
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "addnewuser";
    }

    @PostMapping("saveUser")
    public String saveUser(ModelMap modelMap, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("user", user);
            List<Role> roles = roleRepository.findAll();
            modelMap.addAttribute("allRoles", roles);

            return "redirect:/admin/addnewuser";
        }
        userServiceFind.save(user);
        return "redirect:/admin";
    }

    @GetMapping("updateInfo")
    public String updateUser(@RequestParam("usrId") long id, ModelMap model) {
        User user = userServiceFind.findUserById(id);
        model.addAttribute("user", user);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "addnewuser";
    }


    @PatchMapping("/updateInfo/{id}")
    public String add(@PathVariable("id") Long id, @Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = (List<Role>) roleRepository.findAll();
            model.addAttribute("allRoles", roles);
            return "addnewuser";
        } else {
            userServiceFind.update(id, user);
            return "redirect:/admin";
        }
    }


    @GetMapping("deleteUser")
    public String deleteUser(@RequestParam("usrId") long id) {
        userServiceFind.deleteUser(id);
        return "redirect:/admin";
    }
}
