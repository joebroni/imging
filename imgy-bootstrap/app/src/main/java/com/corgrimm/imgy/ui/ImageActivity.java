package com.corgrimm.imgy.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.models.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import java.util.ArrayList;
import java.util.List;

import static com.corgrimm.imgy.core.Constants.Extra.*;
import static com.corgrimm.imgy.core.Constants.Oauth.EXPIRED_TOKEN;
import static com.corgrimm.imgy.core.Constants.Oauth.IMGUR_CLIENT_ID;
import static com.corgrimm.imgy.core.Constants.Oauth.TOKEN_VALID;
import static com.corgrimm.imgy.core.Constants.Vote.*;

public class ImageActivity extends BootstrapActivity {

    @Inject protected ObjectMapper objectMapper;

    @InjectView(R.id.imgImage) protected SmartImageView image;
    @InjectView(R.id.gifView) protected WebView gifView;

    @InjectView(R.id.caption) protected TextView caption;
    @InjectView(R.id.upvote) protected ImageButton upvote;
    @InjectView(R.id.downvote) protected ImageButton downvote;

    @InjectExtra(GALLERY) protected ArrayList<Object> gallery;
    @InjectExtra(INDEX) protected int index;

    protected  SlidingMenu menu;
    ListView menuList;
    protected int vote_status;
    List<Comment> comments;
    GalleryImage gImage;


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

        gImage = (GalleryImage) gallery.get(index);

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

        if (gImage.getVote() != null) {
            if (gImage.getVote().equals(UP_VOTE_STRING)) {
                upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_green_256));
                vote_status = UPVOTE;
            }
            else if (gImage.getVote().equals(DOWN_VOTE_STRING)) {
                downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_red_256));
                vote_status = DOWNVOTE;
            }
        }

        caption.setText(gImage.getTitle());

        ImgyApi.getImageComments(ImageActivity.this, gImage.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                try {
                    JSONArray jData = response.getJSONArray("data");
                    try {
                        comments = objectMapper.readValue(String.valueOf(jData), new TypeReference<List<Comment>>() { });
                        if (gImage.getAccount_url() != null) {
                            menuList.setAdapter(new CommentAdapter(ImageActivity.this, comments, gImage.getAccount_url()));
                        }
                        else {
                            menuList.setAdapter(new CommentAdapter(ImageActivity.this, comments, ""));
                        }
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
//                if (vote_status == UPVOTE) {
//                    upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_white_256));
//                    vote_status = NO_VOTE;
//                }
//                else {
                    if (vote_status == DOWNVOTE) {
                        downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_white_256));
                    }
                    upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_green_256));
                    vote_status = UPVOTE;
                    voteOnImage(UP_VOTE_STRING);
//                }
            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (vote_status == DOWNVOTE) {
//                    downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_white_256));
//                    vote_status = NO_VOTE;
//                }
//                else {
                    if (vote_status == UPVOTE) {
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_white_256));
                    }
                    downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_red_256));
                    vote_status = DOWNVOTE;
                    voteOnImage(DOWN_VOTE_STRING);
//                }
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
            case R.id.left:
                previous();
                return true;
            case R.id.right:
                next();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void previous() {
        if (index != 0 ) {
            if (gallery.get(index - 1).getClass() == GalleryImage.class) {
                startActivity(new Intent(ImageActivity.this, ImageActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, index - 1).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            else {
                startActivity(new Intent(ImageActivity.this, AlbumActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, index - 1).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
    }

    private void next() {
        if (index != gallery.size() - 1 ) {
            if (gallery.get(index + 1).getClass() == GalleryImage.class) {
                startActivity(new Intent(ImageActivity.this, ImageActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, index + 1).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            else {
                startActivity(new Intent(ImageActivity.this, AlbumActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, index + 1).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
    }

    private void voteOnImage(final String vote) {
        int tokenStatus = ImgyApi.checkForValidAuthToken(this);
        if ( tokenStatus == TOKEN_VALID) {
            ImgyApi.voteForImage(this, gImage.getId(), vote, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);

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
        }
        else if (tokenStatus == EXPIRED_TOKEN) {
            ImgyApi.getAccessTokenFromRefresh(ImageActivity.this, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                }

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    ImgyApi.saveAuthToken(ImageActivity.this, response);
                    voteOnImage(vote);
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
