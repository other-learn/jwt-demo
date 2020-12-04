package com.lm.example.jwtdemo.auth.model;

import com.lm.example.jwtdemo.constants.AuthConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author meng-liang
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class HasAnyRoleBean {
    private String[] roles;
    private AuthConstants.AuthorizationEnum[] auth;
    public HasAnyRoleBean() {
        this.roles = new String[]{"null"};
        this.auth = new AuthConstants.AuthorizationEnum[]{AuthConstants.AuthorizationEnum.NULL};
    }
}
