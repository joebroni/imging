package com.corgrimm.imgy.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.core.AvatarLoader;
import com.corgrimm.imgy.models.Comment;
import com.corgrimm.imgy.models.GalleryAlbum;
import com.corgrimm.imgy.models.GalleryImage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.image.SmartImageView;
import com.slidingmenu.lib.SlidingMenu;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.io.IOException;
import java.util.List;

import static com.corgrimm.imgy.core.Constants.Extra.IMAGE;
import static com.corgrimm.imgy.core.Constants.Vote.*;

public class ImageActivity extends BootstrapActivity {

    @Inject protected ObjectMapper objectMapper;

    @InjectView(R.id.imgImage) protected SmartImageView image;
    @InjectView(R.id.gifView) protected WebView gifView;

    @InjectView(R.id.caption) protected TextView caption;
    @InjectView(R.id.upvote) protected ImageButton upvote;
    @InjectView(R.id.downvote) protected ImageButton downvote;

    @InjectExtra(IMAGE) protected GalleryImage gImage;

    protected  SlidingMenu menu;
    ListView menuList;
    protected int vote_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image);

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

        if (gImage.getAnimated()) {
            image.setVisibility(View.GONE);
            gifView.setVisibility(View.VISIBLE);
            gifView.getSettings().setUseWideViewPort(true);
            gifView.loadUrl(gImage.getLink());
            gifView.setBackgroundColor(Color.parseColor("#333333"));
        }
        else {
            image.setImageUrl(gImage.getLink());
        }

        caption.setText(gImage.getTitle());

        ImgyApi.getImageComments(ImageActivity.this, gImage.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                try {
                    JSONArray jData = response.getJSONArray("data");
                    try {
                        List<Comment> comments = objectMapper.readValue(String.valueOf(jData), new TypeReference<List<Comment>>() { });
                        menuList.setAdapter(new CommentAdapter(ImageActivity.this, comments));
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
        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vote_status == UPVOTE) {
                    upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_white_256));
                    vote_status = NO_VOTE;
                }
                else {
                    if (vote_status == DOWNVOTE) {
                        downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_white_256));
                    }
                    upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_green_256));
                    vote_status = UPVOTE;
                }
            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vote_status == DOWNVOTE) {
                    downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_white_256));
                    vote_status = NO_VOTE;
                }
                else {
                    if (vote_status == UPVOTE) {
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_white_256));
                    }
                    downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_red_256));
                    vote_status = DOWNVOTE;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.imgy_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.comments:
                menu.toggle(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
