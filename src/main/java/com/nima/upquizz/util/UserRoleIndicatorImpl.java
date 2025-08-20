package com.nima.upquizz.util;

import com.nima.upquizz.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserRoleIndicatorImpl implements UserRoleIndicator{
    @Override
    public boolean isUserAdmin(User user) {
        return user.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals("ROLE_ADMIN"));
    }
}
