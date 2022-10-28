package com.example.demo.appuser.registration;

import com.example.demo.appuser.login.LogRepo;
import com.example.demo.appuser.login.LogServic;
import com.example.demo.appuser.models.Logs;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.BadAttributeValueExpException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@RestController
@RequestMapping(path = "api/logs")
@AllArgsConstructor
public class LogsController {

    private LogServic logService;
    @Autowired
    LogRepo logRepo;


    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody Logs logs) {
        try {
            logService.createLog(logs);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (BadAttributeValueExpException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidPropertiesFormatException e) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(e.getMessage());
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?> getLogs(){
         return ResponseEntity.ok().body(logService.getLogs());
    }

    @GetMapping("/search/message")
    public ResponseEntity<List<Logs>> getLogsByMessage(@RequestParam String message) {
        return new ResponseEntity<>(logRepo.findByMessage(message), HttpStatus.OK);
    }

    @GetMapping("/search/logType")
    public ResponseEntity<?> getLogsByMessage(@RequestParam int logType) {
        if (logType > 2 || logType < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid LogType");
        }
        return new ResponseEntity<>(logRepo.findByLogType(logType), HttpStatus.OK);

    }




}
