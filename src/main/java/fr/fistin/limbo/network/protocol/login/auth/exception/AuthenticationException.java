package fr.fistin.limbo.network.protocol.login.auth.exception;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 19:58
 */
public class AuthenticationException extends Exception {

    public AuthenticationException() {}

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

}
