package com.example.viladafolha.model;

import com.example.viladafolha.model.transport.InhabitantDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface InhabitantRepo extends JpaRepository<Inhabitant, Long> {

    Optional<Inhabitant> findByEmail(String email);

    @Query(value = "SELECT id, name, birthday FROM inhabitants WHERE date_part('month', birthday)= :month", nativeQuery = true)
    List<Inhabitant> getAllByFilter(@Param("month") Integer month);
    @Query(value = "SELECT id, name, birthday FROM inhabitants WHERE extract(year from AGE(birthday)) >= :age", nativeQuery = true)
    List<Inhabitant> getAllByThatAgeOrOlder(@Param("age") int age);
    @Modifying
    @Query("update Inhabitant inhab set inhab.password = :password where inhab.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);

    List<InhabitantDTO> findAllByName(String name);
}
