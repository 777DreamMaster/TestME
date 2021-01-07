package ru.chumakov.TestME.repos;

import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.Question;
import ru.chumakov.TestME.models.Test;

import java.util.List;

public interface QuestionRepo extends CrudRepository<Question, Long> {
    List<Question> findAllByTest(Test test);
}
