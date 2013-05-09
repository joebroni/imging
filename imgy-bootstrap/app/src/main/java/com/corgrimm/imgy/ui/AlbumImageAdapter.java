package com.corgrimm.imgy.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.models.AlbumImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.image.SmartImageView;

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

        SmartImageView imageView = (SmartImageView) albumImageView.findViewById(R.id.imgImage);
        TextView description = (TextView) albumImageView.findViewById(R.id.description);
        TextView title = (TextView) albumImageView.findViewById(R.id.title);

        imageView.setImageDrawable(null);
        title.setVisibility(View.GONE);
        description.setVisibility(View.GONE);

//        imageView.setSampleSize(2);
        imageView.setImageUrl(image.getLink());
        if (image.getTitle() != null && !image.getTitle().equals("")) {
            title.setVisibility(View.VISIBLE);
            title.setText(image.getTitle());
        }
        if (image.getDescription() != null && !image.getDescription().equals("")) {
            description.setVisibility(View.VISIBLE);
            description.setText(image.getDescription());
        }
            //drawableManager.fetchDrawableOnThread(((GalleryImage)object).getLink(), imageView);
        return albumImageView;

    }
}
