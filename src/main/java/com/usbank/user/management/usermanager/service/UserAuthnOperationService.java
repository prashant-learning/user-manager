package com.usbank.user.management.usermanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        Set<String> requestRoles = userSignupRequest.getRole();

        Set<Role> entityRole = requestRoles.stream().map(requestRole -> {
            Role role = new Role();
            role.setName(UserRole.valueOf(requestRole));
            return role;
        }).collect(Collectors.toSet());

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userSignupRequest.getAddress());
        Address addressEntity = mapper.readValue(jsonString, Address.class);

        user.setAddress(addressEntity);
        user.setRole(entityRole);
        user.setEmail(userSignupRequest.getEmail());
        user.setPassword(userSignupRequest.getPassword());
        user.setPhoneNumber(userSignupRequest.getPhoneNumber());
        user.setUsername(userSignupRequest.getUsername());

        addressRepository.save(addressEntity);
        entityRole.forEach(role -> roleRepository.save(role));
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
    }
}
