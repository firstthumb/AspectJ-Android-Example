package android.mobile.peakgames.net.aspectjandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.mobile.peakgames.net.aspectjandroid.exception.AuthenticationException;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class AspectActivity extends Activity {
    private static final String TAG = AspectActivity.class.getName();

    private AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
    private HttpGet getRequest = new HttpGet("http://www.peakgames.net/");

    private String[] images = new String[21];

    private ImageView imageView;

    private static final Set<String> AUTH_NAMES = new HashSet<String>();

    static {
        AUTH_NAMES.add("erol");
    }

    private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>(21);
    private static final long MAX_ELAPSED_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        setContentView(R.layout.activity_aspect);
        imageView = (ImageView) findViewById(R.id.imageView);

        {
            Button button = (Button) findViewById(R.id.clickButton1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Before ID : " + ((Button)v).getText() + " clicked");
                    doAsyncFetchImage();
                    Log.d(TAG, "After ID : " + ((Button)v).getText() + " clicked");
                }
            });
        }

        {
            Button button = (Button) findViewById(R.id.clickButton2);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Before ID : " + ((Button)v).getText() + " clicked");
                    doAsyncHttpCall();
                    Log.d(TAG, "After ID : " + ((Button)v).getText() + " clicked");
                }
            });
        }

        {
            Button button = (Button) findViewById(R.id.clickButton3);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Before ID : " + ((Button)v).getText() + " clicked");
                    try {
                        doAuthAsyncHttpCall();
                    } catch (AuthenticationException exc) {
                        new AlertDialog.Builder(AspectActivity.this)
                                .setTitle("Authentication Error")
                                .setMessage("You are not authenticated")
                                .show();
                    } catch (Exception exc) {
                        new AlertDialog.Builder(AspectActivity.this)
                                .setTitle("Error")
                                .setMessage("Error occurred Exception : " + exc)
                                .show();
                    }
                    Log.d(TAG, "After ID : " + ((Button)v).getText() + " clicked");
                }
            });
        }

        {
            final EditText input = new EditText(this);
            Button button = (Button) findViewById(R.id.clickButton4);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Before ID : " + ((Button)v).getText() + " clicked");
                    new AlertDialog.Builder(AspectActivity.this)
                            .setTitle("Login Name")
                            .setMessage("Enter your login name")
                            .setView(input)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Session.getInstance().setName(input.getText().toString());
                                }
                            })
                            .show();
                    Log.d(TAG, "After ID : " + ((Button)v).getText() + " clicked");
                }
            });
        }
    }

    public void doAsyncFetchImage() {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                return fetchImage(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }.execute((images[new Random().nextInt(21)]));
    }

    public Bitmap fetchImage(String imageUri) {
        long beginTime = System.currentTimeMillis();
        try {
            if (cache.containsKey(imageUri)) {
                Log.d(TAG, "Image " + imageUri + " found in cache");

                Bitmap bitmap = cache.get(imageUri);
                return bitmap;
            } else {
                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(imageUri);
                cache.put(imageUri, bitmap);
                Log.d(TAG, "Cached " + imageUri);
                return bitmap;
            }
        } finally {
            long endTime = System.currentTimeMillis();
            long elapsedTime = (endTime - beginTime);
            Log.d(TAG, "fetchImage elapsed " + elapsedTime + " ms");
            if (MAX_ELAPSED_TIME < elapsedTime) {
                Log.e(TAG, "fetchImage exceeded MAX_ELAPSED_TIME, the process is taking too much time");
            }
        }
    }

    public void doAsyncHttpCall() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return doHttpCall();
            }
        }.execute();
    }

    public void doAuthAsyncHttpCall() {
        if (AUTH_NAMES.contains(Session.getInstance().getName())) {
            Log.d(TAG, "Authenticate successfully");
        } else {
            Log.e(TAG, "User : " + Session.getInstance().getName() + " is not authenticated");
            throw new AuthenticationException();
        }

        doAsyncHttpCall();
    }

    public String doHttpCall() {
        long beginTime = System.currentTimeMillis();
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
        } finally {
            long endTime = System.currentTimeMillis();
            long elapsedTime = (endTime - beginTime);
            Log.d(TAG, "doHttpCall elapsed " + elapsedTime + " ms");
            if (MAX_ELAPSED_TIME < elapsedTime) {
                Log.e(TAG, "doHttpCall exceeded MAX_ELAPSED_TIME, the process is taking too much time");
            }
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

    private void initialize() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);

        for (int i = 0; i < 21; i++) {
            images[i] = String.format("http://www.google.com.tr/intl/tr/logoyapsana/images/winners/30_%02d.gif", (i + 1));
        }
    }
}
