package com.example.pivotal.olderselfsignedcert;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RequestQueueSingleton.cacheDirectory = getCacheDir();
        RequestQueueSingleton.applicationContext = getApplicationContext();
    }

    public void buttonPressed(View view) {
        try {
            RequestQueueSingleton.getRequestQueue().add(makeRequest());
        } catch (Exception e) {
            setResultText("Missing config see RequestQueueFactory");
        }
    }

    private StringRequest makeRequest() {
        return new StringRequest(Request.Method.GET, "https://10.37.2.6:8000", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                setResultText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                setResultText(error.getMessage());
            }
        });
    }

    private void setResultText(String text) {
        ((TextView) findViewById(R.id.response_result)).setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
