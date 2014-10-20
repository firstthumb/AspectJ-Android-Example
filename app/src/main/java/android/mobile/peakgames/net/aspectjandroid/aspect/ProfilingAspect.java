package android.mobile.peakgames.net.aspectjandroid.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ProfilingAspect {
    private static final String TAG = ProfilingAspect.class.getName();
    private static final float MAX_ELAPSED_TIME = 1000;

    @Pointcut("execution(* android.mobile.peakgames.net.aspectjandroid.AspectActivity.doHttpCall(..))")
    public void doHttpCallEntryPoint() {
    }

    @Around("doHttpCallEntryPoint()")
    public void doHttpCallMethod(ProceedingJoinPoint joinPoint) {
        Log.d(TAG, "Signature : " + joinPoint.getSignature());

        float beginTime = System.currentTimeMillis();
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        float endTime = System.currentTimeMillis();
        float elapsedTime = (endTime - beginTime);

        Log.d(TAG, "doHttpCallMethod elapsed " + elapsedTime + " ms");
        if (MAX_ELAPSED_TIME < elapsedTime) {
            Log.e(TAG, "doHttpCallMethod exceeded MAX_ELAPSED_TIME, the process is taking too much time");
        }
    }
}
