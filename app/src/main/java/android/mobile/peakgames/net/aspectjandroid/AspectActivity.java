package android.mobile.peakgames.net.aspectjandroid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class AspectActivity extends Activity {
    private static final String TAG = AspectActivity.class.getName();
    private AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
    private HttpGet getRequest = new HttpGet("http://www.peakgames.net/");

    private static final String[] IMAGES = new String[21];
    static {
        for (int i = 0; i < 21; i++) {
            IMAGES[i] = String.format("http://www.google.com.tr/intl/tr/logoyapsana/images/winners/30_%02d.gif", (i+1));
        }
    }

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);

        setContentView(R.layout.activity_aspect);
        imageView = (ImageView) findViewById(R.id.imageView);

        {
            Button button = (Button) findViewById(R.id.clickButton1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeAsyncCall();
                }
            });
        }

        {
            Button button = (Button) findViewById(R.id.clickButton2);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doAsyncCall();
                }
            });
        }

        {
            Button button = (Button) findViewById(R.id.clickButton3);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doAsyncCall();
                }
            });
        }
    }

    public void makeAsyncCall() {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                return fetchImage(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }.execute((IMAGES[new Random().nextInt(21)]));
    }

    public Bitmap fetchImage(String imageUri) {
        return ImageLoader.getInstance().loadImageSync(imageUri);
    }

    public void doAsyncCall() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return doHttpCall();
            }
        }.execute();
    }

    public String doHttpCall() {
        try {
            HttpResponse response = client.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        return sb.toString();
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aspect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
