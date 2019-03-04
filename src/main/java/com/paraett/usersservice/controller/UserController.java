package com.paraett.usersservice.controller;

import com.paraett.usersservice.model.dtos.MassRegisterUserDto;
import com.paraett.usersservice.model.dtos.OwnerRegisterUserDto;
import com.paraett.usersservice.model.entities.User;
import com.paraett.usersservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registerOwner")
    public ResponseEntity<User> registerOwner(@RequestBody OwnerRegisterUserDto ownerRegisterUserDto, @RequestParam Long companyId) {
        User user = this.userService.registerOwner(ownerRegisterUserDto, companyId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @PostMapping("/massRegister")
    public ResponseEntity<List<User>> massRegister(@RequestBody List<MassRegisterUserDto> massRegisterUserDtoList, @RequestParam Long companyId) {
        List<User> users = this.userService.massRegister(massRegisterUserDtoList, companyId);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").queryParam("companyId", companyId).build().toUri();

        return ResponseEntity.created(location).body(users);
    }
}
