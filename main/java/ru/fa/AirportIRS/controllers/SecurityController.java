package ru.fa.AirportIRS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fa.AirportIRS.models.Person;
import ru.fa.AirportIRS.service.PersonService;

import java.util.Set;

@Controller
@RequestMapping("/auth")
public class SecurityController {
    @Autowired
    private PersonService personService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String getSignUp(Model model) {
        model.addAttribute("new_person", new Person());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String postSignUp(Model model, @ModelAttribute("new_person") Person person) {
        if (personService.existsByUsername(person.getUsername())){
            model.addAttribute("signup_error", "This username is already taken");
            return "/auth/signup";
        }
        if (personService.existsByEmail(person.getEmail())){
            model.addAttribute("signup_error", "User with this email is already exists");
            return "/auth/signup";
        }
        String pass = passwordEncoder.encode(person.getPassword());
        person.setPassword(pass);
        if (person.getUsername().equals("admin")){
            person.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));
        }else {person.setRoles(Set.of("ROLE_USER"));}
        personService.save(person);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String getLogIn(Model model,
                           @RequestParam(name = "error", required = false) boolean error) {
        if (error) {
            model.addAttribute("login_error", "Invalid username or password");
        }
        model.addAttribute("auth_person", new Person());
        return "auth/login";
    }
}
