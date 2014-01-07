package com.liberty.types;

import com.liberty.annotation.Handler;
import com.liberty.processing.AuthenticationActor;

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 21:13
 */
public class RequestType {

    @Handler(AuthenticationActor.class)
    public static int RT_AUTHENTICATE = 100;

}
