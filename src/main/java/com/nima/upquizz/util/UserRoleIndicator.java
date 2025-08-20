package com.nima.upquizz.util;

import com.nima.upquizz.entity.User;

public interface UserRoleIndicator {
    boolean isUserAdmin(User user);
}
