package com.example.viladafolha.repos;

import com.example.viladafolha.model.Inhabitant;
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
    List<InhabitantDTO> findAllByName(String name);

    @Query(value = "SELECT i.id, i.name, i.birthday FROM Inhabitant i WHERE function('month', i.birthday ) = :month")
    List<Inhabitant> findAllByBirthdayMonth(@Param("month") int month);

    @Query(value = "SELECT id, name, birthday FROM inhabitants WHERE extract(year from AGE(birthday)) >= :age", nativeQuery = true)
    List<Inhabitant> findAllByThatAgeOrOlder(@Param("age") int age);

//    List<Inhabitant> findAllByBirthday_Month(Date month);

    @Modifying
    @Query("update Inhabitant inhab set inhab.password = :password where inhab.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);

}
