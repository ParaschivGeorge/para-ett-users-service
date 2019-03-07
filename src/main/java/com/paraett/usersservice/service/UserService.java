package com.paraett.usersservice.service;

import com.paraett.usersservice.exception.ActivationCodeInvalidException;
import com.paraett.usersservice.exception.UserNotFoundException;
import com.paraett.usersservice.model.dtos.AccountActivationUserDto;
import com.paraett.usersservice.model.dtos.MassRegisterUserDto;
import com.paraett.usersservice.model.dtos.OwnerRegisterUserDto;
import com.paraett.usersservice.model.entities.User;
import com.paraett.usersservice.model.enums.UserType;
import com.paraett.usersservice.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

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

        // Check if all managers are set
        for (MassRegisterUserDto massRegisterUserDto: massRegisterUserDtoList) {
            if (!this.userRepository.findByEmail(massRegisterUserDto.getManagerEmail()).isPresent()) {
                Boolean managerFound = false;
                for (MassRegisterUserDto massRegisterUserDto1: massRegisterUserDtoList) {
                    if (massRegisterUserDto1.getEmail().equals(massRegisterUserDto.getManagerEmail()) &&
                            ((massRegisterUserDto1.getType() == UserType.MANAGER) || (massRegisterUserDto1.getType() == UserType.OWNER))) {
                        managerFound = true;
                        break;
                    }
                }
                if (!managerFound) {
                    throw new UserNotFoundException("manager email: " + massRegisterUserDto.getManagerEmail());
                }
            }
        }

        // Add all users
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

    public User activateAccount(AccountActivationUserDto accountActivationUserDto) {
        Optional<User> optionalUser = this.userRepository.findByEmail(accountActivationUserDto.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                return user;
            }
            if (user.getPassword().equals(accountActivationUserDto.getActivationCode())) {
                user.setPassword(bCryptPasswordEncoder.encode(accountActivationUserDto.getPassword()));
                user.setEnabled(true);
                return this.userRepository.save(user);
            } else {
                throw new ActivationCodeInvalidException("Activation code invalid!");
            }
        } else {
            throw new UserNotFoundException("email: " + accountActivationUserDto.getEmail());
        }
    }
}
