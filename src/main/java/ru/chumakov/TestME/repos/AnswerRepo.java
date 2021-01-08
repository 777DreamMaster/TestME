package ru.chumakov.TestME.repos;

import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.Answer;
import ru.chumakov.TestME.models.Groupy;
import ru.chumakov.TestME.models.Question;

import java.util.List;

public interface AnswerRepo extends CrudRepository<Answer, Long> {
    List<Answer> findAllByQuestion(Question question);
    Long countByQuestionAndCorrect(Question question, boolean correct);
}
