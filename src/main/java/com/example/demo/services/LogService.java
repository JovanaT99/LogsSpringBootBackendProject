package com.example.demo.services;


import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.LogRepository;
import com.example.demo.models.Logs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
import java.time.LocalDateTime;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Service
@AllArgsConstructor
@Getter
public class LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;


    public List<Logs> getClients() {
        return logRepository.findAll();
    }

    public void createLog(Logs logs)
            throws BadAttributeValueExpException, InvalidPropertiesFormatException {
        if (logs.getLogType() > 2 || logs.getLogType() < 0) {
            throw new BadAttributeValueExpException("Incorrect logType");
        }
        if (logs.getMessage().length() > 1024) {
            throw new InvalidPropertiesFormatException("Message should be less than 1024");
        }
        logs.setCreatedAt(LocalDateTime.now());
        logRepository.save(logs);

    }


}
