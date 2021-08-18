package fr.fistin.limbo.auth.exception;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 20:06
 */
public class InvalidCredentialsException extends AuthenticationException {

    public InvalidCredentialsException() {}

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }

}
