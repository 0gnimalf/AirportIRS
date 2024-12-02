package ru.fa.AirportIRS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fa.AirportIRS.models.FlightStatusType;
import ru.fa.AirportIRS.models.Person;
import ru.fa.AirportIRS.security.UserDetailsCustom;
import ru.fa.AirportIRS.service.FlightService;
import ru.fa.AirportIRS.service.PersonService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class UserController {
    @Autowired
    PersonService personService;
    @Autowired
    FlightService flightService;

    @GetMapping
    public String home(@AuthenticationPrincipal UserDetailsCustom userDetails, Model model) {
        Person person = personService.getByUsername(userDetails.getUsername());
        model.addAttribute("current_user", person.getFirstName());
        model.addAttribute("admin", person.getRoles().contains("ROLE_ADMIN"));
        return "home/home";
    }
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetailsCustom userDetails, Model model) {
        Person person = personService.getByUsername(userDetails.getUsername());
        model.addAttribute("person", person);
        return "home/personalAccount/profile";
    }
    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal UserDetailsCustom userDetails, Model model) {
        Person person = personService.getByUsername(userDetails.getUsername());
        model.addAttribute("editPerson", person);
        return "home/personalAccount/edit-profile";

    }
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("editPerson") Person person,
                                @AuthenticationPrincipal UserDetailsCustom userDetails) {
        Person personToUpdate = personService.getByUsername(userDetails.getUsername());
        personToUpdate.setFirstName(person.getFirstName());
        personToUpdate.setLastName(person.getLastName());
        personToUpdate.setBirthDate(person.getBirthDate());
        personToUpdate.setPhone(person.getPhone());

        personService.save(personToUpdate);
        return "redirect:/home";
    }
    @GetMapping("/statistic")
    public String statistic(Model model,
                            @RequestParam(required = false) List<FlightStatusType> statuses,
                            @RequestParam(name = "year", defaultValue = "2024") Integer year
    ) {
        model.addAttribute("statuses", FlightStatusType.values());
        if (statuses != null) {
        Map<String, Map<String, Integer>> data = flightService.getByStatusAndYear(statuses, year);
        model.addAttribute("statusesChartData", data);
        }
        return "home/statistic";
    }

}
