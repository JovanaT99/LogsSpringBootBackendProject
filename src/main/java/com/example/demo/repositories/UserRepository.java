package com.example.demo.repositories;

import com.example.demo.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository //Nije neohodno ali je bolje za mene xD
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<AppUser,Long> {

   Optional<AppUser>findByEmail(String email);
    Optional<AppUser>findByUsername(String username);



    @Transactional
    @Modifying
    @Query
    ("UPDATE AppUser  a SET a.enabled=TRUE WHERE a.email=?1")
    int enableAppUser(String email);

   // @Query("SELECT u FROM AppUser u WHERE u.email = ?1")
   // public AppUser findByEmail(String email);

  // Optional<AppUser>findByUsername(String username);
}
