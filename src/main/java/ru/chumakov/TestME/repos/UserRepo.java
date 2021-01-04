package ru.chumakov.TestME.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.chumakov.TestME.models.User;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
