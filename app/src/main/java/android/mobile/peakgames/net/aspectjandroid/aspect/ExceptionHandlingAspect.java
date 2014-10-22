package android.mobile.peakgames.net.aspectjandroid.aspect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.mobile.peakgames.net.aspectjandroid.exception.AuthenticationException;
import android.util.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ExceptionHandlingAspect {
    private static final String TAG = ExceptionHandlingAspect.class.getName();

    @Pointcut("execution(* android.mobile.peakgames.net.aspectjandroid.AspectActivity.*(..))")
    public void exceptionEntryPoint() {
    }

    @AfterThrowing(pointcut = "exceptionEntryPoint()", throwing = "throwable")
    public void exceptionMethod(JoinPoint joinPoint, Throwable throwable) {
        Log.e(TAG, "Exception caught : " + throwable + " on method : " + joinPoint.getSignature());

        if (joinPoint.getTarget() instanceof Activity) {
            if (throwable instanceof AuthenticationException) {
                new AlertDialog.Builder((Context) joinPoint.getTarget())
                        .setTitle("Authentication Error")
                        .setMessage("You are not authenticated")
                        .show();
            } else {
                new AlertDialog.Builder((Context) joinPoint.getTarget())
                        .setTitle("Error")
                        .setMessage("Error occurred at : " + joinPoint.getSignature() + " Exception : " + throwable)
                        .show();
            }
        }
    }
}
