package com.example.viladafolha.events;

import com.example.viladafolha.repos.InhabitantRepo;
import com.example.viladafolha.model.Privilege;
import com.example.viladafolha.model.Role;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.repos.RoleRepo;
import com.example.viladafolha.repos.PrivilegeRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Component
public class SetupData implements ApplicationListener<ContextRefreshedEvent> {

    private Boolean alreadySetup = false;
    private final InhabitantRepo inhabRepo;
    private final RoleRepo roleRepo;
    private final PrivilegeRepo privilegeRepo;
    private final PasswordEncoder encoder;


    public SetupData(InhabitantRepo inhabRepo,
                     RoleRepo roleRepo,
                     PrivilegeRepo privilegeRepo,
                     @Lazy PasswordEncoder encoder) {
        this.inhabRepo = inhabRepo;
        this.roleRepo = roleRepo;
        this.privilegeRepo = privilegeRepo;
        this.encoder = encoder;
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

    private void createUserInhabitants() {
        var userRole = roleRepo.findByName("ROLE_USER").orElseThrow();
        Inhabitant inhabitant = new Inhabitant(
                "alex",
                "trom",
                "12312312412312",
                "alex@test.com",
                encoder.encode("S1Lver@x"),
                LocalDate.of(1992, 4, 1),
                9999.0,
                Set.of(userRole)
        );
        inhabRepo.save(inhabitant);
        inhabitant = new Inhabitant(
                "john",
                "doe",
                "12112312412312",
                "jdoe@test.com",
                encoder.encode("S1Lver@x"),
                LocalDate.of(1999, 4, 2),
                1400.0,
                Set.of(userRole)
        );
        inhabRepo.save(inhabitant);
        inhabitant = new Inhabitant(
                "herr",
                "dior",
                "12412312412312",
                "herr@test.com",
                encoder.encode("S1Lver@x"),
                LocalDate.of(1999, 2, 3),
                200.0,
                Set.of(userRole)
        );
        inhabRepo.save(inhabitant);
        inhabitant = new Inhabitant(
                "brol",
                "potin",
                "12312512412312",
                "potin@test.com",
                encoder.encode("S1Lver@x"),
                LocalDate.of(1991, 8, 4),
                2200.0,
                Set.of(userRole)
        );
        inhabRepo.save(inhabitant);
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
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
                encoder.encode("S1Lver@x"),
                LocalDate.of(2000, 8, 1),
                100000.0,
                Set.of(adminRole)
        );
        inhabRepo.save(inhabitant);
        createUserInhabitants();

        alreadySetup = true;
    }
}