package com.allenchankl.remembersample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.allenchankl.remember2.Remember;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * A simple activity that counts how many times it's been resumed via {@link Remember}.
 */
public class RememberSample extends AppCompatActivity implements Remember.Callback{

    private static final String TAG = RememberSample.class.getSimpleName();

    private static final String PREFS_NAME = "com.remember.example";

    private static Remember sRemember;

    private static Remember getRemember(Context context) {
        if (sRemember == null) {
            sRemember = Remember.create(context, PREFS_NAME);
        }
        return sRemember;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_sample);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int howMany = getRemember(this).getInt("counter", 1);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        TextView textView = (TextView) findViewById(R.id.text_view);
        String testString = getApplicationContext().getResources().getQuantityString(R.plurals.youve_resumed, howMany,
                howMany);
        textView.setText(testString);

        getRemember(this).putInt("counter", howMany + 1);

        // Some other simple examples:
        getRemember(this).putFloat("test-float", 1111.0f);
        getRemember(this).putString("test-string", "1. hello world!");
        getRemember(this).putBoolean("test-boolean", true);
        getRemember(this).putLong("test-long", 1_111_111L);

        Log.d(TAG, "put float: " + getRemember(this).getFloat("test-float", 0f));
        Log.d(TAG, "put string: " + getRemember(this).getString("test-string", ""));
        Log.d(TAG, "put boolean: " + getRemember(this).getBoolean("test-boolean", false));
        Log.d(TAG, "put long: " + getRemember(this).getLong("test-long", 0L));

        delay(1000);

        Log.d(TAG, "clean up");
        getRemember(this).clear();

        Log.d(TAG, "Build and commit ->");
        getRemember(this).build()
                .putFloat("test-float", 2222.0f)
                .putString("test-string", "2. hello world!")
                .putBoolean("test-boolean", false)
                .putLong("test-long", 2_222_222L)
                .commit();

        Log.d(TAG, "put float: " + getRemember(this).getFloat("test-float", 0f));
        Log.d(TAG, "put string: " + getRemember(this).getString("test-string", ""));
        Log.d(TAG, "put boolean: " + getRemember(this).getBoolean("test-boolean", false));
        Log.d(TAG, "put long: " + getRemember(this).getLong("test-long", 0L));

        Log.d(TAG, "(Disk) put float: " + sharedPreferences.getFloat("test-float", 0f));
        Log.d(TAG, "(Disk) put string: " + sharedPreferences.getString("test-string", ""));
        Log.d(TAG, "(Disk) put boolean: " + sharedPreferences.getBoolean("test-boolean", false));
        Log.d(TAG, "(Disk) put long: " + sharedPreferences.getLong("test-long", 0L));


        delay(1000);

        Log.d(TAG, "clean up");
        getRemember(this).clear();

        Log.d(TAG, "Build and apply ->");
        getRemember(this).build()
                .putFloat("test-float", 3333.0f)
                .putString("test-string", "3. hello world!")
                .putBoolean("test-boolean", true)
                .putLong("test-long", 3_333_333L)
                .apply(this);

        Log.d(TAG, "put float: " + getRemember(this).getFloat("test-float", 0f));
        Log.d(TAG, "put string: " + getRemember(this).getString("test-string", ""));
        Log.d(TAG, "put boolean: " + getRemember(this).getBoolean("test-boolean", false));
        Log.d(TAG, "put long: " + getRemember(this).getLong("test-long", 0L));

        Log.d(TAG, "(Disk) put float: " + sharedPreferences.getFloat("test-float", 0f));
        Log.d(TAG, "(Disk) put string: " + sharedPreferences.getString("test-string", ""));
        Log.d(TAG, "(Disk) put boolean: " + sharedPreferences.getBoolean("test-boolean", false));
        Log.d(TAG, "(Disk) put long: " + sharedPreferences.getLong("test-long", 0L));

        Log.d(TAG, "clean up");
        getRemember(this).clear();

        delay(1000);

        Log.d(TAG, "Pass null without build->");
        getRemember(this).putString("test-string", null);

        Log.d(TAG, "put string: " + getRemember(this).getString("test-string", "empty"));

        delay(1000);

        Log.d(TAG, "clean up");
        getRemember(this).clear();

        Log.d(TAG, "Pass null with build and apply->");
        getRemember(this).build()
                .putString("test-string", null)
                .apply(this);

        Log.d(TAG, "put string: " + getRemember(this).getString("test-string", "empty"));

        delay(1000);

        Log.d(TAG, "clean up");
        getRemember(this).clear();

        Log.d(TAG, "Pass null with build and commit->");
        getRemember(this).build()
                .putString("test-string", null)
                .commit();

        Log.d(TAG, "put string: " + getRemember(this).getString("test-string", "empty"));

        delay(1000);

        // JSON object example, with callback:
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("some-json-key", "some-json-value");
        } catch (JSONException err) {
            // Don't care
        }
        getRemember(this).putJsonObject("test-json-object", jsonObj, new Remember.Callback() {
            @Override
            public void apply(Boolean success) {
                Log.d(TAG, "put json object: " + getRemember(RememberSample.this).getJsonObject("test-json-object", null));
            }
        });

        // JSON array example, with callback:
        JSONArray jsonArr = new JSONArray();
        jsonArr.put(1);
        jsonArr.put(2);
        jsonArr.put(3);
        getRemember(this).putJsonArray("test-json-array", jsonArr, new Remember.Callback() {
            @Override
            public void apply(Boolean success) {
                Log.d(TAG, "put json array: " + getRemember(RememberSample.this).getJsonArray("test-json-array", null));
            }
        });

        // Query example:
        Set<String> matchingKeys = getRemember(this).query(new Remember.Predicate() {
            @Override
            public boolean match(Object obj) {
                // Look for numeric values >0
                return obj instanceof Number && ((Number) obj).floatValue() > 0;
            }
        });
        Log.d(TAG, "keys with numeric values >0: " + matchingKeys);
    }

    private void delay(int time) {
        try
        {
            Thread.sleep(time);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void apply(Boolean success) {
        Log.d(TAG, "Callback success: " + success);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remember_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
