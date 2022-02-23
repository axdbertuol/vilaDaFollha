package com.example.viladafolha.controllers.service;

import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.model.InhabitantRepo;
import com.example.viladafolha.model.UserSpringSecurity;
import com.example.viladafolha.model.transport.InhabitantDTO;
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
    private final InhabitantRepo inhabitantRepo;
    private final JavaMailSender mailSender;

    public UserService(InhabitantRepo inhabitantRepo, JavaMailSender mailSender) {
        this.inhabitantRepo = inhabitantRepo;
        this.mailSender = mailSender;
    }

    public InhabitantDTO getInhabitant(String email) {
        return inhabitantRepo.findByEmail(email).orElseThrow(RuntimeException::new).toDTO();
    }


    public InhabitantDTO getInhabitant(Long id) throws InhabitantNotFoundException {
        return inhabitantRepo.findById(id).orElseThrow(InhabitantNotFoundException::new).toDTO();
    }

    public List<InhabitantDTO> getInhabitantByName(String name) {
        return inhabitantRepo.findAllByName(name);
    }

    public List<InhabitantDTO> getInhabitantByBirthdayMonth(Integer month) {
        return inhabitantRepo.getAllByFilter(month).stream().map(Inhabitant::toDTO).toList();
    }

    public Optional<InhabitantDTO> getMostExpensiveInhabitant() {
        return inhabitantRepo.findAll().stream().map(Inhabitant::toDTO).max(Comparator.comparing(InhabitantDTO::getBalance));
    }

    public List<InhabitantDTO> getAllByThatAgeOrOlder(int age) {
        return inhabitantRepo.getAllByThatAgeOrOlder(age).stream().map(Inhabitant::toDTO).toList();
    }

    public Inhabitant createInhabitant(InhabitantDTO inhab) {
        if (getInhabitant(inhab.getEmail()) != null) {
            throw new RuntimeException("That email is already registered");
        }
        return (Inhabitant) inhabitantRepo.save(new Inhabitant(inhab));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = getInhabitant(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserSpringSecurity(user.getEmail(), user.getPassword(), user.getRoles());
    }


    public void removeInhabitant(Long id) throws InhabitantNotFoundException {
        inhabitantRepo.deleteById(id);
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

        inhabitantRepo.updatePassword(inhabitantDTO.getEmail(), inhabitantDTO.getPassword());


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
