package com.atixlabs.semillasmiddleware.security.service;

import com.atixlabs.semillasmiddleware.security.configuration.CustomUser;
import com.atixlabs.semillasmiddleware.security.repository.UserRepository;
import com.atixlabs.semillasmiddleware.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> opUser = userRepository.findByUsername(username);

        if (opUser.isPresent()) {
            User user =  opUser.get();
            return CustomUser.create(user);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

