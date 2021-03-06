package com.viladafolha.repos;

import com.viladafolha.model.Inhabitant;
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
    List<Inhabitant> findAllByName(String name);

    @Query(value = "SELECT * FROM inhabitants i WHERE date_part('month', i.birthday) = :month", nativeQuery = true)
    List<Inhabitant> findAllByBirthdayMonth(@Param("month") int month);

    @Query(value = "SELECT * FROM inhabitants i WHERE extract(year from AGE(i.birthday)) >= :age", nativeQuery = true)
    List<Inhabitant> findAllByThatAgeOrOlder(@Param("age") int age);

    @Modifying
    @Query("update Inhabitant inhab set inhab.password = :password where inhab.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);

}
