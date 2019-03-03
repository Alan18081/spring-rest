package com.alex.springrest.shared;


import com.alex.springrest.SpringApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000;
    public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String EMAIL_VERIFICATION_URL = "/users/email-verification/**";
    public static final String PASSWORD_RESET_URL = "/users/reset-password/**";

    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
        return appProperties.getTokenSecret();
    }
}
