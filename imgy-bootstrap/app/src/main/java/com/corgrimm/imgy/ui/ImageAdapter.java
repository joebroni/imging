package com.corgrimm.imgy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.models.Album;
import com.corgrimm.imgy.models.AlbumImage;
import com.corgrimm.imgy.models.GalleryAlbum;
import com.corgrimm.imgy.models.GalleryImage;
import com.corgrimm.imgy.util.DrawableManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.image.SmartImageView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    ArrayList<Object> objects;
    DrawableManager drawableManager;
    int width;
    SmartImageView imageView;
    ObjectMapper objectMapper;


    public ImageAdapter(Context c, ArrayList<Object> objects) {
        ctx = c;
        this.objects = objects;
        TypedArray ta = ctx.obtainStyledAttributes(R.styleable.Gallery1);
        imageBackground = ta.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 1);
        ta.recycle();
        drawableManager = new DrawableManager();

        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        objectMapper = new ObjectMapper();

    }

    @Override
    public int getCount() {
        return objects.size();
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
        Object object = objects.get(arg0);
        if (object.getClass() == GalleryImage.class) {
            String imageLink = ((GalleryImage)object).getLink();
            imageLink = imageLink.substring(0, imageLink.length()-4) + "m" + imageLink.substring(imageLink.length()-4);
            imageView.setImageUrl(imageLink);
            //drawableManager.fetchDrawableOnThread(((GalleryImage)object).getLink(), imageView);
        }
        else if (object.getClass() == GalleryAlbum.class) {
            GalleryAlbum album = (GalleryAlbum) object;
            ImgyApi.getAlbumImageInfo(ctx, album.getId(), album.getCover(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);

                    try {
                        Album albumFull = objectMapper.readValue(String.valueOf(response.getJSONObject("data")), Album.class);
                        for (AlbumImage image : albumFull.getImages()) {
                            if (image.getId().equals(albumFull.getCover())) {
                                imageView.setImageUrl(image.getLink());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    super.onFailure(error, content);
                }
            });
        }
        return imageView;

    }
}
