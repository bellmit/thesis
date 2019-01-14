package com.nikolaj.thesis.thesis.util.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String HEADER_STRING2 = "id";
    public static final String SIGN_UP_URL = "/projectmember/signup";
    public static final String URL = "http://localhost:3000";
    public static final String TOKEN_URL = "/token";

}
