package com.usbank.user.management.usermanager.security;

import com.usbank.user.management.usermanager.model.entity.User;
import com.usbank.user.management.usermanager.repository.UserRepository;
import com.usbank.user.management.usermanager.util.EncryptDecryptUtil;
import com.usbank.user.management.usermanager.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.hibernate.internal.util.StringHelper.isEmpty;

/**
 *
 *   OncePerRequestFilter is invoked per request.
 *
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();

        String username = jwtUtil.getUserNameFromToken(token);

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

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails == null ? List.of() : userDetails.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

}
