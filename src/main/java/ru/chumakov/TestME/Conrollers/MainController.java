package ru.chumakov.TestME.Conrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String getHomePage(Model model) {
        model.addAttribute("title", "TestME");
        return "home";
    }
}
