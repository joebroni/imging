package com.corgrimm.imgy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.models.Album;
import com.corgrimm.imgy.models.AlbumImage;
import com.corgrimm.imgy.models.GalleryAlbum;
import com.corgrimm.imgy.models.GalleryImage;
import com.corgrimm.imgy.util.DrawableManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.image.SmartImageView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 3/22/13
 * Time: 9:08 AM
 */
public class AlbumImageAdapter extends BaseAdapter {

    private Context ctx;
    List<AlbumImage> images;
    ObjectMapper objectMapper;


    public AlbumImageAdapter(Context c, List<AlbumImage> images) {
        ctx = c;
        this.images = images;
        objectMapper = new ObjectMapper();

    }

    @Override
    public int getCount() {
        return images.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View albumImageView = convertView;
        if (albumImageView == null) {
            albumImageView = LayoutInflater.from(ctx).inflate(R.layout.album_image, parent, false);
        }
        AlbumImage image = images.get(position);
//        if (arg1 == null) {  // if it's not recycled, initialize some attributes
//            imageView = new SmartImageView(ctx);
//            imageView.setLayoutParams(new GridView.LayoutParams(width/2, width/2));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(0, 0, 0, 0);
//        } else {
//            imageView = (SmartImageView) arg1;
//        }
        SmartImageView imageView = (SmartImageView) albumImageView.findViewById(R.id.imgImage);
        TextView title = (TextView) albumImageView.findViewById(R.id.caption);

        imageView.setImageDrawable(null);
        title.setVisibility(View.GONE);

        imageView.setImageUrl(image.getLink());
        if (image.getTitle() != null && !image.getTitle().equals("")) {
            title.setVisibility(View.VISIBLE);
            title.setText(image.getTitle());
        }
            //drawableManager.fetchDrawableOnThread(((GalleryImage)object).getLink(), imageView);
        return albumImageView;

    }
}
