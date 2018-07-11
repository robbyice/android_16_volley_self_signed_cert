package com.example.pivotal.olderselfsignedcert;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.io.File;

import javax.net.ssl.TrustManager;

public class RequestQueueSingleton {

    private static RequestQueue requestQueue;
    public static File cacheDirectory;
    public static Context applicationContext;

    public static RequestQueue getRequestQueue() throws Exception {
        if (cacheDirectory == null) {
            throw new Exception("Need to directly set cache dir on this singleton");
        }

        if (requestQueue == null) {

            Cache cache = new DiskBasedCache(cacheDirectory, 1024 * 1024);

            TrustManager[] customTrustManagers = SslConfigurationHelper.getTrustManagers(applicationContext);
            SslConfigurationHelper.configureHostNameVerifier();
            TLSSocketFactory sslSocketFactory = new TLSSocketFactory(customTrustManagers);

            Network network = new BasicNetwork(new HurlStack(null, sslSocketFactory));

            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
            return requestQueue;
        } else {
            return requestQueue;
        }
    }
}
