package com.example.demo.appuser.registration.token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfTokenService {

    private final ConfTokenRepository confTokenRepository;

    public void saveConfToken(ConfToken confToken){
        confTokenRepository.save(confToken);
    }

    public Optional<ConfToken>getToken(String token){
        return confTokenRepository.findByToken(token);

    }
    public int setConfirmedAt(String token){
        return confTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());

    }
}
