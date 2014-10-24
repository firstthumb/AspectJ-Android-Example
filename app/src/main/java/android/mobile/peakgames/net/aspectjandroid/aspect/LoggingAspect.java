package android.mobile.peakgames.net.aspectjandroid.aspect;

import android.util.Log;
import android.widget.Button;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

//@Aspect
public class LoggingAspect {
    private static final String TAG = LoggingAspect.class.getName();

//    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onClickEntryPoint() {
    }

//    @Before("onClickEntryPoint()")
    public void onClickBefore(JoinPoint joinPoint) {
        Log.d(TAG, "Before Advice ==> Clicked on : " + ((Button)joinPoint.getArgs()[0]).getText());
    }

//    @Around("onClickEntryPoint()")
    public void onClickAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.d(TAG, "Around Advice ==> Clicked on : " + ((Button)joinPoint.getArgs()[0]).getText());

        joinPoint.proceed();
    }

//    @After("onClickEntryPoint()")
    public void onClickAfter(JoinPoint joinPoint) {
        Log.d(TAG, "After Advice ==> Clicked on : " + ((Button)joinPoint.getArgs()[0]).getText());
    }

//    @AfterReturning(pointcut = "onClickEntryPoint()")
    public void onClickAfterReturning() {
        Log.d(TAG, "AfterReturning Advice ==>");
    }
}
