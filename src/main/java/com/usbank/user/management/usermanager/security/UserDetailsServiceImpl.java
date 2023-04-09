package com.usbank.user.management.usermanager.security;

import com.usbank.user.management.usermanager.model.entity.User;
import com.usbank.user.management.usermanager.repository.UserRepository;
import com.usbank.user.management.usermanager.util.EncryptDecryptUtil;
import com.usbank.user.management.usermanager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with the username does not exist"));

        List<SimpleGrantedAuthority> listRole = user.getRole()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .toList();

        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                username,
                new BCryptPasswordEncoder().encode(EncryptDecryptUtil.decrypt( user.getPassword())),
                listRole
        );
        return userDetails;
    }
}
