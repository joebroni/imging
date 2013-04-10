package com.corgrimm.imgy.api;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 6/11/12
 * Time: 10:04 AM
 */
public class ImgyApi {
    static StringEntity entity;

    public static ConnectivityManager connectivityManager;

    private static RequestParams params;

    public static void getMainGallery(Context context, JsonHttpResponseHandler getGalleryResponseHandler) {
        ImgyRestClient.get(context, "gallery/hot/viral/0.json", null, getGalleryResponseHandler);
    }

    public static void getImageInfo(Context context, String imageId, JsonHttpResponseHandler imageInfoResponseHandler) {
        ImgyRestClient.get(context, String.format("gallery/image/%s", imageId), null, imageInfoResponseHandler);
    }

    public static void getAlbumImageInfo(Context context, String albumId, String imageId, JsonHttpResponseHandler imageInfoResponseHandler) {
        ImgyRestClient.get(context, String.format("gallery/album/%s/image/%s", albumId, imageId), null, imageInfoResponseHandler);
    }

    public static void getImageComments(Context context, String imageId, JsonHttpResponseHandler imageInfoResponseHandler) {
        ImgyRestClient.get(context, String.format("gallery/image/%s/comments", imageId), null, imageInfoResponseHandler);
    }

    public static boolean CheckInternet(Context context) throws Error {
        boolean isNetAvailable = false;

        if (context != null) {
            ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mgr != null) {
                boolean mobileNetwork;
                boolean wifiNetwork;
                boolean wiMaxNetwork;

                boolean mobileNetworkConnecetd = false;
                boolean wifiNetworkConnecetd = false;
                boolean wiMaxNetworkConnected = false;

                NetworkInfo mobileInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo wiMaxInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

                if (mobileInfo != null) {
                    mobileNetwork = mobileInfo.isAvailable();
                    if (mobileNetwork)
                        mobileNetworkConnecetd = mobileInfo.isConnectedOrConnecting();
                }

                if (wifiInfo != null) {
                    wifiNetwork = wifiInfo.isAvailable();
                    if (wifiNetwork)
                        wifiNetworkConnecetd = wifiInfo.isConnectedOrConnecting();
                }

                if (wiMaxInfo != null) {
                    wiMaxNetwork = wiMaxInfo.isAvailable();
                    if (wiMaxNetwork)
                        wiMaxNetworkConnected = wiMaxInfo.isConnectedOrConnecting();
                }

                isNetAvailable = (mobileNetworkConnecetd || wifiNetworkConnecetd || wiMaxNetworkConnected);
            }
        }
        return isNetAvailable;
    }
}
