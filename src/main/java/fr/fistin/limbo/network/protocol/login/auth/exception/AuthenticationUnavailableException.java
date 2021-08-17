package fr.fistin.limbo.network.protocol.login.auth.exception;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 19:59
 */
public class AuthenticationUnavailableException extends AuthenticationException {

    public AuthenticationUnavailableException() {}

    public AuthenticationUnavailableException(String message) {
        super(message);
    }

    public AuthenticationUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationUnavailableException(Throwable cause) {
        super(cause);
    }

}
