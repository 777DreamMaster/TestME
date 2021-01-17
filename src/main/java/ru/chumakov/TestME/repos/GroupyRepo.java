package ru.chumakov.TestME.repos;

import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.Groupy;
import ru.chumakov.TestME.models.User;

import java.util.Set;

public interface GroupyRepo extends CrudRepository<Groupy, Long> {
    Groupy findByCode(String code);
    Set<Groupy> findAllByOwner(User user);
}
