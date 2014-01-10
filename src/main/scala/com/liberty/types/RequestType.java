package com.liberty.types;

import com.liberty.actors.AuthenticationActor;
import com.liberty.actors.StartGameActor;
import com.liberty.annotation.Handler;

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 21:13
 */
public class RequestType {

    @Handler(AuthenticationActor.class)
    public static int RT_AUTHENTICATE = 100;

    @Handler(StartGameActor.class)
    public static int RT_START_GAME = 110;

}
