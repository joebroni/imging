

package com.corgrimm.imgy.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.slidingmenu.lib.SlidingMenu;
import org.json.JSONObject;

import static com.corgrimm.imgy.core.Constants.Prefs.*;

/**
 * Activity to view the carousel and view pager indicator with fragments.
 */
public class ContentActivity extends RoboSherlockFragmentActivity {

    @Inject protected ObjectMapper objectMapper;
    @Inject SharedPreferences sharedPrefs;

    protected ToggleButton viral;
    protected ToggleButton user;
    protected ToggleButton score;
    protected ToggleButton newest;
    protected ToggleButton popular;

    protected Button myImages;
    protected Button myAlbums;
    protected Button upload;

    ImageListFragment gridFragment;

    SlidingMenu menu;
    SlidingMenu filterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        Crashlytics.start(this);

        setContentView(R.layout.content_view);

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.home_menu);

        filterMenu = new SlidingMenu(this);
        filterMenu.setMode(SlidingMenu.RIGHT);
        filterMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        filterMenu.setShadowWidthRes(R.dimen.shadow_width);
        filterMenu.setShadowDrawable(R.drawable.shadowright);
        filterMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        filterMenu.setFadeDegree(0.35f);
        filterMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        filterMenu.setMenu(R.layout.filter_menu);

        gridFragment = (ImageListFragment) getSupportFragmentManager().findFragmentById(R.id.image_list_fragment);

        viral = (ToggleButton) findViewById(R.id.viral);
        user = (ToggleButton) findViewById(R.id.user);
        score = (ToggleButton) findViewById(R.id.score);
        newest = (ToggleButton) findViewById(R.id.newest);
        popular = (ToggleButton) findViewById(R.id.popular);

        myImages = (Button) findViewById(R.id.my_images);
        myAlbums = (Button) findViewById(R.id.my_albums);
        upload = (Button) findViewById(R.id.upload);

        setupActionBar();

        setListeners();

        String collection = sharedPrefs.getString(COLLECTION, VIRAL);
        String filter = sharedPrefs.getString(FILTER, POPULAR);

        if (collection.equals(VIRAL)) {
            viral.setChecked(true);
        }
        else if (collection.equals(USER)) {
            user.setChecked(true);
        }
        else if (collection.equals(SCORE)) {
            score.setChecked(true);
        }

        if (filter.equals(NEWEST)) {
            newest.setChecked(true);
        }
        else if (filter.equals(POPULAR)) {
            popular.setChecked(true);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // extract the OAUTH access token if it exists
        Intent intent = this.getIntent();
        Uri uri = this.getIntent().getData();
        if(uri != null) {
            String blessed_request_code = uri.getQueryParameter("code");
            String blessed_request_secret = uri.getQueryParameter("oauth_token_secret");
            // See step 6
            ImgyApi.getAccessTokenFromCode(ContentActivity.this, blessed_request_code, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    ImgyApi.saveAuthToken(ContentActivity.this, response);
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                }
            });
        }
    }

    private void setupActionBar() {

        // Change the home icon to the three little lines
        getSupportActionBar().setIcon(R.drawable.ic_up_menu);

        // Make sure home button can be clicked on.
        getSupportActionBar().setHomeButtonEnabled(true);

        // Show the screen title?
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Show the little arrow next to the home icon in the top left
        // to let users know something is over there.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void setListeners() {

        viral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viral.isChecked()) {
                    user.setChecked(false);
                    score.setChecked(false);
                    sharedPrefs.edit()
                            .putString(COLLECTION, VIRAL)
                            .commit();
                    gridFragment.refreshGrid();
                }
                else {
                    viral.setChecked(true);
//                    sharedPrefs.edit()
//                            .putString(COLLECTION, VIRAL)
//                            .commit();
//                    gridFragment.refreshGrid();
                }
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isChecked()) {
                    viral.setChecked(false);
                    score.setChecked(false);
                    sharedPrefs.edit()
                            .putString(COLLECTION, USER)
                            .commit();
                    gridFragment.refreshGrid();
                }
                else {
                    viral.setChecked(true);
                    sharedPrefs.edit()
                            .putString(COLLECTION, VIRAL)
                            .commit();
                    gridFragment.refreshGrid();
                }
            }
        });

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (score.isChecked()) {
                    user.setChecked(false);
                    viral.setChecked(false);
                    sharedPrefs.edit()
                            .putString(COLLECTION, SCORE)
                            .commit();
                    gridFragment.refreshGrid();
                }
                else {
                    viral.setChecked(true);
                    sharedPrefs.edit()
                            .putString(COLLECTION, VIRAL)
                            .commit();
                    gridFragment.refreshGrid();
                }
            }
        });

        newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newest.isChecked()) {
                    popular.setChecked(false);
                    sharedPrefs.edit()
                            .putString(FILTER, NEWEST)
                            .commit();
                    gridFragment.refreshGrid();
                }
                else {
                    newest.setChecked(true);
//                    sharedPrefs.edit()
//                            .putString(FILTER, NEWEST)
//                            .commit();
//                    gridFragment.refreshGrid();
                }
            }
        });

        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popular.isChecked()) {
                    newest.setChecked(false);
                    sharedPrefs.edit()
                            .putString(FILTER, POPULAR)
                            .commit();
                    gridFragment.refreshGrid();
                }
                else {
                    newest.setChecked(true);
                    sharedPrefs.edit()
                            .putString(FILTER, NEWEST)
                            .commit();
                    gridFragment.refreshGrid();
                }
            }
        });

        myImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridFragment.getMyImages();
            }
        });

        myAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridFragment.getMyAlbums();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Crashlytics.getInstance().crash();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.imgy_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                menu.toggle(true);
                return true;
            case R.id.filter:
                filterMenu.toggle(true);
                return true;
            case R.id.refresh:
                gridFragment.refreshGrid();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
