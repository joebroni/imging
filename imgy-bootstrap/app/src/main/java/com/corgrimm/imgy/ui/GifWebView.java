package com.corgrimm.imgy.ui;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 3/29/13
 * Time: 2:03 PM
 */
public class GifWebView extends WebView {

    public GifWebView(Context context) {
        super(context);
    }

    public void setUrl(String url) {
        loadUrl(url);
    }
}
