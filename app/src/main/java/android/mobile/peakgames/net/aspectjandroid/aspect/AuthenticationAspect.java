package android.mobile.peakgames.net.aspectjandroid.aspect;

import android.mobile.peakgames.net.aspectjandroid.Session;
import android.mobile.peakgames.net.aspectjandroid.exception.AuthenticationException;
import android.util.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.HashSet;
import java.util.Set;

//@Aspect
public class AuthenticationAspect {
    private static final String TAG = AuthenticationAspect.class.getName();

    private static final Set<String> AUTH_NAMES = new HashSet<String>();

    static {
        AUTH_NAMES.add("peak");
    }

//    @Pointcut("execution(* android.mobile.peakgames.net.aspectjandroid.AspectActivity.*Auth*(..)) || execution(@android.mobile.peakgames.net.aspectjandroid.SecureMethod * *(..))")
    public void authenticateEntryPoint() {
    }

//    @Around("authenticateEntryPoint()")
    public void authenticateMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        if (AUTH_NAMES.contains(Session.getInstance().getName())) {
            Log.d(TAG, "Authenticate successfully");

            joinPoint.proceed();
        } else {
            Log.e(TAG, "User : " + Session.getInstance().getName() + " is not authenticated");
            throw new AuthenticationException();
        }
    }
}
