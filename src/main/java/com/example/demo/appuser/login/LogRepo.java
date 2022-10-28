package com.example.demo.appuser.login;

import com.example.demo.appuser.models.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public
interface LogRepo extends JpaRepository<Logs, Long> {

    @Query
            ("SELECT s FROM Logs s WHERE s.message LIKE ?1%")
    List<Logs> findByMessage(String message);

    @Query
            ("SELECT s FROM Logs s WHERE s.logType=?1")
    List<Logs>findByLogType(int logType);

}

