package com.usbank.user.management.usermanager.service;

import com.usbank.user.management.usermanager.model.Accounts;
import com.usbank.user.management.usermanager.model.entity.User;
import com.usbank.user.management.usermanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Accounts> getAccountsByUsername(String username){
        Optional<User> mayBeUsername = userRepository.findByUsername(username);

        if(mayBeUsername.isEmpty()){
            throw new UsernameNotFoundException("User does not exist");
        }

        User user = mayBeUsername.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "http://localhost:8095/api/v1/user/accounts/{userId}";

        Map<String, String> map = new HashMap<>();
        map.put("userId", user.getId().toString());

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Accounts[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
                Accounts[].class, map);

        Accounts[] accounts = responseEntity.getBody();

        return List.of(accounts);



    }
}
