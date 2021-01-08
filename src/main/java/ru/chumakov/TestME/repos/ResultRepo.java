package ru.chumakov.TestME.repos;

import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.Result;
import ru.chumakov.TestME.models.Testing;

import java.util.Set;

public interface ResultRepo extends CrudRepository<Result, Long> {
    Set<Result> findAllByTesting(Testing testing);
}
