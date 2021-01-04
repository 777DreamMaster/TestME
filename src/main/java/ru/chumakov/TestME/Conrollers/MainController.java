package ru.chumakov.TestME.Conrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "TestME");
        return "greeting";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "TestME");
        return "home";
    }

}
