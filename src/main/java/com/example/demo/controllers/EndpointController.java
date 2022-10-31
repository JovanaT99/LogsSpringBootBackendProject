package com.example.demo.controllers;

import com.example.demo.models.AppUser;
import com.example.demo.services.UserService;
import com.example.demo.services.LogService;
import com.example.demo.request.LogsRequest;
import com.example.demo.models.Logs;
import com.example.demo.request.RegistrationRequest;
import com.example.demo.services.RegistrationService;
import com.example.demo.models.ConfToken;
import com.example.demo.services.ConfTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/clients")
@AllArgsConstructor
public class EndpointController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ConfTokenService confTokenService;

    private RegistrationService registrationService;
    @Autowired
    private LogService logServic;
    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public List<Logs> getClients() {
        return logServic.getClients();
    }

    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            registrationService.register(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registered");
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestBody LogsRequest logsRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logsRequest.getAccount(), logsRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AppUser appUser = (AppUser) authentication.getPrincipal();
            String token = UUID.randomUUID().toString();
            ConfToken confToken = new ConfToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(20),
                    appUser
            );

            confTokenService.saveConfToken(confToken);
            //System.out.println("Bla bla" + appUser.getEmail());
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            return ResponseEntity.ok(map);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Username or password incorrect");
        }
    }

}
