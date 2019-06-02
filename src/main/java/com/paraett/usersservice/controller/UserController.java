package com.paraett.usersservice.controller;

import com.paraett.usersservice.model.dtos.AccountActivationUserDto;
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
        System.out.println("TETS");
        List<User> users = this.userService.massRegister(massRegisterUserDtoList, companyId);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").queryParam("companyId", companyId).build().toUri();

        return ResponseEntity.created(location).body(users);
    }

    @PutMapping("/activateAccount")
    public ResponseEntity<User> activateAccount(@RequestBody AccountActivationUserDto accountActivationUserDto) {
        User user = this.userService.activateAccount(accountActivationUserDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) Long companyId, @RequestParam(required = false) Long managerId) {
        List<User> users = this.userService.getAllUsers(companyId, managerId);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = this.userService.getUser(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = this.userService.getUserByEmail(email);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = this.userService.updateUser(id, user);

        return ResponseEntity.accepted().body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);

        // TODO: update the other services
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Object> deleteUsers(@RequestParam Long companyId) {
        this.userService.deleteUsers(companyId);

        return ResponseEntity.noContent().build();
    }
}
