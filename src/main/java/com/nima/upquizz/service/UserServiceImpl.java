package com.nima.upquizz.service;

import com.nima.upquizz.entity.Authority;
import com.nima.upquizz.entity.User;
import com.nima.upquizz.repository.ReportRepository;
import com.nima.upquizz.repository.UserRepository;
import com.nima.upquizz.request.PasswordChangeRequest;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.UserResponse;
import com.nima.upquizz.util.FindUserAuth;
import com.nima.upquizz.util.PageResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final FindUserAuth findUserAuthentication;
    private final QuizService quizService;
    private final ReportService reportService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReportRepository reportRepository;
    private final PageResponseUtil pageResponseUtil;

    public UserServiceImpl(FindUserAuth findUserAuthentication, QuizService quizService, ReportService reportService, UserRepository userRepository, PasswordEncoder passwordEncoder, ReportRepository reportRepository, PageResponseUtil pageResponseUtil) {
        this.findUserAuthentication = findUserAuthentication;
        this.quizService = quizService;
        this.reportService = reportService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.reportRepository = reportRepository;
        this.pageResponseUtil = pageResponseUtil;
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserInfo() {
        User user = findUserAuthentication.getAuthenticatedUser();

        return createUserResponse(user);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<User> page = userRepository.findAll(pageable);
        List<UserResponse> responses = page.getContent().stream().map(this::createUserResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserById(long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<User> requestedUser = userRepository.findById(id);

        if (requestedUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return createUserResponse(requestedUser.get());
    }

    @Transactional
    @Override
    public void deleteUser() {
        User user = findUserAuthentication.getAuthenticatedUser();
        if (isAdmin(user) && adminCount() < 2){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Last admin cant be deleted");
        }
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();

        if (!passwordEncoder.matches(passwordChangeRequest.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old passwords don't match");
        }
        if (passwordChangeRequest.oldPassword().equals(passwordChangeRequest.newPassword())) {
            throw new IllegalArgumentException("New passwords should be different from the old password");
        }
        if (!passwordChangeRequest.newPassword().equals(passwordChangeRequest.confirmPassword())){
            throw new IllegalArgumentException("Confirm passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeRequest.newPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<UserResponse> searchUsers(String username, Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<User> page = userRepository.findUsersByUsernameContains(username, pageable);
        List<UserResponse> responses = page.getContent().stream().map(this::createUserResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }



    private UserResponse createUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getAuthorities().stream().map(it -> new Authority(it.getAuthority())).toList(),
                quizService.getQuizCountByOwner(user),
                reportRepository.countByOwner(user)
        );
    }

    private int adminCount(){
        return userRepository.countUserByAuthorities(List.of(new Authority("ROLE_ADMIN")));
    }

    private boolean isAdmin(User user){
        return user.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals("ROLE_ADMIN"));
    }
}
