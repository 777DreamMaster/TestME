package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.chumakov.TestME.models.Role;
import ru.chumakov.TestME.models.User;
import ru.chumakov.TestME.repos.UserRepo;

import java.util.Collections;


@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(User user, Model model){

        User userFromDB = userRepo.findByUsername(user.getUsername());

        if (userFromDB!=null){
            model.addAttribute("message", "Пользователь уже существует");
            return registration();
        }

        user.setRoles(Collections.singleton(Role.USER));
        user.setActive(true);
        userRepo.save(user);

        return "redirect:/login";
    }

}
