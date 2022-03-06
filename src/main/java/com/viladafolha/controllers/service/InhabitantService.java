package com.viladafolha.controllers.service;

import com.viladafolha.exceptions.InhabitantNotFoundException;
import com.viladafolha.model.Inhabitant;
import com.viladafolha.model.transport.MailDTO;
import com.viladafolha.repos.InhabitantRepo;
import com.viladafolha.model.UserSpringSecurity;
import com.viladafolha.model.transport.InhabitantDTO;
import com.viladafolha.repos.RoleRepo;
import com.viladafolha.util.PasswordGenerator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class InhabitantService implements UserDetailsService {
    private final InhabitantRepo inhabitantRepo;
    private final PasswordEncoder encoder;
    private final RoleRepo roleRepo;
    private final EmailService emailService;


    public InhabitantService(InhabitantRepo inhabitantRepo, @Lazy PasswordEncoder encoder, RoleRepo roleRepo, EmailService emailService) {
        this.inhabitantRepo = inhabitantRepo;
        this.encoder = encoder;
        this.roleRepo = roleRepo;
        this.emailService = emailService;
    }

    public InhabitantDTO getInhabitant(String email) {
        Optional<Inhabitant> optional = inhabitantRepo.findByEmail(email);
        return optional.map(Inhabitant::toDTO).orElse(null);
    }


    public InhabitantDTO getInhabitant(Long id) throws InhabitantNotFoundException {
        return inhabitantRepo.findById(id).orElseThrow(InhabitantNotFoundException::new).toDTO();
    }

    public List<InhabitantDTO> getInhabitantByName(String name) {
        return inhabitantRepo.findAllByName(name).stream().map(Inhabitant::toDTO).toList();
    }

    public List<InhabitantDTO> getInhabitantByBirthdayMonth(String month) {
//        Date date = Date.from(Instant.from(LocalDate.of(2000, Integer.parseInt(month), 1)));
        return inhabitantRepo.findAllByBirthdayMonth(Integer.parseInt(month)).stream().map(Inhabitant::toDTO).toList();
    }

    public InhabitantDTO getMostExpensiveInhabitant() {
        var list = inhabitantRepo.findAll().stream().max(Comparator.comparing(Inhabitant::getBalance));
        return list.map(Inhabitant::toDTO).orElse(null);
    }

    public List<InhabitantDTO> getAllByThatAgeOrOlder(String age) {
        return inhabitantRepo.findAllByThatAgeOrOlder(Integer.parseInt(age)).stream().map(Inhabitant::toDTO).toList();
    }

    public InhabitantDTO createInhabitant(InhabitantDTO inhabDTO) {
        if (getInhabitant(inhabDTO.getEmail()) != null) {
            throw new RuntimeException("That email is already registered.");
        }

        if (!inhabDTO.getEmail().matches("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")) {
            throw new RuntimeException("Invalid email.");
        }
        if (!inhabDTO.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new RuntimeException(
                    """
                            Password must contain 8 characters:
                             at least 1 uppercase letter;
                             1 lowercase letter;
                             1 special character;
                             1 number""");
        }

        var userRole = roleRepo.findByName("ROLE_USER").orElseThrow();
        inhabDTO.setRoles(Set.of(userRole));
        inhabDTO.setPassword(encoder.encode(inhabDTO.getPassword()));

        Inhabitant inhabitant = new Inhabitant(inhabDTO);
        inhabitantRepo.save(inhabitant);
        return inhabitant.toDTO();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = getInhabitant(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserSpringSecurity(user.getEmail(), user.getPassword(), user.getRoles());
    }


    public void removeInhabitant(Long id) {
        if (inhabitantRepo.findById(id).isPresent()) {
            inhabitantRepo.deleteById(id);
        }
    }

    public Boolean sendNewPassword() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
             email = (String) authentication.getPrincipal();
        }
        assert email != null;

        InhabitantDTO inhabitantDTO = getInhabitant(email);
        String newPass = generatePassword();
        String encodePass = encoder.encode(newPass);
        inhabitantDTO.setPassword(encodePass);

        Document doc = new Document("src/main/resources/templates/Basic.html");
        doc.body().appendChild(new Element("p")).text("Your new password is " + newPass);


        MailDTO mail = new MailDTO();
        mail.setSubject("Password reset completed!");
        mail.setTo(inhabitantDTO.getEmail());
        mail.setFrom(emailService.getSourceEmail());
        mail.setText(doc.html());

        try {
            emailService.sendEmail(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        inhabitantRepo.updatePassword(inhabitantDTO.getEmail(), inhabitantDTO.getPassword());
        return true;
    }

    private String generatePassword() {
        return new String(PasswordGenerator.generatePassword(12));
    }


    public PasswordEncoder getEncoder() {
        return encoder;
    }
}
