package com.sportman.repositories;

import com.sportman.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    public boolean existsByUsernameOrEmail(String username, String email);

    public boolean existsByUsername(String username);

    public Optional<User> findByUsername(String username);

    public boolean existsByEmail(String email);

}
