package com.usbank.user.management.usermanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usbank.user.management.usermanager.exception.RoleNotFoundException;
import com.usbank.user.management.usermanager.exception.UserAlreadyExistException;
import com.usbank.user.management.usermanager.model.UserRole;
import com.usbank.user.management.usermanager.model.request.UserLoginRequest;
import com.usbank.user.management.usermanager.model.request.UserSignupRequest;
import com.usbank.user.management.usermanager.model.entity.Address;
import com.usbank.user.management.usermanager.model.entity.Role;
import com.usbank.user.management.usermanager.model.entity.User;
import com.usbank.user.management.usermanager.model.response.UserLoginResponse;
import com.usbank.user.management.usermanager.model.response.UserRegistrationResponse;
import com.usbank.user.management.usermanager.repository.AddressRepository;
import com.usbank.user.management.usermanager.repository.RoleRepository;
import com.usbank.user.management.usermanager.util.EncryptDecryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.usbank.user.management.usermanager.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserAuthnOperationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;


    public Optional<User> getUserByUserName(String username){
        return userRepository.findByUsername(username);
    }


    /**
     *  First check if same username, email and phone number is present in Db then throw 409 conflict
     *  with the error UserAlreadyExistException
     *
     *
     *
     */
    public UserRegistrationResponse registerUser(UserSignupRequest userSignupRequest) throws JsonProcessingException {

        validateUserSignUpRequest(userSignupRequest);

        User user = new User();
        Set<Role> roleSet = new HashSet<>();

        Set<String> requestRoles = userSignupRequest.getRole();

        requestRoles.forEach(strRole -> {
          roleSet.add( roleRepository.findByName(UserRole.valueOf(strRole)).get());
        });

        String encryptedPassword = EncryptDecryptUtil.encrypt(userSignupRequest.getPassword());

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userSignupRequest.getAddress());
        Address addressEntity = mapper.readValue(jsonString, Address.class);

        user.setAddress(addressEntity);
        user.setRole(roleSet);
        user.setEmail(userSignupRequest.getEmail());
        user.setPassword(encryptedPassword);
        user.setPhoneNumber(userSignupRequest.getPhoneNumber());
        user.setUsername(userSignupRequest.getUsername());

        addressRepository.save(addressEntity);

        User createdUser = userRepository.save(user);

       return  new UserRegistrationResponse(createdUser.getId(), "Successfully created the user");
    }

    public UserLoginResponse loginUser(UserLoginRequest loginRequest){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email:" + loginRequest.getUsername()));

        return  new UserLoginResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber());

    }


    private void validateUserSignUpRequest(UserSignupRequest userSignupRequest){
        if(userRepository.findByUsername(userSignupRequest.getUsername()).isPresent()){
            throw new UserAlreadyExistException("User already exist with this username");
        }
        if(userRepository.findByEmail(userSignupRequest.getEmail()).isPresent()){
            throw new UserAlreadyExistException("User already exist with this email");
        }
        if(userRepository.findByPhoneNumber(userSignupRequest.getPhoneNumber()).isPresent()){
            throw new UserAlreadyExistException("User already exist with this phone number");
        }
        userSignupRequest.getRole().stream().forEach(role -> {
           if( roleRepository.findByName(UserRole.valueOf(role)).isEmpty()){
             throw new RoleNotFoundException("Requested role is not found");
           }
        });

    }
}
