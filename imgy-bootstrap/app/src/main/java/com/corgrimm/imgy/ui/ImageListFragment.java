package com.corgrimm.imgy.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.corgrimm.imgy.BootstrapServiceProvider;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.core.Constants;
import com.corgrimm.imgy.models.*;
import com.devspark.appmsg.AppMsg;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flurry.android.FlurryAgent;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.slidingmenu.lib.SlidingMenu;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.corgrimm.imgy.core.Constants.Extra.*;
import static com.corgrimm.imgy.core.Constants.Oauth.*;
import static com.corgrimm.imgy.core.Constants.Prefs.SUBREDDIT;

public class ImageListFragment extends RoboFragment {

    @InjectView(R.id.gridview) GridView grid;
    @InjectView(R.id.loading) ProgressBar loading;
    @InjectView(R.id.sorry) TextView sorry;

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected ObjectMapper objectMapper;
    @Inject SharedPreferences sharedPrefs;

    ArrayList<Object> gallery;
    ArrayList<ImageIdUrl> imageLinks;
    SlidingMenu menu;
    String subreddit;

    public static HashMap<String, String> voteMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate( R.layout.image_list_fragment, container, false );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshGrid();

        voteMap = new HashMap<String, String>();

    }

    @Override
    public void onResume() {
        super.onResume();

        FlurryAgent.logEvent(Constants.Flurry.VIEW_GALLERY);

        Iterator it = voteMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();

            GalleryAlbum album;
            GalleryImage image;
            for (Object o : gallery) {
                if (o.getClass() == GalleryAlbum.class) {
                    album = (GalleryAlbum) o;
                    if (album.getId().equals(pairs.getKey())) {
                        album.setVote(pairs.getValue().toString());
                    }
                }
                else if (o.getClass() == GalleryImage.class) {
                    image = (GalleryImage) o;
                    if (image.getId().equals(pairs.getKey())) {
                        image.setVote(pairs.getValue().toString());
                    }
                }
            }

            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void refreshGrid() {
        sorry.setVisibility(View.GONE);
        grid.setAdapter(null);
        loading.setVisibility(View.VISIBLE);
        imageLinks = new ArrayList<ImageIdUrl>();

        subreddit = sharedPrefs.getString(SUBREDDIT, null);

        if (subreddit != null) {
            ImgyApi.getSubredditGallery(getActivity(), subreddit, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    gallery = new ArrayList<Object>();
                    try {
                        JSONArray jData = response.getJSONArray("data");
                        int i = 0;
                        while (i < jData.length()) {
                            SubredditImage image = objectMapper.readValue(String.valueOf(jData.getJSONObject(i)), SubredditImage.class);
                            gallery.add(image);
                            i++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    }

                    Log.d("IMGY", "Gallery Size is " + Integer.toString(gallery.size()));

                    for (Object o : gallery) {
                        if (o.getClass() == SubredditImage.class) {
                            SubredditImage image = (SubredditImage) o;
                            imageLinks.add(new ImageIdUrl(image.getId(), image.getLink()));
                        }
                    }

                    grid.setAdapter(new ImageAdapter(getActivity(), imageLinks));
                    loading.setVisibility(View.GONE);

                    if (gallery.size() == 0)
                        sorry.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }

                @Override
                public void onFailure(Throwable e, JSONArray errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }
            });

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    FlurryAgent.logEvent(Constants.Flurry.GALLERY_ITEM_ClICK);
                    ImageIdUrl imageIdUrl = imageLinks.get(i);
                    for (Object o : gallery) {
                        if (o.getClass() == SubredditImage.class) {
                            SubredditImage image = (SubredditImage) o;
                            if (imageIdUrl.getId().equals(image.getId())) {
                                startActivity(new Intent(getActivity(), ImageActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, gallery.indexOf(o)));
                                break;
                            }
                        }
                    }
                }
            });
        }
        else {
            ImgyApi.getMainGallery(getActivity(), new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    processGallery(response);
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }

                @Override
                public void onFailure(Throwable e, JSONArray errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }
            });

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ImageIdUrl imageIdUrl = imageLinks.get(i);
                    for (Object o : gallery) {
                        if (o.getClass() == GalleryImage.class) {
                            GalleryImage image = (GalleryImage) o;
                            if (imageIdUrl.getId().equals(image.getId())) {
                                startActivity(new Intent(getActivity(), ImageActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, gallery.indexOf(o)));
                                break;
                            }
                        }
                        else {
                            GalleryAlbum album = (GalleryAlbum) o;
                            if (imageIdUrl.getId().equals(album.getId())) {
                                startActivity(new Intent(getActivity(), AlbumActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, gallery.indexOf(o)));
                                break;
                            }
                        }

                    }
                }
            });
        }


    }

    private void processGallery(JSONObject response) {
        gallery = new ArrayList<Object>();
        try {
            JSONArray jData = response.getJSONArray("data");
            int i = 0;
            while (i < jData.length()) {
                if (jData.getJSONObject(i).getBoolean("is_album")) {
                    GalleryAlbum album = objectMapper.readValue(String.valueOf(jData.getJSONObject(i)), GalleryAlbum.class);
                    if (album.getPrivacy().equals("public")) {
                        gallery.add(album);
                    }
                } else {
                    GalleryImage image = objectMapper.readValue(String.valueOf(jData.getJSONObject(i)), GalleryImage.class);
                    gallery.add(image);
                }
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
        } catch (JsonMappingException e) {
            e.printStackTrace();
            AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
        } catch (JsonParseException e) {
            e.printStackTrace();
            AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
        } catch (IOException e) {
            e.printStackTrace();
            AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
        }

        Log.d("IMGY", "Gallery Size is " + Integer.toString(gallery.size()));

        for (Object o : gallery) {
            final int index = gallery.indexOf(o);
            if (o.getClass() == GalleryImage.class) {
                GalleryImage image = (GalleryImage) o;
                imageLinks.add(new ImageIdUrl(image.getId(), image.getLink()));
            }
            else {
                final GalleryAlbum album = (GalleryAlbum) o;
                ImgyApi.getAlbumImageInfo(getActivity(), album.getId(), album.getCover(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        super.onSuccess(response);

                        try {
                            Album albumFull = objectMapper.readValue(String.valueOf(response.getJSONObject("data")), Album.class);
                            for (AlbumImage image : albumFull.getImages()) {
                                if (image.getId().equals(albumFull.getCover())) {
                                    //imageView.setImageUrl(image.getLink());
                                    if (index > imageLinks.size()) {
                                        imageLinks.add(new ImageIdUrl(album.getId(), image.getLink()));
                                    }
                                    else {
                                        if (index == 0) {
                                            imageLinks.add(0, new ImageIdUrl(album.getId(), image.getLink()));
                                        }
                                        else {
                                            imageLinks.add(index-1, new ImageIdUrl(album.getId(), image.getLink()));
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                        }

                        if (imageLinks.size() >= gallery.size() - 1) {
                            grid.setAdapter(new ImageAdapter(getActivity(), imageLinks));
                            loading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject errorResponse) {
                        super.onFailure(e, errorResponse);
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        super.onFailure(error, content);
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    }
                });
            }
        }
    }

    public void getMyImages() {
        grid.setAdapter(null);
        imageLinks = new ArrayList<ImageIdUrl>();
        loading.setVisibility(View.VISIBLE);
        int tokenStatus = ImgyApi.checkForValidAuthToken(getActivity());
        if ( tokenStatus == TOKEN_VALID) {
            ImgyApi.getMyImages(getActivity(), new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    gallery = new ArrayList<Object>();
                    try {
                        JSONArray jData = response.getJSONArray("data");
                        int i = 0;
                        while (i < jData.length()) {
                            GalleryImage image = objectMapper.readValue(String.valueOf(jData.getJSONObject(i)), GalleryImage.class);
                            gallery.add(image);
                            i++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    }

                    Log.d("IMGY", "Gallery Size is " + Integer.toString(gallery.size()));

                    for (Object o : gallery) {
                        if (o.getClass() == GalleryImage.class) {
                            GalleryImage image = (GalleryImage) o;
                            imageLinks.add(new ImageIdUrl(image.getId(), image.getLink()));
                        }
                    }

                    grid.setAdapter(new ImageAdapter(getActivity(), imageLinks));
                    loading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }

                @Override
                public void onFailure(Throwable e, JSONArray errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }
            });

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ImageIdUrl imageIdUrl = imageLinks.get(i);
                    for (Object o : gallery) {
                        if (o.getClass() == GalleryImage.class) {
                            GalleryImage image = (GalleryImage) o;
                            if (imageIdUrl.getId().equals(image.getId())) {
                                startActivity(new Intent(getActivity(), ImageActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, gallery.indexOf(o)));
                                break;
                            }
                        }
                    }
                }
            });
        }
        else if (tokenStatus == EXPIRED_TOKEN) {
            FlurryAgent.logEvent(Constants.Flurry.TOKEN_REFRESH_CALLED);
            ImgyApi.getAccessTokenFromRefresh(getActivity(), new JsonHttpResponseHandler() {
                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    FlurryAgent.logEvent(Constants.Flurry.TOKEN_REFRESH_FAILED);
                }

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    ImgyApi.saveAuthToken(getActivity(), response);
                    getMyImages();
                }
            });
        }
        else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(
                    Uri.parse(String.format("https://api.imgur.com/oauth2/authorize?client_id=%s&response_type=%s&state=%s", IMGUR_CLIENT_ID, "code", "useless")));
            startActivity(intent);
        }
    }


    public void getMyAlbums() {
        grid.setAdapter(null);
        imageLinks = new ArrayList<ImageIdUrl>();
        loading.setVisibility(View.VISIBLE);
        int tokenStatus = ImgyApi.checkForValidAuthToken(getActivity());
        if ( tokenStatus == TOKEN_VALID) {
            ImgyApi.getMyAlbums(getActivity(), new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    gallery = new ArrayList<Object>();
                    try {
                        JSONArray jData = response.getJSONArray("data");
                        int i = 0;
                        while (i < jData.length()) {
                            GalleryAlbum album = objectMapper.readValue(String.valueOf(jData.getJSONObject(i)), GalleryAlbum.class);
                            gallery.add(album);
                            i++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    }

                    Log.d("IMGY", "Gallery Size is " + Integer.toString(gallery.size()));

                    for (Object o : gallery) {
                        final GalleryAlbum album = (GalleryAlbum) o;
                        ImgyApi.getAlbumImageInfo(getActivity(), album.getId(), album.getCover(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                super.onSuccess(response);

                                try {
                                    Album albumFull = objectMapper.readValue(String.valueOf(response.getJSONObject("data")), Album.class);
                                    for (AlbumImage image : albumFull.getImages()) {
                                        if (image.getId().equals(albumFull.getCover())) {
                                            //imageView.setImageUrl(image.getLink());
                                            imageLinks.add(new ImageIdUrl(album.getId(), image.getLink()));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                                }

                                if (imageLinks.size() == gallery.size()) {
                                    grid.setAdapter(new ImageAdapter(getActivity(), imageLinks));
                                }
                            }

                            @Override
                            public void onFailure(Throwable e, JSONObject errorResponse) {
                                super.onFailure(e, errorResponse);
                                AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                            }

                            @Override
                            public void onFailure(Throwable error, String content) {
                                super.onFailure(error, content);
                                AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                            }
                        });
                    }

                    grid.setAdapter(new ImageAdapter(getActivity(), imageLinks));
                    loading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }

                @Override
                public void onFailure(Throwable e, JSONArray errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }
            });

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ImageIdUrl imageIdUrl = imageLinks.get(i);
                    for (Object o : gallery) {
                        GalleryAlbum album = (GalleryAlbum) o;
                        if (imageIdUrl.getId().equals(album.getId())) {
                            startActivity(new Intent(getActivity(), AlbumActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, gallery.indexOf(o)));
                            break;
                        }
                    }
                }
            });
        }
        else if (tokenStatus == EXPIRED_TOKEN) {
            FlurryAgent.logEvent(Constants.Flurry.TOKEN_REFRESH_CALLED);
            ImgyApi.getAccessTokenFromRefresh(getActivity(), new JsonHttpResponseHandler() {
                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(getActivity(), getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    FlurryAgent.logEvent(Constants.Flurry.TOKEN_REFRESH_FAILED);
                }

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    ImgyApi.saveAuthToken(getActivity(), response);
                    getMyAlbums();
                }
            });
        }
        else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(
                    Uri.parse(String.format("https://api.imgur.com/oauth2/authorize?client_id=%s&response_type=%s&state=%s", IMGUR_CLIENT_ID, "code", "useless")));
            startActivity(intent);
        }
    }
}
