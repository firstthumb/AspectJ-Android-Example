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

//    @Pointcut(value = "execution(* android.mobile.peakgames.net.aspectjandroid.AspectActivity.fetchImage(String)) && args(imageUri)", argNames = "imageUri")
    public void loadImageEntryPoint(String imageUri) {
    }

//    @Around(value = "loadImageEntryPoint(imageUri)")
    public Object loadImageMethod(ProceedingJoinPoint joinPoint, String imageUri) throws Throwable {
        Object object;
        if (cache.containsKey(imageUri)) {
            Log.d(TAG, "Image " + imageUri + " found in cache");

            Bitmap bitmap = cache.get(imageUri);
            return bitmap;
        }
        else {
            object = joinPoint.proceed();
            cache.put(imageUri, (Bitmap)object);
            Log.d(TAG, "Cached " + imageUri);
        }

        return object;
    }
}
