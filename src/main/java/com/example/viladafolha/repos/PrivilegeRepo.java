package com.example.viladafolha.repos;

import com.example.viladafolha.model.Privilege;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PrivilegeRepo extends CrudRepository<Privilege, Long> {
    Optional<Privilege> findByName(String name);
}
