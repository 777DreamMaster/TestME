package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.chumakov.TestME.models.*;
import ru.chumakov.TestME.repos.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @Autowired
    private TestingRepo testingRepo;
    @Autowired
    private ResultRepo resultRepo;

    @GetMapping
    public String getTests(@AuthenticationPrincipal User user,
                           Model model){
        Map<Groupy, ArrayList<Test>> GT = new HashMap<>();
        Set<Groupy> groups;
        if (!user.getAuthorities().contains(Role.ADMIN)){

            if (user.getAuthorities().contains(Role.CURATOR)) groups = user.getOwnGroups();
            else groups = user.getInGroups();

            for (Groupy group : groups) {
                GT.put(group, new ArrayList<>(testRepo.findAllByFromGroup(group)));
            }
            model.addAttribute("GT", GT);
        }

        Iterable <Test> testsAll = testRepo.findAllByFromGroup(null);
        model.addAttribute("tests",testsAll);

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

        return "redirect:/test";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CURATOR')")
    @GetMapping("/{id}")
    public String testGetInfo(@PathVariable(value = "id") Test test,
                              Model model) {
        if (test==null){
            return "redirect:/test";
        }
        ArrayList<Test> tests = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>(questionRepo.findAllByTest(test));

        Map<Question,ArrayList<Answer>> questionWithAnswers= new HashMap<>();
        for (int i = 0; i <questions.size() ; i++) {
            questionWithAnswers.put(questions.get(i),new ArrayList<>(answerRepo.findAllByQuestion(questions.get(i))));
        }
        tests.add(test);
        model.addAttribute("test", tests);
        model.addAttribute("QA",questionWithAnswers);
        return "test-info";
    }

    @GetMapping("/{id}/pass")
    public String passingTest(@PathVariable(value = "id") Test test,
                              Model model) {
        if (test==null){
            return "redirect:/test";
        }
        Long count = questionRepo.countByTest(test) ;

        ArrayList<Question> questions = new ArrayList<>(questionRepo.findAllByTest(test));
        Map<Question,ArrayList<Answer>> questionWithAnswers= new HashMap<>();
        for (int i = 0; i <questions.size() ; i++) {
            questionWithAnswers.put(questions.get(i),new ArrayList<>(answerRepo.findAllByQuestion(questions.get(i))));
        }

        model.addAttribute("count", count );
        model.addAttribute("test", test);
        model.addAttribute("QA",questionWithAnswers);

        return "test-passing";
    }

    @PostMapping("/{id}/pass")
    public String sendTestResults(@PathVariable(value = "id") Test test,
                                  @AuthenticationPrincipal User user,
                                  @RequestParam Map<String,String> form,
                                  Model model) {
        Testing testing = new Testing(LocalDateTime.now(),test,user);

        testingRepo.save(testing);
        List<Result> resultList = new ArrayList<>();
        Result result;
        for (int i = 1; i <= questionRepo.countByTest(test) ; i++) {

            long qID = Long.parseLong(form.get("q" + i));
            Question question = questionRepo.findById(qID).orElseThrow();
            for (int j = 1; j <=4 ; j++) {
                String paramName = "ans" + j + "q" + i;
                String paramNameCor = "ans" + j + "cor" + i;
                if (form.containsKey(paramNameCor)) {
                    long aID = Long.parseLong(form.get(paramName));

                    result = new Result(
                            new ResultKey(
                                        testing,
                                        question,
                                        answerRepo.findById(aID).orElseThrow()),
                                    testing,
                                    question,answerRepo.findById(aID).orElseThrow());

                    resultList.add(result);
                }

            }
        }

        resultRepo.saveAll(resultList);

        return "redirect:/results/" + testing.getId();
    }

}
