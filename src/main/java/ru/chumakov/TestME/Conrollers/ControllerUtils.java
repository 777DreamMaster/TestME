package ru.chumakov.TestME.Conrollers;

import ru.chumakov.TestME.models.Answer;
import ru.chumakov.TestME.models.Question;
import ru.chumakov.TestME.models.Result;
import ru.chumakov.TestME.models.Testing;
import ru.chumakov.TestME.repos.*;

import java.util.*;


public class ControllerUtils {

    public Map<Testing, String> getResults(Set<Testing> testings, ResultRepo resultRepo, QuestionRepo questionRepo, AnswerRepo answerRepo) {
        HashMap<Testing, String> TestingRes = new HashMap<>();
        for (Testing testing : testings) {

            long correctCountUser = 0;
            long correctCountTest = questionRepo.countByTest(testing.getTest());

            Set<Result> resultsSet = resultRepo.findAllByTesting(testing);

            HashSet<Answer> QA = new HashSet<>();
            for (Result result : resultsSet) {
                QA.add(result.getAnswer());
            }
            boolean f;
            ArrayList<Question> questions = new ArrayList<>(questionRepo.findAllByTest(testing.getTest()));
            for (Question question : questions) {
                f = true;
                for (Answer answer : answerRepo.findAllByQuestion(question)) {
                    if ((answer.isCorrect() && !QA.contains(answer)) || (!answer.isCorrect() && QA.contains(answer))) {
                        f = false;
                        break;
                    }
                }
                if (f)
                    correctCountUser++;
            }

            TestingRes.put(testing, correctCountUser + "/" + correctCountTest);
        }
        return TestingRes;
    }
}
