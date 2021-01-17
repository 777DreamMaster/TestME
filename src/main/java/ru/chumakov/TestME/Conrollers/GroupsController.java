package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.chumakov.TestME.models.*;
import ru.chumakov.TestME.repos.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    @Autowired
    private GroupyRepo groupyRepo;
    @Autowired
    private TestRepo testRepo;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private AnswerRepo answerRepo;
    @Autowired
    private TestingRepo testingRepo;
    @Autowired
    private ResultRepo resultRepo;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String getGroups(@AuthenticationPrincipal User you,
                            Model model) {
        Iterable <Groupy> groups = groupyRepo.findAll();
        model.addAttribute("you", you);
        model.addAttribute("groups",groups);
        return "groups";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    @GetMapping("/add")
    public String getGroupAdd(Model model) {
        return "groups-add";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    @PostMapping("/add")
    public String groupPost(@AuthenticationPrincipal User user,
                            @RequestParam String name,
                            @RequestParam String code, Model model){
        Groupy groupFromDB = groupyRepo.findByCode(code);
        if (groupFromDB!=null){
            model.addAttribute("groupError", "Группа c таким кодом уже существует");
            return "groups-add";
        }

        Groupy groupy = new Groupy(name, code, user);
        groupyRepo.save(groupy);
        return "redirect:/groups/" + groupy.getId();

    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    @GetMapping("/{id}")
    public String getGroupInfo(@PathVariable(value = "id") Groupy group, Model model) {

        if (group==null){
            return "redirect:/";
        }

        model.addAttribute("group", group);
        model.addAttribute("users", group.getContainsUsers());
        return "groups-info";
    }

    @GetMapping("/join")
    public String getGroupJoin(Model model){
        Iterable<Groupy> groups = groupyRepo.findAll();
        model.addAttribute("groups",groups);
        return "join-group";
    }

    @PostMapping("/join")
    public String groupJoin(@AuthenticationPrincipal User user,
                            @RequestParam String code,
                            @RequestParam Long group,
                            Model model){
        Groupy groupy = groupyRepo.findByCode(code);
        Groupy groupBySelected = groupyRepo.findById(group).orElse(null);

        if (groupy == null || !groupy.equals(groupBySelected)){
            model.addAttribute("groupError","Неверный код группы");
            Iterable<Groupy> groups = groupyRepo.findAll();
            model.addAttribute("groups",groups);
            return "join-group";
        }
        groupy.getContainsUsers().add(user);
        user.getInGroups().add(groupy);
        groupyRepo.save(groupy);
        return "redirect:/test";
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    @GetMapping("/{id}/edit")
    public String getGroupEdit(@PathVariable(value = "id") long id,
                            Model model) {
        if (!groupyRepo.existsById(id)){
            return "redirect:/groups";
        }

        Groupy group = groupyRepo.findById(id).orElseThrow();

        model.addAttribute("group", group);
        return "group-edit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    @PostMapping("/{id}/edit")
    public String groupUpdate(@PathVariable(value = "id") long id,
                              @RequestParam String name,
                              @RequestParam String code,
                              Model model){
        Groupy group = groupyRepo.findById(id).orElseThrow();
        Groupy groupFromDB = groupyRepo.findByCode(code);

        if (groupFromDB!=null && !code.equals(group.getCode())){
            model.addAttribute("group", group);
            model.addAttribute("groupError", "Группа c таким кодом уже существует");
            return "group-edit";
        }

        group.setName(name);
        group.setCode(code);
        groupyRepo.save(group);
        return "redirect:/groups/" + id;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    @GetMapping("/{group}/results/{user}")
    public String getUserGroupResults(@PathVariable(value = "group") Groupy group,
                                        @PathVariable(value = "user") User user,
                                        Model model) {
        if (group == null || user == null){
            return "redirect:/";
        }
        List<Test> tests = testRepo.findAllByFromGroup(group);
        Set<Testing> testings = new HashSet<>();
        for (Test test : tests) {
            testings.addAll(testingRepo.findAllByTestAndUserOrderByPassDateDesc(test,user));
        }

        Map<Testing, String> TestingRes = new ControllerUtils().getResults(testings, resultRepo, questionRepo, answerRepo);

        model.addAttribute("group", group);
        model.addAttribute("user", user);
        model.addAttribute("tRes", TestingRes);
        model.addAttribute("format", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        model.addAttribute("testings", testings);
        return "userByGroup-results";
    }

}
