package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.chumakov.TestME.models.User;
import ru.chumakov.TestME.repos.UserRepo;

import java.util.ArrayList;
import java.util.Optional;


@Controller
public class UsersController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/users")
    public String getUsers(Model model) {
        Iterable <User> users = userRepo.findAll();
        model.addAttribute("users",users);
        return "users";
    }

    @GetMapping("/users/add")
    public String addUser(Model model) {
        return "users-add";
    }

    @PostMapping("/users/add")
    public String userPost(@RequestParam String user_name, @RequestParam String user_last_name,
                           @RequestParam String login, @RequestParam String password, Model model){
        User user = new User(user_name, user_last_name, login, password);
        userRepo.save(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}")
    public String addUser(@PathVariable(value = "id") long id, Model model) {
        if (!userRepo.existsById(id)){
            return "redirect:/users";
        }

        Optional<User> user = userRepo.findById(id);
        ArrayList<User> list = new ArrayList<>();
        user.ifPresent(list::add);
        model.addAttribute("user", list);
        return "user-login-password";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable(value = "id") long id, Model model) {
        if (!userRepo.existsById(id)){
            return "redirect:/users";
        }

        Optional<User> user = userRepo.findById(id);
        ArrayList<User> list = new ArrayList<>();
        user.ifPresent(list::add);
        model.addAttribute("user", list);
        return "user-edit";
    }

    @PostMapping("/users/{id}/edit")
    public String userUpdate(@PathVariable(value = "id") long id, @RequestParam String user_name,
                             @RequestParam String user_last_name, @RequestParam String login,
                             @RequestParam String password, Model model){
        User user = userRepo.findById(id).orElseThrow();
        user.setFirstName(user_name);
        user.setLastName(user_last_name);
        user.setLogin(login);
        user.setPassword(password);
        userRepo.save(user);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/remove")
    public String userDelete(@PathVariable(value = "id") long id, Model model){
        User user = userRepo.findById(id).orElseThrow();
        userRepo.delete(user);
        return "redirect:/users";
    }


}
