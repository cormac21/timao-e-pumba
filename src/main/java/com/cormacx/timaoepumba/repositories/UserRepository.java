package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    List<User> findAll();

    Optional<User> findByUsername();
}
