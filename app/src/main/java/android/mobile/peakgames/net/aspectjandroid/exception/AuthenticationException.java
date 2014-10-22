package android.mobile.peakgames.net.aspectjandroid.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("You are not allowed to execute the code");
    }
}
