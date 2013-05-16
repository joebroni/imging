package com.corgrimm.imgy.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.models.Album;
import com.corgrimm.imgy.models.Comment;
import com.corgrimm.imgy.models.GalleryAlbum;
import com.corgrimm.imgy.models.GalleryImage;
import com.devspark.appmsg.AppMsg;
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
import java.util.ArrayList;
import java.util.List;

import static com.corgrimm.imgy.core.Constants.Extra.*;
import static com.corgrimm.imgy.core.Constants.Oauth.EXPIRED_TOKEN;
import static com.corgrimm.imgy.core.Constants.Oauth.IMGUR_CLIENT_ID;
import static com.corgrimm.imgy.core.Constants.Oauth.TOKEN_VALID;
import static com.corgrimm.imgy.core.Constants.Vote.*;

public class AlbumActivity extends BootstrapActivity {

    @Inject protected ObjectMapper objectMapper;

    @InjectView(R.id.albumList) protected ListView albumList;
    @InjectView(R.id.albumTitle) protected TextView albumTitle;
    @InjectView(R.id.upvote) protected ImageButton upvote;
    @InjectView(R.id.downvote) protected ImageButton downvote;
    @InjectView(R.id.downvotes) protected TextView downvotes;
    @InjectView(R.id.upvotes) protected TextView upvotes;
    @InjectView(R.id.author) protected TextView author;

    @InjectExtra(GALLERY) protected ArrayList<Object> gallery;
    @InjectExtra(INDEX) protected int index;


    protected  SlidingMenu menu;
    protected int vote_status;
    List<Comment> comments;
    ListView menuList;
    Album albumFull;
    GalleryAlbum gAlbum;


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

        gAlbum = (GalleryAlbum) gallery.get(index);

        if (gAlbum.getVote() != null) {
            if (gAlbum.getVote().equals(UP_VOTE_STRING)) {
                upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_green_256));
                vote_status = UPVOTE;
            }
            else if (gAlbum.getVote().equals(DOWN_VOTE_STRING)) {
                downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_red_256));
                vote_status = DOWNVOTE;
            }
        }

        ImgyApi.getAlbumImageInfo(AlbumActivity.this, gAlbum.getId(), gAlbum.getCover(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);

                try {
                    albumFull = objectMapper.readValue(String.valueOf(response.getJSONObject("data")), Album.class);
                    albumTitle.setText(albumFull.getTitle());
                    if (gAlbum.getUps() != null)
                        upvotes.setText(gAlbum.getUps().toString());
                    if (gAlbum.getDowns() != null)
                        downvotes.setText(gAlbum.getDowns().toString());
                    author.setText(gAlbum.getAccount_url());
                    albumList.setAdapter(new AlbumImageAdapter(AlbumActivity.this, albumFull.getImages()));
                    getComments();
                } catch (IOException e) {
                    e.printStackTrace();
                    AppMsg.makeText(AlbumActivity.this, getString(R.string.album_error), AppMsg.STYLE_ALERT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppMsg.makeText(AlbumActivity.this, getString(R.string.album_error), AppMsg.STYLE_ALERT).show();
                }
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
                AppMsg.makeText(AlbumActivity.this, getString(R.string.album_error), AppMsg.STYLE_ALERT).show();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                AppMsg.makeText(AlbumActivity.this, getString(R.string.album_error), AppMsg.STYLE_ALERT).show();
            }
        });



        setListeners();
    }

    private void setListeners() {
        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if (vote_status == DOWNVOTE) {
                downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_white_256));
            }
            upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_green_256));
            vote_status = UPVOTE;
            voteOnAlbum(UP_VOTE_STRING);

            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if (vote_status == UPVOTE) {
                upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_white_256));
            }
            downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_red_256));
            vote_status = DOWNVOTE;
            voteOnAlbum(DOWN_VOTE_STRING);

            }
        });

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Comment comment = comments.get(i);
            if (comment.getChildren().size() > 0) {
                startActivity(new Intent(AlbumActivity.this, CommentsActivity.class).putExtra(COMMENTS, comment).putExtra(OP, albumFull.getAccount_url()));
            }
            }
        });
    }

    private void getComments() {
        ImgyApi.getAlbumComments(AlbumActivity.this, gAlbum.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                try {
                    JSONArray jData = response.getJSONArray("data");
                    try {
                        comments = objectMapper.readValue(String.valueOf(jData), new TypeReference<List<Comment>>() {
                        });
                        if (albumFull != null) {
                            if (albumFull.getAccount_url() != null) {
                                menuList.setAdapter(new CommentAdapter(AlbumActivity.this, comments, albumFull.getAccount_url()));
                            }
                            else {
                                menuList.setAdapter(new CommentAdapter(AlbumActivity.this, comments, ""));
                            }
                        }
                        else {
                            AppMsg.makeText(AlbumActivity.this, getString(R.string.album_error), AppMsg.STYLE_ALERT).show();
                        }
                        Log.d("IMGY", "Comments count: " + Integer.toString(comments.size()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        AppMsg.makeText(AlbumActivity.this, getString(R.string.album_error), AppMsg.STYLE_ALERT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppMsg.makeText(AlbumActivity.this, getString(R.string.album_error), AppMsg.STYLE_ALERT).show();
                }

            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
            }
        });
    }

    private void voteOnAlbum(final String vote) {
        int tokenStatus = ImgyApi.checkForValidAuthToken(this);
        if ( tokenStatus == TOKEN_VALID) {
            ImgyApi.voteForAlbum(this, albumFull.getId(), vote, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(AlbumActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }

                @Override
                public void onFailure(Throwable e, JSONArray errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(AlbumActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }
            });
        }
        else if (tokenStatus == EXPIRED_TOKEN) {
            ImgyApi.getAccessTokenFromRefresh(AlbumActivity.this, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    AppMsg.makeText(AlbumActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    ImgyApi.saveAuthToken(AlbumActivity.this, response);
                    voteOnAlbum(vote);
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
                startActivity(new Intent(AlbumActivity.this, ImageActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, index - 1).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            else {
                startActivity(new Intent(AlbumActivity.this, AlbumActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, index - 1).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
    }

    private void next() {
        if (index != gallery.size() - 1 ) {
            if (gallery.get(index + 1).getClass() == GalleryImage.class) {
                startActivity(new Intent(AlbumActivity.this, ImageActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, index + 1).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            else {
                startActivity(new Intent(AlbumActivity.this, AlbumActivity.class).putExtra(GALLERY, gallery).putExtra(INDEX, index + 1).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
    }
}
