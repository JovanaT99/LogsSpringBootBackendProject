package com.example.demo.repositories;

import com.example.demo.models.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public
interface LogRepository extends JpaRepository<Logs, Long> {

    @Query
            ("SELECT s FROM Logs s WHERE s.message LIKE ?1%")
    List<Logs> findByMessage(String message);

    @Query
            ("SELECT s FROM Logs s WHERE s.logType=?1")
    List<Logs>findByLogType(int logType);


    @Query
            ("SELECT d FROM Logs d WHERE d.createdAt>=?1")
    List<Logs>findByDate(LocalDateTime createdAt);

    @Query
            ("SELECT d FROM Logs d WHERE d.createdAt<=?1")
    List<Logs>findByDateTo(LocalDateTime createdAt);



}

