package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.user.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Optional<Privilege> findByName(String name);

}
