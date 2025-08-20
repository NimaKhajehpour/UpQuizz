package com.nima.upquizz.controller;

import com.nima.upquizz.entity.User;
import com.nima.upquizz.request.PasswordChangeRequest;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.UserResponse;
import com.nima.upquizz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User endpoints", description = "endpoints used to access user")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "get user info")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserInfo(){
        return userService.getUserInfo();
    }

    @Operation(summary = "get all users")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<UserResponse> getAllUsers(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }

    @Operation(summary = "search users")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<UserResponse> searchUser(
            @RequestParam @Size(min = 3, message = "username must at least have 3 characters") String username,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return userService.searchUsers(username, pageable);
    }

    @Operation(summary = "get user by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id){
        return userService.getUserById(id);
    }

    @Operation(summary = "delete user")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(){
        userService.deleteUser();
    }

    @Operation(summary = "change password")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest){
        userService.changePassword(passwordChangeRequest);
    }
}
