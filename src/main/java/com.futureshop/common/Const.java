package com.futureshop.common;

/**
 * Created by yangzhou on 2017-11-27.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    // 需要改进 每个用户有自己的salt
    public static final String MD5_SALT = "-DPb?{RNDLL/[u=[|nYV?Zaan/fv/^u%##24W^.Bt+z7CxI.zXyC4.e[i(/@MP{w";
}
