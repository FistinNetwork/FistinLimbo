package fr.fistin.limbo.auth.yggdrasil.response;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 20:04
 */
public class YggdrasilResponse {

    private String error;
    private String errorMessage;
    private String cause;

    public YggdrasilResponse() {}

    public String getError() {
        return this.error;
    }

    public String getCause() {
        return this.cause;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    protected void setError(String error) {
        this.error = error;
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected void setCause(String cause) {
        this.cause = cause;
    }

}
