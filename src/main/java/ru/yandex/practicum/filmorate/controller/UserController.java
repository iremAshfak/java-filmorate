package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAllUsers();
    }

    @GetMapping("{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.findUserByID(userId);
    }

    @PostMapping
    public UserDto create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public UserDto update(@RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriendById(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<Collection<UserDto>> readAllFriendsByUserId(@PathVariable Long id) {
        return new ResponseEntity<>(userService.readAllFriendsByUserId(id), HttpStatus.OK);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<UserDto>> readAllCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return new ResponseEntity<>(userService.readAllCommonFriends(id, otherId), HttpStatus.OK);
    }
}