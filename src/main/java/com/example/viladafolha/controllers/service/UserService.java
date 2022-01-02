package com.example.viladafolha.controllers.service;

import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.model.InhabitantDao;
import com.example.viladafolha.model.UserSpringSecurity;
import com.example.viladafolha.model.transport.InhabitantDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final InhabitantDao inhabitantDao;

    public UserService(InhabitantDao inhabitantDao) {
        this.inhabitantDao = inhabitantDao;
    }

    public InhabitantDTO getInhabitant(String email) {
        Optional<InhabitantDTO> optional = inhabitantDao.getByEmail(email);
        return optional.orElse(null);
    }


    public InhabitantDTO getInhabitant(Long id) throws InhabitantNotFoundException {
        return inhabitantDao.getById(id).orElseThrow(InhabitantNotFoundException::new);
    }

    public List<InhabitantDTO> getInhabitantByName(String name) {
        return inhabitantDao.getAllByFilter(name);
    }

    public List<InhabitantDTO> getInhabitantByBirthdayMonth(Integer month) {
        return inhabitantDao.getAllByFilter(month);
    }

    public Optional<InhabitantDTO> getMostExpensiveInhabitant() {
        return inhabitantDao.getAll().stream().max(Comparator.comparing(InhabitantDTO::getBalance));
    }

    public List<InhabitantDTO> getAllByThatAgeOrOlder(int age) {
        return inhabitantDao.getAllByThatAgeOrOlder(age);
    }

    public Inhabitant createInhabitant(InhabitantDTO inhab) {
        return inhabitantDao.create(inhab).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = getInhabitant(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserSpringSecurity(user.getEmail(), user.getPassword(), user.getRoles());
    }


    public InhabitantDTO removeInhabitant(String email) {
        var inhabitantDTO = getInhabitant(email);
        if (inhabitantDTO == null) {
            throw new UsernameNotFoundException(email);
        }
        return inhabitantDao.remove(inhabitantDTO);
    }
}
