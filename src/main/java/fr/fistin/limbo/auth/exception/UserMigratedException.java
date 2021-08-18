package fr.fistin.limbo.auth.exception;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 20:05
 */
public class UserMigratedException extends InvalidCredentialsException {

    public UserMigratedException() {}

    public UserMigratedException(String message) {
        super(message);
    }

    public UserMigratedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserMigratedException(Throwable cause) {
        super(cause);
    }

}
