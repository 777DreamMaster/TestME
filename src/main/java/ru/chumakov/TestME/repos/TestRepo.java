package ru.chumakov.TestME.repos;

import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.Test;

public interface TestRepo extends CrudRepository<Test, Long> {
}
