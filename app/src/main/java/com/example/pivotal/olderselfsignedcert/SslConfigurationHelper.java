package com.example.pivotal.olderselfsignedcert;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.HttpClientStack;
import com.example.pivotal.olderselfsignedcert.R;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SslConfigurationHelper {
    public static void configureHostNameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                Log.e("CipherUsed", sslSession.getCipherSuite());
                Log.e("OutboundRequest", "Attempting to reach host: " + hostname);

                //host name with self signed cert we're trying to reach
                return "10.37.2.6".equals(hostname);
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }

    public static TrustManager[] getTrustManagers(Context applicationContext) {
         try {
             CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

             InputStream certificateStream = applicationContext.getResources().openRawResource(R.raw.self_signed_cert);

             Certificate certificate = null;

             try {
                 certificate = certificateFactory.generateCertificate(certificateStream);
             } finally {
                 certificateStream.close();
             }

             KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
             keyStore.load(null, null);
             keyStore.setCertificateEntry("self-signed-cert", certificate);

             TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
             trustManagerFactory.init(keyStore);

             return trustManagerFactory.getTrustManagers();
         } catch (Exception e) {
             return null;
         }
    }
}
