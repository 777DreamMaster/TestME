package ru.chumakov.TestME.repos;

import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.Answer;
import ru.chumakov.TestME.models.Testing;

public interface TestingRepo extends CrudRepository<Testing, Long> {
}
