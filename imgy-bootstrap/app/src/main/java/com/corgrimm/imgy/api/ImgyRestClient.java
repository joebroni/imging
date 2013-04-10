package com.corgrimm.imgy.api;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import com.corgrimm.imgy.R;
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

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 6/7/12
 * Time: 7:49 AM
 */
public class ImgyRestClient {

    // production base urls
    public static final String IMGY_BASE_URL = "https://api.imgur.com/3/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

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

//        client.addHeader("Content-Type", "application/json");
//        client.setUserAgent(getAgentString(context));
        setHeaders();
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
//            FlurryAgent.logEvent(context.getString(R.string.flurry_event_no_connectivity));
        } else {
            client.get(getAbsoluteUrl(context, url), params, responseHandler);
//            JLogger.getInstance(context).log("Jingit: Get url: " + getAbsoluteUrl(context, url));
            Log.d("IMGY", "Get url: " + url);
        }

    }

    public static void rawUrlGet(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

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
//        if (url.contains("/ws/"))
//            url = url.replace("/ws/", "/json/");
//        client.addHeader("Content-Type", "application/json");
//        client.setUserAgent(getAgentString(context));
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
//            FlurryAgent.logEvent(context.getString(R.string.flurry_event_no_connectivity));
        } else {
            client.get(url, params, responseHandler);
//            JLogger.getInstance(context).log("Jingit: Get url: " + url);
            Log.d("IMGY", "Get raw url: " + url);
        }
    }

    public static void post(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.addHeader("Content-Type", "application/json");
//        client.setUserAgent(getAgentString(context));

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
//            FlurryAgent.logEvent(context.getString(R.string.flurry_event_no_connectivity));
        } else {
            client.post(getAbsoluteUrl(context, url), params, responseHandler);
//            JLogger.getInstance(context).log("Jingit: Post url: " + getAbsoluteUrl(context, url));
            Log.d("IMGY", "Post url: " + url);
        }
    }

    public static void postWithBody(final Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
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
//        client.setUserAgent(getAgentString(context));
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
//            FlurryAgent.logEvent(context.getString(R.string.flurry_event_no_connectivity));
        } else {
            client.post(context, getAbsoluteUrl(context, url), entity, contentType, responseHandler);
//            JLogger.getInstance(context).log("Jingit: Post with Body url: " + getAbsoluteUrl(context, url));
            Log.d("IMGY", "Post with Body url: " + url);
        }
    }

//    public static void postSecureWithBody(final Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
//        try {
//            KeyStore trustStore = null;
//            try {
//                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            } catch (KeyStoreException e) {
//                e.printStackTrace();
//            }
//            trustStore.load(null, null);
//            try {
//                client.setSSLSocketFactory(new MySSLSocketFactory(trustStore));
//            } catch (KeyManagementException e) {
//                e.printStackTrace();
//            } catch (KeyStoreException e) {
//                e.printStackTrace();
//            } catch (UnrecoverableKeyException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        }
//        client.addHeader("X-Requested-By", "jingit-csrf");
//        client.setUserAgent(getAgentString(context));
//        if (!ImgyApi.CheckInternet(context)) {
//            AlertDialog alert = new AlertDialog.Builder(context).create();
//            alert.setTitle(context.getString(R.string.oops));
//            alert.setMessage(context.getString(R.string.error_no_connection));
//            alert.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    ((Activity) context).finish();
//                }
//            });
//            alert.show();
////            FlurryAgent.logEvent(context.getString(R.string.flurry_event_no_connectivity));
//        } else {
//            client.post(context, getSecureAbsoluteUrl(context, url), entity, contentType, responseHandler);
////            JLogger.getInstance(context).log("Jingit: Post with Body url: " + getSecureAbsoluteUrl(context, url));
//        }
//    }

    public static void postRawUrlWithBody(final Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
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
//        client.setUserAgent(getAgentString(context));
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
//            FlurryAgent.logEvent(context.getString(R.string.flurry_event_no_connectivity));
        } else {
            client.post(context, url, entity, contentType, responseHandler);
//            JLogger.getInstance(context).log("Jingit: Post with Body url: " + url);
            Log.d("IMGY", "Post raw url with body url: " + url);
        }
    }

    public static void postRawUrl(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
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
//        client.setUserAgent(getAgentString(context));
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
//            FlurryAgent.logEvent(context.getString(R.string.flurry_event_no_connectivity));
        } else {
            client.post(context, url, params, responseHandler);
//            JLogger.getInstance(context).log("Jingit: Post url: " + url);
            Log.d("IMGY", "Post raw url: " + url);
        }
    }

    public static void deleteRawUrl(final Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
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
//        client.setUserAgent(getAgentString(context));
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
//            FlurryAgent.logEvent(context.getString(R.string.flurry_event_no_connectivity));
        } else {
            client.delete(context, url, responseHandler);
//            JLogger.getInstance(context).log("Jingit: Delete url: " + url);
            Log.d("IMGY", "Delete raw url: " + url);
        }
    }

    public static void putRawUrl(final Context context, String url, HttpEntity entity, String contentTyoe, AsyncHttpResponseHandler responseHandler) {
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
//        client.setUserAgent(getAgentString(context));
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
//            FlurryAgent.logEvent(context.getString(R.string.flurry_event_no_connectivity));
        } else {
            client.put(context, url, entity, contentTyoe, responseHandler);
//            JLogger.getInstance(context).log("Jingit: Put url: " + url);
            Log.d("IMGY", "Put raw url: " + url);
        }
    }

