package com.example.viladafolha.events;

import com.example.viladafolha.model.InhabitantRepo;
import com.example.viladafolha.model.Privilege;
import com.example.viladafolha.model.Role;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.repos.RoleRepo;
import com.example.viladafolha.repos.PrivilegeRepo;
import com.example.viladafolha.model.InhabitantRepo;
import com.example.viladafolha.repos.PrivilegeRepo;
import com.example.viladafolha.repos.RoleRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class SetupData implements ApplicationListener<ContextRefreshedEvent> {

    private Boolean alreadySetup = false;
    private InhabitantRepo inhabRepo;
    private RoleRepo roleRepo;
    private PrivilegeRepo privilegeRepo;
    private PasswordEncoder passwordEncoder;

    public SetupData(InhabitantRepo inhabRepo, RoleRepo roleRepo, PrivilegeRepo privilegeRepo, PasswordEncoder passwordEncoder) {
        this.inhabRepo = inhabRepo;
        this.roleRepo = roleRepo;
        this.privilegeRepo = privilegeRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {

        var privilege = privilegeRepo.findByName(name);
        if (privilege.isEmpty()) {
            return privilegeRepo.save(new Privilege(null, name));
        }
        return privilege.get();
    }

    @Transactional
    public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        var role = roleRepo.findByName(name);
        if (role.isEmpty()) {
            var newRole = new Role(null, name);
            newRole.setPrivileges(privileges);
            return roleRepo.save(newRole);
        }
        return role.get();
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup || inhabRepo.findByEmail("test@test.com").isEmpty()) {
            return;
        }

        var readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        var writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = new ArrayList<>();
        adminPrivileges.add(readPrivilege);
        adminPrivileges.add(writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);

        List<Privilege> userPrivileges = new ArrayList<>();
        userPrivileges.add(readPrivilege);
        createRoleIfNotFound("ROLE_USER", userPrivileges);

        var adminRole = roleRepo.findByName("ROLE_ADMIN").orElseThrow();
        Inhabitant inhabitant = new Inhabitant(
                "admin",
                "admin",
                "12312312312312",
                "admin@test.com",
                "S1LvEr@x",
                new Date(),
                1400.0,
                Set.of(adminRole));
        inhabRepo.save(inhabitant);
        alreadySetup = true;
    }
}