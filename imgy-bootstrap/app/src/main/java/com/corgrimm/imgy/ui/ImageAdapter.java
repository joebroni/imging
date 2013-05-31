package com.corgrimm.imgy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.models.ImageIdUrl;
import com.corgrimm.imgy.util.DrawableManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 3/22/13
 * Time: 9:08 AM
 */
public class ImageAdapter extends BaseAdapter {

    private Context ctx;
    int imageBackground;
    ArrayList<ImageIdUrl> links;
    DrawableManager drawableManager;
    int width;
    SmartImageView imageView;
    ObjectMapper objectMapper;


    public ImageAdapter(Context c, ArrayList<ImageIdUrl> links) {
        ctx = c;
        this.links = links;
        TypedArray ta = ctx.obtainStyledAttributes(R.styleable.Gallery1);
        imageBackground = ta.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 1);
        ta.recycle();
        drawableManager = new DrawableManager();

        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();

        objectMapper = new ObjectMapper();

    }

    @Override
    public int getCount() {
        return links.size();
    }


    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        if (arg1 == null) {  // if it's not recycled, initialize some attributes
            imageView = new SmartImageView(ctx);
            imageView.setLayoutParams(new GridView.LayoutParams(width/2, width/2));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (SmartImageView) arg1;
        }

        imageView.setImageDrawable(null);
        ImageIdUrl imageIdUrl = links.get(arg0);

        String imageLink = imageIdUrl.getLink();
        imageLink = imageLink.substring(0, imageLink.length()-4) + "m" + imageLink.substring(imageLink.length()-4);
        imageView.setImageUrl(imageLink);
        return imageView;

    }
}