//    public static void getSecure(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
////        client.addHeader("Content-Type", "application/json");
////        client.addHeader("User_Agent", getAgentString(context));
//        client.addHeader("X-Requested-By", "jingit-csrf");
//        client.get(getSecureAbsoluteUrl(context, url), params, responseHandler);
////        JLogger.getInstance(context).log("Jingit: Get Secure url: " + getAbsoluteUrl(context, url));
//    }
//
//    public static void postSecure(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
////        client.addHeader("Content-Type", "application/json");
////        client.addHeader("User_Agent", getAgentString(context));
//        client.addHeader("X-Requested-By", "jingit-csrf");
//        client.post(getSecureAbsoluteUrl(context, url), params, responseHandler);
////        JLogger.getInstance(context).log("Jingit: Post Secure url: " + getAbsoluteUrl(context, url));
//    }

    public static String getAbsoluteUrl(Context context, String relativeUrl) {
            return IMGY_BASE_URL +relativeUrl;
    }

    private static void setHeaders() {
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Client-ID 8a24b67691c0319");
    }

//    private static String getSecureAbsoluteUrl(Context context, String relativeUrl) {
//
//        if (environment == Constants.APPLICATION_ENVIRONMENT_PRODUCTION)
//            return JINGIT_URL_SCHEME + JINGIT_DEBIT_CARD_HOST_PRODUCTION + relativeUrl;
//        else if (environment == Constants.APPLICATION_ENVIRONMENT_SANDBOX)
//            return JINGIT_URL_SCHEME + JINGIT_DEBIT_CARD_HOST_SANDBOX + relativeUrl;
//        else if (environment == Constants.APPLICATION_ENVIRONMENT_TEST)
//            return JINGIT_URL_SCHEME + JINGIT_DEBIT_CARD_HOST_TEST + relativeUrl;
//        else if (environment == Constants.APPLICATION_ENVIRONMENT_PERF)
//            return JINGIT_URL_SCHEME + JINGIT_DEBIT_CARD_HOST_PERF + relativeUrl;
//        else
//            return null;
//    }

//    private static String getAgentString(Context context) {
//        String agentString;
//        String strVersionCode = null;
//        String strVersionName = null;
//        try {
//            PackageInfo packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            strVersionCode = String.valueOf(packInfo.versionCode);
//            strVersionName = packInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            strVersionName = "Cannot load Version!";
//            strVersionCode = "?";
//        }
//        String releaseString = Build.VERSION.RELEASE;
//        if (DeviceInfo.isDeviceRooted()) {
//            releaseString = releaseString + "*";
//        }
//
//        agentString = "Jingit/" + strVersionName + " (" + Build.MODEL + "; Android; " + releaseString + ")";
//
//        return agentString;
//    }

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
