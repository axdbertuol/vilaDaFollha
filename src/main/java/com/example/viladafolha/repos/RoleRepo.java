package com.example.viladafolha.repos;

import com.example.viladafolha.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepo extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String role_admin);
}
