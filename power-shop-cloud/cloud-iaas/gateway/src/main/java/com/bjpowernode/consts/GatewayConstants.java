package com.bjpowernode.consts;

import java.util.Arrays;
import java.util.List;

public interface GatewayConstants {
    List<String> WHITE_PATH = Arrays.asList(
            "/oauth/token"
    );

    String AUTHORIZATION_HEADER = "authorization";
    String BEARER_PREFIX_UPPER_CASE = "Bearer";
    String BEARER_PREFIX_LOWER_CASE = "bearer";
    String CONTENT_TYPE = "Content-Type";
    String APPLICATION_UTF_8 = "application/json;charset=utf-8";
    String LOGIN_ROUTE_ID = "toAuthServer";
    String LB_AUTH_SERVER = "lb://auth-server";
    String TOKEN_PREFIX = "access_token";
    String TOKEN_EXPRESS_IN = "expires_in";
    String REDIS_TOKEN_PREFIX = "jwt:token:";


}
