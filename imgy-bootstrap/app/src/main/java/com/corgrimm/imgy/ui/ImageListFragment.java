package com.corgrimm.imgy.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.corgrimm.imgy.BootstrapServiceProvider;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.models.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.corgrimm.imgy.core.Constants.Extra.*;
import static com.corgrimm.imgy.core.Constants.Oauth.*;

public class ImageListFragment extends RoboFragment {

    @InjectView(R.id.gridview) GridView grid;
    @InjectView(R.id.loading) ProgressBar loading;

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected ObjectMapper objectMapper;

    ArrayList<Object> gallery;
    ArrayList<ImageIdUrl> imageLinks;
    SlidingMenu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate( R.layout.image_list_fragment, container, false );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshGrid();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void refreshGrid() {
        grid.setAdapter(null);
        loading.setVisibility(View.VISIBLE);
        imageLinks = new ArrayList<ImageIdUrl>();
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
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (imageLinks.size() >= gallery.size() - 1) {
                                    grid.setAdapter(new ImageAdapter(getActivity(), imageLinks));
                                    loading.setVisibility(View.GONE);
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
                }
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
//                if (gallery.get(i).getClass() == GalleryImage.class) {
//                    startActivity(new Intent(getActivity(), ImageActivity.class).putExtra(IMAGE, (GalleryImage)gallery.get(i)));
//                }
//                else if (gallery.get(i).getClass() == GalleryAlbum.class) {
//                    startActivity(new Intent(getActivity(), AlbumActivity.class).putExtra(ALBUM, (GalleryAlbum)gallery.get(i)));
//                }

            }
        });
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
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
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
                }

                @Override
                public void onFailure(Throwable e, JSONArray errorResponse) {
                    super.onFailure(e, errorResponse);
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
            ImgyApi.getAccessTokenFromRefresh(getActivity(), new JsonHttpResponseHandler() {
                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                }

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    ImgyApi.saveAuthToken(getActivity(), response);
                    getMyImages();
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    super.onFailure(error, content);
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
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (imageLinks.size() == gallery.size()) {
                                    grid.setAdapter(new ImageAdapter(getActivity(), imageLinks));
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

                    grid.setAdapter(new ImageAdapter(getActivity(), imageLinks));
                    loading.setVisibility(View.GONE);
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
            ImgyApi.getAccessTokenFromRefresh(getActivity(), new JsonHttpResponseHandler() {
                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
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
