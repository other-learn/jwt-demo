package com.lm.example.jwtdemo.service;

import com.lm.example.jwtdemo.auth.model.HasAnyRoleBean;

import java.util.Set;

/**
 * @author meng-liang
 */
public interface AuthService {
    Set<HasAnyRoleBean> getRoleAndAuthByUserId(String userId);
}
