package com.nima.upquizz.response;

import com.nima.upquizz.entity.Authority;

import java.util.List;

public record UserResponse(
        long id,
        String username,
        List<Authority> authorities,
        int quizCount,
        int reportCount
) {
}
