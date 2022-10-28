package com.example.demo.appuser.login;


import com.example.demo.appuser.models.Logs;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
import java.time.LocalDateTime;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Service
public class LogServic {

    private final LogRepo logRepo;

    public LogServic(LogRepo logRepo) {
        this.logRepo = logRepo;
    }

    public List<Logs>getLogs() {
        return logRepo.findAll();
    }


    public void createLog(Logs logs)
            throws BadAttributeValueExpException, InvalidPropertiesFormatException {
        if (logs.getLogType() > 2 || logs.getLogType() < 0) {
            throw new BadAttributeValueExpException("Incorrect logType");
        }
        if(logs.getMessage().length()>1024) {
            throw new InvalidPropertiesFormatException("Message should be less than 1024");
        }
        logs.setCreatedAt(LocalDateTime.now());
        logRepo.save(logs);

    }


}
