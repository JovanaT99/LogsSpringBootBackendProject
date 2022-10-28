package com.example.demo.appuser.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfTokenRepository
        extends JpaRepository<ConfToken, Long> {

    Optional<ConfToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query
    //(value = "UPDATE ConfToken c  SET c.comfirmedAt= ?2 WHERE c.token= ?1",nativeQuery = true)
   ("UPDATE ConfToken c  SET c.comfirmedAt= ?2 WHERE c.token= ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);


}
