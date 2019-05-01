package com.paraett.usersservice.service;

import com.paraett.usersservice.exception.ActivationCodeInvalidException;
import com.paraett.usersservice.exception.NotFoundException;
import com.paraett.usersservice.model.dtos.AccountActivationUserDto;
import com.paraett.usersservice.model.dtos.MassRegisterUserDto;
import com.paraett.usersservice.model.dtos.OwnerRegisterUserDto;
import com.paraett.usersservice.model.entities.User;
import com.paraett.usersservice.model.enums.UserType;
import com.paraett.usersservice.repository.UserRepository;
import com.paraett.usersservice.repository.UserSpecifications;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User registerOwner(OwnerRegisterUserDto ownerRegisterUserDto, Long companyId) {

        Date date = new Date();


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
                calculateFreeDaysLeft(ownerRegisterUserDto.getFreeDaysTotal()));
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
                    throw new NotFoundException("manager email: " + massRegisterUserDto.getManagerEmail());
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
                            calculateFreeDaysLeft(massRegisterUserDto.getFreeDaysTotal())
                    ));

                    users.add(user);

                    emailService.sendAccountActivationMessage(user.getEmail(), user.getPassword());

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
            throw new NotFoundException("email: " + accountActivationUserDto.getEmail());
        }
    }

    public List<User> getAllUsers(Long companyId, Long managerId) {
        return this.userRepository.findAll(UserSpecifications.findAllFiltered(companyId, managerId));
    }

    public User getUser(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new NotFoundException("id: " + id);
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (updatedUser.getType() != null) {
                user.setType(updatedUser.getType());
            }
            if (updatedUser.getEmail() != null && !userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                user.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
            }
            if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isEmpty()) {
                user.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null && !updatedUser.getLastName().isEmpty()) {
                user.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getManagerId() != null) {
                Optional<User> optionalManager = this.userRepository.findById(updatedUser.getManagerId());
                if (optionalManager.isPresent()) {
                    user.setManagerId(updatedUser.getManagerId());
                } else {
                    throw new NotFoundException("manager id: " + updatedUser.getManagerId());
                }
            }
            if (updatedUser.getCompanyId() != null) {
                user.setCompanyId(updatedUser.getCompanyId());
            }
            if (updatedUser.getNorm() != null) {
                user.setNorm(updatedUser.getNorm());
            }
            if (updatedUser.getFreeDaysLeft() != null) {
                user.setFreeDaysLeft(updatedUser.getFreeDaysLeft() );
            }
            return this.userRepository.save(user);
        }
        throw new NotFoundException("id: " + id);
    }

    public void deleteUser(Long id) {
        if (this.userRepository.findById(id).isPresent()) {
            this.userRepository.deleteById(id);
            return;
        }
        throw new NotFoundException("id: " + id);
    }

    public void deleteUsers(Long companyId) {
        this.userRepository.deleteAllByCompanyId(companyId);
    }

    private Integer calculateFreeDaysLeft(Integer freeDaysTotal) {
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        return (int)Math.ceil((365.25 - dayOfYear) * freeDaysTotal / 365.25);
    }
}
