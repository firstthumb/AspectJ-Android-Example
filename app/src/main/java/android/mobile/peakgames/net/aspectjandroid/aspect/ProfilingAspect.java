package android.mobile.peakgames.net.aspectjandroid.aspect;

import android.util.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ProfilingAspect {
    private static final String TAG = ProfilingAspect.class.getName();
    private static final long MAX_ELAPSED_TIME = 1000;

    @Pointcut("execution(String android.mobile.peakgames.net.aspectjandroid.AspectActivity.doHttpCall(..))")
    public void doHttpCallEntryPoint() {
    }

    @Pointcut("execution(* android.mobile.peakgames.net.aspectjandroid.AspectActivity.fetchImage(..))")
    public void loadImageEntryPoint() {
    }

    @Around("doHttpCallEntryPoint() || loadImageEntryPoint()")
    public Object doHttpCallMethod(ProceedingJoinPoint joinPoint) {
        Object returnValue = null;

        long beginTime = System.currentTimeMillis();
        try {
            returnValue = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = (endTime - beginTime);

        Log.d(TAG, joinPoint.getSignature().getName() + " elapsed " + elapsedTime + " ms");
        if (MAX_ELAPSED_TIME < elapsedTime) {
            Log.e(TAG, joinPoint.getSignature() + " exceeded MAX_ELAPSED_TIME, the process is taking too much time");
        }

        return returnValue;
    }
}
