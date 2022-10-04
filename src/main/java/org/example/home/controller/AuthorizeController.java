package org.example.home.controller;

import lombok.AllArgsConstructor;
import org.example.home.domain.AppUser;
import org.example.home.service.CustomUserDetailsService;
import org.example.home.service.UserService;
import org.example.home.service.UserServiceImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthorizeController {
    private final UserService userService;
    @GetMapping("/registered")
    public String registered(Model model){
        model.addAttribute("user", new AppUser());
        return "registered";
    }
    @PostMapping("/registered")
    public String addUser(@ModelAttribute("userForm") AppUser appUser){

       AppUser user = userService.saveUser(appUser);

        return "redirect:/";
    }

}
