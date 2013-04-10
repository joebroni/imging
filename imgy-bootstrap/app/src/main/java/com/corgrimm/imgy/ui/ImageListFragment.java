package com.corgrimm.imgy.ui;

import static com.corgrimm.imgy.core.Constants.Extra.*;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ListView;

import com.corgrimm.imgy.BootstrapServiceProvider;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.core.News;
import com.corgrimm.imgy.models.Album;
import com.corgrimm.imgy.models.AlbumImage;
import com.corgrimm.imgy.models.GalleryAlbum;
import com.corgrimm.imgy.models.GalleryImage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageListFragment extends RoboFragment {

    @InjectView(R.id.gridview) GridView grid;

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected ObjectMapper objectMapper;

    ArrayList<Object> gallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate( R.layout.news, container, false );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImgyApi.getMainGallery(getActivity(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                gallery = new ArrayList<Object>();
                try {
                    JSONArray jData = response.getJSONArray("data");
                    int i = 0;
                    while (i < jData.length()) {
                        if (jData.getJSONObject(i).getBoolean("is_album")) {
                            GalleryAlbum album = objectMapper.readValue(String.valueOf(jData.getJSONObject(i)), GalleryAlbum.class);
                            gallery.add(album);
                        } else {
                            GalleryImage image = objectMapper.readValue(String.valueOf(jData.getJSONObject(i)), GalleryImage.class);
                            gallery.add(image);
                        }
                        i++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("IMGY", "Gallery Size is " + Integer.toString(gallery.size()));
                grid.setAdapter(new ImageAdapter(getActivity(), gallery));
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
            }

            @Override
            public void onFailure(Throwable e, JSONArray errorResponse) {
                super.onFailure(e, errorResponse);
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (gallery.get(i).getClass() == GalleryImage.class) {
                    startActivity(new Intent(getActivity(), ImageActivity.class).putExtra(IMAGE, (GalleryImage)gallery.get(i)));
                }
                else if (gallery.get(i).getClass() == GalleryAlbum.class) {
                    startActivity(new Intent(getActivity(), AlbumActivity.class).putExtra(ALBUM, (GalleryAlbum)gallery.get(i)));
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
