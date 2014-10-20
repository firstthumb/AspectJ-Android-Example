package android.mobile.peakgames.net.aspectjandroid.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class MyAspect {

    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onClickEntryPoint() {
    }

    @Before("onClickEntryPoint()")
    public void onClickBefore(JoinPoint joinPoint) {

    }

    @Around("onClickEntryPoint()")
    public void onClickAround(ProceedingJoinPoint joinPoint) {
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @After("onClickEntryPoint()")
    public void onClickAfter(JoinPoint joinPoint) {

    }

    @AfterReturning(pointcut = "onClickEntryPoint()")
    public void onClickAfterReturning() {

    }
}
