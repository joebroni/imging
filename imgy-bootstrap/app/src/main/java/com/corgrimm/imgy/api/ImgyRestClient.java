package com.corgrimm.imgy.api;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import com.corgrimm.imgy.R;
import com.google.inject.Inject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.HttpEntity;
import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static com.corgrimm.imgy.core.Constants.Prefs.AUTH_TOKEN;
import static com.corgrimm.imgy.core.Constants.Prefs.PREFS_NAME;
import static com.corgrimm.imgy.core.Constants.Prefs.TOKEN_EXPIRE;
import static com.corgrimm.imgy.core.Constants.Oauth.*;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 6/7/12
 * Time: 7:49 AM
 */
public class ImgyRestClient {
    @Inject protected static SharedPreferences sharedPrefs;

    // production base urls
    public static final String IMGY_BASE_URL = "https://api.imgur.com/3/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        setSocketFactory(client);
        setHeaders(context);
        if (!ImgyApi.CheckInternet(context)) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle(context.getString(R.string.oops));
            alert.setMessage(context.getString(R.string.error_no_connection));
            alert.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) context).finish();
                }
            });
            alert.show();
        } else {
            client.get(getAbsoluteUrl(context, url), params, responseHandler);
            Log.d("IMGY", "Get url: " + url);
        }

    }

    public static void rawUrlGet(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        setSocketFactory(client);
        setHeaders(context);
        if (!ImgyApi.CheckInternet(context)) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle(context.getString(R.string.oops));
            alert.setMessage(context.getString(R.string.error_no_connection));
            alert.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) context).finish();
                }
            });
            alert.show();
        } else {
            client.get(url, params, responseHandler);
            Log.d("IMGY", "Get raw url: " + url);
        }
    }

    public static void post(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        setSocketFactory(client);
        setHeaders(context);
        if (!ImgyApi.CheckInternet(context)) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle(context.getString(R.string.oops));
            alert.setMessage(context.getString(R.string.error_no_connection));
            alert.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) context).finish();
                }
            });
            alert.show();
        } else {
            client.post(getAbsoluteUrl(context, url), params, responseHandler);
            Log.d("IMGY", "Post url: " + url);
        }
    }

    public static void postWithBody(final Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        setSocketFactory(client);
        setHeaders(context);
        if (!ImgyApi.CheckInternet(context)) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle(context.getString(R.string.oops));
            alert.setMessage(context.getString(R.string.error_no_connection));
            alert.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) context).finish();
                }
            });
            alert.show();
        } else {
            client.post(context, getAbsoluteUrl(context, url), entity, contentType, responseHandler);
            Log.d("IMGY", "Post with Body url: " + url);
        }
    }

    public static void postRawUrlWithBody(final Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        setSocketFactory(client);
        setHeaders(context);
        if (!ImgyApi.CheckInternet(context)) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle(context.getString(R.string.oops));
            alert.setMessage(context.getString(R.string.error_no_connection));
            alert.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) context).finish();
                }
            });
            alert.show();
        } else {
            client.post(context, url, entity, contentType, responseHandler);
            Log.d("IMGY", "Post raw url with body url: " + url);
        }
    }

    public static void postRawUrl(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        setSocketFactory(client);
        setHeaders(context);
        if (!ImgyApi.CheckInternet(context)) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle(context.getString(R.string.oops));
            alert.setMessage(context.getString(R.string.error_no_connection));
            alert.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) context).finish();
                }
            });
            alert.show();
        } else {
            client.post(context, url, params, responseHandler);
            Log.d("IMGY", "Post raw url: " + url);
        }
    }

    public static void deleteRawUrl(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        setSocketFactory(client);
        setHeaders(context);
        if (!ImgyApi.CheckInternet(context)) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle(context.getString(R.string.oops));
            alert.setMessage(context.getString(R.string.error_no_connection));
            alert.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) context).finish();
                }
            });
            alert.show();
        } else {
            client.delete(context, url, responseHandler);
            Log.d("IMGY", "Delete raw url: " + url);
        }
    }

    public static void putRawUrl(final Context context, String url, HttpEntity entity, String contentTyoe, AsyncHttpResponseHandler responseHandler) {
        setSocketFactory(client);
        setHeaders(context);
        if (!ImgyApi.CheckInternet(context)) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle(context.getString(R.string.oops));
            alert.setMessage(context.getString(R.string.error_no_connection));
            alert.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) context).finish();
                }
            });
            alert.show();
        } else {
            client.put(context, url, entity, contentTyoe, responseHandler);
            Log.d("IMGY", "Put raw url: " + url);
        }
    }

    public static String getAbsoluteUrl(Context context, String relativeUrl) {
            return IMGY_BASE_URL +relativeUrl;
    }

    private static void setHeaders(Context context) {

        client.addHeader("Accept", "application/json");
        if (ImgyApi.checkForValidAuthToken(context) == TOKEN_VALID) {
            sharedPrefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
            String authToken = sharedPrefs.getString(AUTH_TOKEN, null);
            client.addHeader("Authorization", String.format("Bearer %s", authToken));
        }
        else {
            client.addHeader("Authorization", "Client-ID 8a24b67691c0319");
        }
    }

    private static void setSocketFactory(AsyncHttpClient client) {
        try {
            KeyStore trustStore = null;
            try {
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
            trustStore.load(null, null);
            try {
                client.setSSLSocketFactory(new MySSLSocketFactory(trustStore));
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }
}


class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
