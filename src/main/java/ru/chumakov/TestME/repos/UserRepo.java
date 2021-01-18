package ru.chumakov.TestME.repos;

import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.Groupy;
import ru.chumakov.TestME.models.User;

import java.util.Set;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Set<User> findAllByInGroupsContainsOrderByFirstName(Groupy groupy);
}
