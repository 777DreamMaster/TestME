package ru.chumakov.TestME.repos;

import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.Answer;
import ru.chumakov.TestME.models.Testing;
import ru.chumakov.TestME.models.User;

import java.util.List;

public interface TestingRepo extends CrudRepository<Testing, Long> {
    List<Testing> findAllByUser(User user);
    List<Testing> findAllByUserOrderByPassDateDesc(User user);
}
