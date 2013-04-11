package com.corgrimm.imgy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.models.Album;
import com.corgrimm.imgy.models.Comment;
import com.corgrimm.imgy.models.GalleryAlbum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.slidingmenu.lib.SlidingMenu;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.io.IOException;
import java.util.List;

import static com.corgrimm.imgy.core.Constants.Extra.ALBUM;
import static com.corgrimm.imgy.core.Constants.Extra.COMMENTS;

public class AlbumActivity extends BootstrapActivity {

    @Inject protected ObjectMapper objectMapper;

    @InjectView(R.id.albumList) protected ListView albumList;

    @InjectExtra(ALBUM) protected GalleryAlbum gAlbum;

    protected  SlidingMenu menu;
    protected int vote_status;
    List<Comment> comments;
    ListView menuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.album);

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadowright);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slider_menu_list);
        menuList = (ListView) findViewById(R.id.menu_list);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImgyApi.getAlbumImageInfo(AlbumActivity.this, gAlbum.getId(), gAlbum.getCover(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);

                try {
                    Album albumFull = objectMapper.readValue(String.valueOf(response.getJSONObject("data")), Album.class);
                    albumList.setAdapter(new AlbumImageAdapter(AlbumActivity.this, albumFull.getImages()));
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

        ImgyApi.getAlbumComments(AlbumActivity.this, gAlbum.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                try {
                    JSONArray jData = response.getJSONArray("data");
                    try {
                        comments = objectMapper.readValue(String.valueOf(jData), new TypeReference<List<Comment>>() {
                        });
                        menuList.setAdapter(new CommentAdapter(AlbumActivity.this, comments));
                        Log.d("IMGY", "Comments count: " + Integer.toString(comments.size()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
            }
        });

        setListeners();
    }

    private void setListeners() {
//        upvote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (vote_status == UPVOTE) {
//                    upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_white_256));
//                    vote_status = NO_VOTE;
//                }
//                else {
//                    if (vote_status == DOWNVOTE) {
//                        downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_white_256));
//                    }
//                    upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_green_256));
//                    vote_status = UPVOTE;
//                }
//            }
//        });
//
//        downvote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (vote_status == DOWNVOTE) {
//                    downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_white_256));
//                    vote_status = NO_VOTE;
//                }
//                else {
//                    if (vote_status == UPVOTE) {
//                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_white_256));
//                    }
//                    downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_red_256));
//                    vote_status = DOWNVOTE;
//                }
//            }
//        });

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Comment comment = comments.get(i);
                if (comment.getChildren().size() > 0) {
                    startActivity(new Intent(AlbumActivity.this, CommentsActivity.class).putExtra(COMMENTS, comment));
                }
            }
        });
    }


}
