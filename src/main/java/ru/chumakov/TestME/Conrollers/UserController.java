package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.chumakov.TestME.models.Groupy;
import ru.chumakov.TestME.models.Role;
import ru.chumakov.TestME.models.User;
import ru.chumakov.TestME.repos.GroupyRepo;
import ru.chumakov.TestME.repos.UserRepo;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GroupyRepo groupyRepo;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String getUserList (Model model){
        model.addAttribute("users",userRepo.findAll());

        return "userList";
    }

    @GetMapping("/{id}/edit")
    public String getUserEdit(@AuthenticationPrincipal User you,
                           @PathVariable(value = "id") long id,
                           Model model) {
        if (!userRepo.existsById(id)){
            return "redirect:/";
        }
        if (you.getId()!=id && !you.getAuthorities().contains(Role.ADMIN)){
            return "redirect:/user/"+you.getId()+"/edit";
        }
        User user = userRepo.findById(id).orElseThrow();

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }


    @PostMapping("/{id}/edit")
    public String userUpdate(@PathVariable(value = "id") long id,
                             @AuthenticationPrincipal User you,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam Map<String,String> form,
                             Model model){
        User user = userRepo.findById(id).orElseThrow();
        User userFromDB = userRepo.findByUsername(username);
        if (userFromDB!=null && !username.equals(user.getUsername())){
            model.addAttribute("userError", "Пользователь c таким логином уже существует");
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "user-edit";
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        if (you.getAuthorities().contains(Role.ADMIN)) {

            user.getRoles().clear();

            for (String key : form.keySet()) {
                if (roles.contains(key)) {
                    user.getRoles().add(Role.valueOf(key));
                }
            }
        }

        userRepo.save(user);

        if (id == you.getId()){
            return "redirect:/user/profile";
        }
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getUserProfile(@AuthenticationPrincipal User you,
                              Model model){
        User user = userRepo.findById(you.getId()).orElseThrow();
        model.addAttribute("user", user);

        Set<Groupy> groups;
        if (user.getAuthorities().contains(Role.CURATOR)) groups = user.getOwnGroups();
        else groups = user.getInGroups();

        model.addAttribute("groups", groups);
        return "user-info";
    }

    @PostMapping("/profile")
    public String leftGroup(@AuthenticationPrincipal User user,
                              @RequestParam long id,
                              Model model){
        Groupy group = groupyRepo.findById(id).orElseThrow();

        user.getInGroups().remove(group);
        userRepo.save(user);
        return "redirect:/user/profile";
    }
}
