package com.corgrimm.imgy.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.models.Album;
import com.corgrimm.imgy.models.AlbumImage;
import com.corgrimm.imgy.models.GalleryAlbum;
import com.corgrimm.imgy.models.GalleryImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.image.SmartImageView;
import com.slidingmenu.lib.SlidingMenu;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.io.IOException;

import static com.corgrimm.imgy.core.Constants.Extra.ALBUM;
import static com.corgrimm.imgy.core.Constants.Extra.IMAGE;
import static com.corgrimm.imgy.core.Constants.Vote.*;

public class AlbumActivity extends BootstrapActivity {

    @Inject protected ObjectMapper objectMapper;

    @InjectView(R.id.albumList) protected ListView albumList;

    @InjectExtra(ALBUM) protected GalleryAlbum gAlbum;

    protected  SlidingMenu menu;
    protected int vote_status;


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
    }


}
