package android.mobile.peakgames.net.aspectjandroid.aspect;

import android.util.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class LoggingAspect {
    private static final String TAG = LoggingAspect.class.getName();

    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onClickEntryPoint() {
    }

    @Before("onClickEntryPoint()")
    public void onClickBefore(JoinPoint joinPoint) {
        Log.d(TAG, "Before Advice ==> Signature : " + joinPoint.getSignature());
    }

    @Around("onClickEntryPoint()")
    public void onClickAround(ProceedingJoinPoint joinPoint) {
        Log.d(TAG, "Around Advice ==> Signature : " + joinPoint.getSignature());

        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @After("onClickEntryPoint()")
    public void onClickAfter(JoinPoint joinPoint) {
        Log.d(TAG, "After Advice ==> Signature : " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "onClickEntryPoint()")
    public void onClickAfterReturning() {
        Log.d(TAG, "AfterReturning Advice ==>");
    }
}
