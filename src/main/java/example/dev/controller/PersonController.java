package example.dev.controller;

import example.dev.entity.Person;
import example.dev.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("persons", personService.getAllPersons());
        return "index";
    }

    @PostMapping("/add")
    public String addPerson(@RequestParam String name, @RequestParam String number) {
        Person person = new Person();
        person.setName(name);
        person.setNumber(number);
        personService.savePerson(person);
        return "redirect:/";
    }
}
