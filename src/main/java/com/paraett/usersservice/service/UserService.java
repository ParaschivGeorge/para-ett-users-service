package com.paraett.usersservice.service;

import com.paraett.usersservice.model.dtos.MassRegisterUserDto;
import com.paraett.usersservice.model.dtos.OwnerRegisterUserDto;
import com.paraett.usersservice.model.entities.User;
import com.paraett.usersservice.model.enums.UserType;
import com.paraett.usersservice.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public List<User> massRegister(List<MassRegisterUserDto> massRegisterUserDtoList, Long companyId) {

        Boolean addedAll = false;
        List<User> users = new ArrayList<>();

        while (!addedAll) {
            addedAll = true;
            for (MassRegisterUserDto massRegisterUserDto: massRegisterUserDtoList) {
                if (this.userRepository.findByEmail(massRegisterUserDto.getEmail()).isPresent()) {
                    continue;
                }
                Optional<User> optionalManager = this.userRepository.findByEmail(massRegisterUserDto.getManagerEmail());
                if (optionalManager.isPresent()) {
                    User user = this.userRepository.save(new User(
                            massRegisterUserDto.getFirstName(),
                            massRegisterUserDto.getLastName(),
                            massRegisterUserDto.getEmail(),
                            massRegisterUserDto.getType(),
                            false,
                            bCryptPasswordEncoder.encode(RandomStringUtils.random(12, true, true)),
                            new Date(),
                            new Date(),
                            new Date(),
                            optionalManager.get().getId(),
                            companyId,
                            massRegisterUserDto.getNorm(),
                            massRegisterUserDto.getSalary()
                    ));

                    users.add(user);

                    //TODO: send email;

                } else {
                    addedAll = false;
                }
            }
        }

        return users;
    }
}
