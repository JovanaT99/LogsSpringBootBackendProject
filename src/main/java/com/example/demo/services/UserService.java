package com.example.demo.services;

import com.example.demo.repositories.UserRepository;
import com.example.demo.models.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "user with email %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfTokenService confTokenService;

 @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }


    public UserDetails loadUserByEmail(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public void singUpUser(AppUser appUser) {
        boolean emailExist = userRepository.findByEmail(appUser.getEmail())
                .isPresent();
        boolean usernameExist = userRepository.findByUsername(appUser.getUsername())
               .isPresent();

        if (emailExist) {
            throw new EntityExistsException("email already exists");
        }
       if (usernameExist) {
           throw new EntityExistsException("username already exists");
       }
        String encodedPass = bCryptPasswordEncoder
                .encode(appUser.getPassword());
        appUser.setPassword(encodedPass);

        userRepository.save(appUser);

    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }



}
