package com.nima.upquizz.service;

import com.nima.upquizz.entity.User;
import com.nima.upquizz.request.PasswordChangeRequest;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse getUserInfo();
    PageResponse<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserById(long id);
    void deleteUser();
    void changePassword(PasswordChangeRequest passwordChangeRequest);
    PageResponse<UserResponse> searchUsers(String username, Pageable pageable);
}
