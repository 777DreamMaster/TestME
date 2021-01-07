package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.chumakov.TestME.models.Answer;
import ru.chumakov.TestME.models.Groupy;
import ru.chumakov.TestME.models.Question;
import ru.chumakov.TestME.models.Test;
import ru.chumakov.TestME.repos.AnswerRepo;
import ru.chumakov.TestME.repos.GroupyRepo;
import ru.chumakov.TestME.repos.QuestionRepo;
import ru.chumakov.TestME.repos.TestRepo;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private GroupyRepo groupyRepo;
    @Autowired
    private TestRepo testRepo;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private AnswerRepo answerRepo;

    @GetMapping
    public String getTests(Model model){
        Iterable <Test> tests = testRepo.findAll();
        model.addAttribute("tests",tests);
        model.addAttribute("format", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return "tests";
    }

    @GetMapping("/creating")
    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    public String getTestCreating(Model model){
        return "test-creating";
    }

    @PostMapping("/creating")
    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    public String countOfQuestions(@RequestParam int count,
                          Model model){
        model.addAttribute("count",count);
        return "test-creating-add";
    }

    @PostMapping("/creating/add")
    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    public String addTest(@RequestParam Map<String,String> form,
                          @RequestParam String name,
                          @RequestParam String group,
                          @RequestParam int count,
                          Model model){
        LocalDateTime date = LocalDateTime.now();
        Test test;
        if (group.isBlank()){
            test = new Test(name, date);
        } else {
            test = new Test(name, date, groupyRepo.findByCode(group));
        }
        ArrayList <Question> questions = new ArrayList<>();
        ArrayList <Answer> answers = new ArrayList<>();
        for (int i = 1; i <= count ; i++) {
            Question question = new Question(form.get(String.valueOf(i)),
                                                test);
            questions.add(question);

            for (int j = 1; j <=4 ; j++) {
                String paramName = "ans" + j + "q" + i;
                String paramNameCor = "ans" + j + "cor" + i;
                Answer answer = new Answer(form.get(paramName),
                                    form.containsKey(paramNameCor),
                                            question);
                answers.add(answer);
            }
        }

        testRepo.save(test);

        questionRepo.saveAll(questions);

        answerRepo.saveAll(answers);

        //model.addAttribute("all",form);
        //return "test-123";
        return "redirect:/test";
    }

    @GetMapping("/{id}")
    public String testGetInfo(@PathVariable(value = "id") Test test, Model model) {
//        if (!groupyRepo.existsById(id)){
//            return "redirect:/groups";
//        }
//
//        Optional<Groupy> group = groupyRepo.findById(id);
        if (test==null){
            return "redirect:/test";
        }
        ArrayList<Test> tests = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>(questionRepo.findAllByTest(test));
        //ArrayList<Answer> answers = new ArrayList<>(answerRepo.findAllByQuestion());

        Map<Question,ArrayList<Answer>> questionWithAnswers= new HashMap<>();
        for (int i = 0; i <questions.size() ; i++) {
            questionWithAnswers.put(questions.get(i),new ArrayList<>(answerRepo.findAllByQuestion(questions.get(i))));
        }
        tests.add(test);
        model.addAttribute("test", tests);
        model.addAttribute("QA",questionWithAnswers);
        return "test-info";
    }

}
