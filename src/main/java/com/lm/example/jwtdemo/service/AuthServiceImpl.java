package com.lm.example.jwtdemo.service;

import com.lm.example.jwtdemo.auth.model.HasAnyRoleBean;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author meng-liang
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public Set<HasAnyRoleBean> getRoleAndAuthByUserId(String userId){
        HasAnyRoleBean hasAnyRoleBean = new HasAnyRoleBean().setRoles(new String[]{"admin"});
        Set<HasAnyRoleBean> result = new HashSet<HasAnyRoleBean>();
        result.add(hasAnyRoleBean);
        return result;
    }
}
