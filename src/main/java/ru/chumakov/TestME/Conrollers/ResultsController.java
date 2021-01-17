package ru.chumakov.TestME.Conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.chumakov.TestME.models.*;
import ru.chumakov.TestME.repos.AnswerRepo;
import ru.chumakov.TestME.repos.QuestionRepo;
import ru.chumakov.TestME.repos.ResultRepo;
import ru.chumakov.TestME.repos.TestingRepo;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/results")
public class ResultsController {

    @Autowired
    TestingRepo testingRepo;
    @Autowired
    QuestionRepo questionRepo;
    @Autowired
    AnswerRepo answerRepo;
    @Autowired
    ResultRepo resultRepo;


    @GetMapping
    public String getResults(@AuthenticationPrincipal User user,
                             Model model){
        Iterable <Testing> testings = testingRepo.findAllByUserOrderByPassDateDesc(user);
        model.addAttribute("results", testings);
        model.addAttribute("format", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return "results";
    }

    @GetMapping("/{id}")
    public String getCurrentResult(@PathVariable long id,
                                   Model model){
        Testing testing = testingRepo.findById(id).orElseThrow();
        Test test = testing.getTest();

        Set<Result> resultsSet = resultRepo.findAllByTesting(testing);
        Set<Answer> myAnswers = new HashSet<>();
        for (Result result: resultsSet) {
            myAnswers.add(result.getAnswer());
        }

        ArrayList<Question> questions = new ArrayList<>(questionRepo.findAllByTest(test));
        Map<Question,ArrayList<Answer>> questionWithAnswers= new HashMap<>();

        for (int i = 0; i < questions.size() ; i++) {
            questionWithAnswers.put(questions.get(i),new ArrayList<>(answerRepo.findAllByQuestion(questions.get(i))));
        }

        Set <Testing> testings= new HashSet<>();
        testings.add(testing);
        Map<Testing, String> TestingRes = new ControllerUtils().getResults(testings,resultRepo,questionRepo,answerRepo);

        model.addAttribute("testing", testing);
        model.addAttribute("test", test);
        model.addAttribute("myAnswers", myAnswers);
        model.addAttribute("QA",questionWithAnswers);
        model.addAttribute("format", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        model.addAttribute("tRes", TestingRes.get(testing));

        return "current-result";
    }
}
