package com.paraett.usersservice.service;

import com.paraett.usersservice.model.dtos.OwnerRegisterUserDto;
import com.paraett.usersservice.model.entities.User;
import com.paraett.usersservice.model.enums.UserType;
import com.paraett.usersservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerOwner(OwnerRegisterUserDto ownerRegisterUserDto, Long companyId) {
        User user = new User(
                ownerRegisterUserDto.getFirstName(),
                ownerRegisterUserDto.getLastName(),
                ownerRegisterUserDto.getEmail(),
                UserType.OWNER,
                true,
                bCryptPasswordEncoder.encode(ownerRegisterUserDto.getPassword()),
                new Date(),
                new Date(),
                new Date(),
                null,
                companyId,
                ownerRegisterUserDto.getNorm(),
                ownerRegisterUserDto.getSalary());
        user = this.userRepository.save(user);

        return user;
    }
}
