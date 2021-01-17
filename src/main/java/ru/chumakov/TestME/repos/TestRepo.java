package ru.chumakov.TestME.repos;

import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.Groupy;
import ru.chumakov.TestME.models.Test;

import java.util.List;

public interface TestRepo extends CrudRepository<Test, Long> {
    List<Test> findAllByFromGroup (Groupy groupy);
    List<Test> findAllByFromGroupOrderByCreationDateDesc (Groupy groupy);
}
