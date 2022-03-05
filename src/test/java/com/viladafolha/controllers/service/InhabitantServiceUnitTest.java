package com.viladafolha.controllers.service;

import com.viladafolha.model.Inhabitant;
import com.viladafolha.model.Role;
import com.viladafolha.model.transport.InhabitantDTO;
import com.viladafolha.repos.InhabitantRepo;
import com.viladafolha.repos.RoleRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InhabitantServiceUnitTest {

    @InjectMocks
    InhabitantService inhabitantService;
    @Mock
    InhabitantRepo inhabitantRepo;
    @Mock
    RoleRepo roleRepo;
    @Mock
    JavaMailSender mailSender;
    @Mock
    BCryptPasswordEncoder passwordEncoder;

    InhabitantDTO inhabitantDTO;
    Role userRole;

    @BeforeEach
    void init() {
        Mockito.reset(inhabitantRepo);
        inhabitantDTO = new InhabitantDTO();
        inhabitantDTO.setName("Test");
        inhabitantDTO.setLastName("Mate");
        inhabitantDTO.setEmail("test@test.com");
        inhabitantDTO.setPassword("T@test4n0");
    }

    @Test
    void givenValidInhabitant_whenSave_thenSucceed() {
        // GIVEN
        final Inhabitant[] inhabitant = new Inhabitant[1];

        when(inhabitantRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(inhabitantRepo.save(any(Inhabitant.class))).then(new Answer<Inhabitant>() {
            long sequence = 1;

            @Override
            public Inhabitant answer(InvocationOnMock invocationOnMock) throws Throwable {
                inhabitant[0] = invocationOnMock.getArgument(0);
                inhabitant[0].setId(sequence++);
                return inhabitant[0];
            }
        });
        var userRole = mock(Role.class);
        userRole.setName("ROLE_USER");
        when(roleRepo.findByName(anyString())).thenReturn(Optional.of(userRole));

        // WHEN
        inhabitantService.createInhabitant(inhabitantDTO);

        // THEN
        verify(inhabitantRepo).save(inhabitant[0]);
        Assertions.assertNotNull(inhabitant[0].getId());
        Assertions.assertTrue(inhabitant[0].getRoles().contains(userRole));
    }

    @Test
    void givenValidInhabitantID_whenRemove_thenSucceed() {
        // GIVEN
        when(inhabitantRepo.findById(1L)).thenReturn(Optional.of(new Inhabitant()));
        // WHEN
        inhabitantService.removeInhabitant(1L);
        // THEN
        verify(inhabitantRepo).deleteById(1L);
    }

    @Test
    void givenInhabitantEmail_whenFindByEmail_thenSucceed() {
        // GIVEN

        when(inhabitantRepo.findByEmail(anyString())).thenReturn(Optional.of(new Inhabitant()));

        // WHEN
        inhabitantService.getInhabitant("test@test.com");

        // THEN
        verify(inhabitantRepo).findByEmail("test@test.com");
    }

    @Test
    void givenInhabitantsWithBalance_whenFindMostExpensiveInhabitant_thenSucceed() {
        // GIVEN

        var inhab1 = new Inhabitant();
        inhab1.setBalance(103.0);
        var inhab2 = new Inhabitant();
        inhab2.setBalance(102.0);
        var inhab3 = new Inhabitant();
        inhab3.setBalance(101.0);


        when(inhabitantRepo.findAll()).thenReturn(List.of(inhab1, inhab2, inhab3));

        // THEN
        var result = inhabitantService.getMostExpensiveInhabitant();

        // THEN
        verify(inhabitantRepo).findAll();
        assert result.getBalance() == 103.0;
    }


}