package android.mobile.peakgames.net.aspectjandroid.aspect;

import android.graphics.Bitmap;
import android.util.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.HashMap;

//@Aspect
public class CachingAspect {
    private static final String TAG = CachingAspect.class.getName();

    private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>(21);

//    @Pointcut("execution(* android.mobile.peakgames.net.aspectjandroid.AspectActivity.fetchImage(..))")
    public void loadImageEntryPoint() {
    }

//    @Around("loadImageEntryPoint()")
    public Object loadImageMethod(ProceedingJoinPoint joinPoint) {
        String imageUri = (String) joinPoint.getArgs()[0];

        Object object = null;
        if (cache.containsKey(imageUri)) {
            Log.d(TAG, "Image " + imageUri + " found in cache");

            Bitmap bitmap = cache.get(imageUri);
            return bitmap;
        }
        else {
            try {
                object = joinPoint.proceed();
                cache.put(imageUri, (Bitmap)object);
                Log.d(TAG, "Cached " + imageUri);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        return object;
    }
}
