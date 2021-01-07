package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.chumakov.TestME.models.Groupy;
import ru.chumakov.TestME.models.User;
import ru.chumakov.TestME.repos.GroupyRepo;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/groups")
@PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
public class GroupsController {

    @Autowired
    private GroupyRepo groupyRepo;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String getGroups(Model model) {
        Iterable <Groupy> groups = groupyRepo.findAll();
        model.addAttribute("groups",groups);
        return "groups";
    }

    @GetMapping("/add")
    public String groupAdd(Model model) {
        return "groups-add";
    }

    @PostMapping("/add")
    public String groupPost(@AuthenticationPrincipal User user,
                            @RequestParam String name,
                            @RequestParam String code, Model model){
        Groupy groupy = new Groupy(name, code, user);
        groupyRepo.save(groupy);
        return "redirect:/groups";
    }

    @GetMapping("/{id}")
    public String groupGetInfo(@PathVariable(value = "id") Groupy group, Model model) {
//        if (!groupyRepo.existsById(id)){
//            return "redirect:/groups";
//        }
//
//        Optional<Groupy> group = groupyRepo.findById(id);
        if (group==null){
            return "redirect:/groups";
        }
        ArrayList<Groupy> list = new ArrayList<>();
        //group.ifPresent(list::add);
        list.add(group);
        model.addAttribute("group", list);
        return "groups-info";
    }

    @GetMapping("/{id}/edit")
    public String groupEdit(@PathVariable(value = "id") long id, Model model) {
        if (!groupyRepo.existsById(id)){
            return "redirect:/groups";
        }

        Optional<Groupy> group = groupyRepo.findById(id);
        ArrayList<Groupy> list = new ArrayList<>();
        group.ifPresent(list::add);
        model.addAttribute("group", list);
        return "group-edit";
    }

    @PostMapping("/{id}/edit")
    public String groupUpdate(@PathVariable(value = "id") long id, @RequestParam String name,
                              @RequestParam String code, Model model){
        Groupy groupy = groupyRepo.findById(id).orElseThrow();
        groupy.setName(name);
        groupy.setCode(code);
        groupyRepo.save(groupy);
        return "redirect:/groups";
    }

    @PostMapping("/{id}/remove")
    public String groupDelete(@PathVariable(value = "id") long id, Model model){
        Groupy groupy = groupyRepo.findById(id).orElseThrow();
        groupyRepo.delete(groupy);
        return "redirect:/groups";
    }

}
