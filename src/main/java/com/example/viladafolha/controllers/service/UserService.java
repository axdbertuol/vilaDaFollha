package com.example.viladafolha.controllers.service;

import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.model.InhabitantDao;
import com.example.viladafolha.model.UserSpringSecurity;
import com.example.viladafolha.model.transport.InhabitantDTO;
import com.mchange.net.MailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {
    private final InhabitantDao inhabitantDao;
    private final JavaMailSender mailSender;

    public UserService(InhabitantDao inhabitantDao, JavaMailSender mailSender) {
        this.inhabitantDao = inhabitantDao;
        this.mailSender = mailSender;
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
        if (getInhabitant(inhab.getEmail()) != null) {
            throw new RuntimeException("That email is already registered");
        }
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


    public InhabitantDTO removeInhabitant(Long id) throws InhabitantNotFoundException {
        var inhabitantDTO = getInhabitant(id);
        return inhabitantDao.remove(inhabitantDTO);
    }

    public Boolean sendNewPassword(String email) {
        var inhabitantDTO = getInhabitant(email);
        if (inhabitantDTO == null) {
            throw new UsernameNotFoundException(email);
        }
        String newPass = generatePassword();
        String encodePass = new BCryptPasswordEncoder().encode(newPass);
        inhabitantDTO.setPassword(encodePass);


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(inhabitantDTO.getEmail());
        mailMessage.setSubject("Complete Password Reset!");
        mailMessage.setFrom("viladafolhax@gmail.com");
        mailMessage.setText("Your new password is " + newPass);

        try {
            mailSender.send(mailMessage);

        } catch (MailException e) {
            throw new RuntimeException(e.getMessage());
        }

        inhabitantDao.updatePassword(inhabitantDTO);


        return true;
    }

    private String generatePassword() {
        return new String(generatePassword(12));
    }


    private char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }
}
