package com.example.demo.appuser.registration;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.UserService;
import com.example.demo.appuser.registration.token.ConfToken;
import com.example.demo.appuser.registration.token.ConfTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private EmailValidator emailValidator;

    private PasswordValidator passwordValidator;
    private final ConfTokenService tokenService;

    public void register(RegistrationRequest registrationRequest) {

        boolean isValidEmail = emailValidator.
                test(registrationRequest.getEmail());

        if (!isValidEmail) {
            throw new InvalidParameterException("email must be valid");
        }
        if (registrationRequest.getUsername().length() < 3) {
            throw new InvalidParameterException("username at least 3 characters");
        }

        boolean isPassValid = passwordValidator.test(registrationRequest.getPassword());

        if (!isPassValid) {
            throw new InvalidParameterException("password at least 8 characters and one letter and one number");
        }
        userService.singUpUser(
                new AppUser(
                        registrationRequest.getUsername(),
                        registrationRequest.getEmail(),
                        registrationRequest.getPassword(),
                        registrationRequest.getAppUserRole(),
                        false,
                        true
                )
        );
    }

    @Transactional
    public String confirmToken(String token) {
        ConfToken confToken = tokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confToken.getComfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confToken.getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {

            throw new IllegalStateException("token expired");

        }
        tokenService.setConfirmedAt(token);
        userService.enableAppUser(
                confToken.getAppUser().getEmail());
        return "confirmed";

    }
}
