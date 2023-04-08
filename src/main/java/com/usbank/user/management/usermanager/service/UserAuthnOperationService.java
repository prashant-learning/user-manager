package com.usbank.user.management.usermanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usbank.user.management.usermanager.exception.RoleNotFoundException;
import com.usbank.user.management.usermanager.exception.UserAlreadyExistException;
import com.usbank.user.management.usermanager.model.UserRole;
import com.usbank.user.management.usermanager.model.UserSignupRequest;
import com.usbank.user.management.usermanager.model.entity.Address;
import com.usbank.user.management.usermanager.model.entity.Role;
import com.usbank.user.management.usermanager.model.entity.User;
import com.usbank.user.management.usermanager.model.response.UserRegistrationResponse;
import com.usbank.user.management.usermanager.repository.AddressRepository;
import com.usbank.user.management.usermanager.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.usbank.user.management.usermanager.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthnOperationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RoleRepository roleRepository;


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


        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userSignupRequest.getAddress());
        Address addressEntity = mapper.readValue(jsonString, Address.class);

        user.setAddress(addressEntity);
        user.setRole(roleSet);
        user.setEmail(userSignupRequest.getEmail());
        user.setPassword(userSignupRequest.getPassword());
        user.setPhoneNumber(userSignupRequest.getPhoneNumber());
        user.setUsername(userSignupRequest.getUsername());

        addressRepository.save(addressEntity);

        User createdUser = userRepository.save(user);

       return  new UserRegistrationResponse(createdUser.getId(), "Successfully created the user");
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
