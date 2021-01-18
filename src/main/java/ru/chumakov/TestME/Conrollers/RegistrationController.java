package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.chumakov.TestME.models.Role;
import ru.chumakov.TestME.models.User;
import ru.chumakov.TestME.repos.UserRepo;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String getRegister(){
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String user_name,
                               @RequestParam String user_last_name,
                               @RequestParam Map<String,String> form,
                               Model model){

        User userFromDB = userRepo.findByUsername(username);

        if (userFromDB!=null){
            model.addAttribute("userError", "Пользователь c таким логином уже существует");
            return "registration";
        }
        User user = new User(user_name,user_last_name,username,password,true);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        for(String key : form.keySet()){
            if (roles.contains(key)){
                user.setRoles(Set.of(Role.valueOf(key),Role.USER));
            }
            else
                user.setRoles(Set.of(Role.USER));
        }

        userRepo.save(user);

        return "redirect:/login";
    }

}
