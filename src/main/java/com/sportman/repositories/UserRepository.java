package com.sportman.repositories;

import com.sportman.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    public boolean existsByUsernameOrEmail(String username, String email);

}
