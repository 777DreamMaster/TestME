package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.chumakov.TestME.models.Groupy;
import ru.chumakov.TestME.models.User;
import ru.chumakov.TestME.repos.GroupyRepo;

import java.util.ArrayList;
import java.util.Optional;


@Controller
public class GroupsController {

    @Autowired
    private GroupyRepo groupyRepo;

    @GetMapping("/groups")
    public String getGroups(Model model) {
        Iterable <Groupy> groups = groupyRepo.findAll();
        model.addAttribute("groups",groups);
        return "groups";
    }

    @GetMapping("/groups/add")
    public String addGroup(Model model) {
        return "groups-add";
    }

    @PostMapping("/groups/add")
    public String groupPost(@AuthenticationPrincipal User user,
                            @RequestParam String name,
                            @RequestParam String code, Model model){
        Groupy groupy = new Groupy(name, code, user);
        groupyRepo.save(groupy);
        return "redirect:/groups";
    }

    @GetMapping("/groups/{id}")
    public String addGroup(@PathVariable(value = "id") long id, Model model) {
        if (!groupyRepo.existsById(id)){
            return "redirect:/groups";
        }

        Optional<Groupy> group = groupyRepo.findById(id);
        ArrayList<Groupy> list = new ArrayList<>();
        group.ifPresent(list::add);
        model.addAttribute("group", list);
        return "groups-name-code";
    }

    @GetMapping("/groups/{id}/edit")
    public String editGroup(@PathVariable(value = "id") long id, Model model) {
        if (!groupyRepo.existsById(id)){
            return "redirect:/groups";
        }

        Optional<Groupy> group = groupyRepo.findById(id);
        ArrayList<Groupy> list = new ArrayList<>();
        group.ifPresent(list::add);
        model.addAttribute("group", list);
        return "group-edit";
    }

    @PostMapping("/groups/{id}/edit")
    public String groupUpdate(@PathVariable(value = "id") long id, @RequestParam String name,
                              @RequestParam String code, Model model){
        Groupy groupy = groupyRepo.findById(id).orElseThrow();
        groupy.setName(name);
        groupy.setCode(code);
        groupyRepo.save(groupy);
        return "redirect:/groups";
    }

    @PostMapping("/groups/{id}/remove")
    public String groupDelete(@PathVariable(value = "id") long id, Model model){
        Groupy groupy = groupyRepo.findById(id).orElseThrow();
        groupyRepo.delete(groupy);
        return "redirect:/groups";
    }

}
