

package com.corgrimm.imgy.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.core.Constants;
import com.crashlytics.android.Crashlytics;
import com.devspark.appmsg.AppMsg;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flurry.android.FlurryAgent;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.slidingmenu.lib.SlidingMenu;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.corgrimm.imgy.core.Constants.Prefs.*;
import static com.corgrimm.imgy.core.Constants.Intent.*;

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
    protected EditText subreddit;

    protected Button myImages;
    protected Button myAlbums;
    protected Button upload;

    File imageFile;

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
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.home_menu);

        filterMenu = new SlidingMenu(this);
        filterMenu.setMode(SlidingMenu.RIGHT);
        filterMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
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
        subreddit = (EditText) findViewById(R.id.subreddit);

        myImages = (Button) findViewById(R.id.my_images);
        myAlbums = (Button) findViewById(R.id.my_albums);
        upload = (Button) findViewById(R.id.upload);

        setupActionBar();

        setListeners();

        String collection = sharedPrefs.getString(COLLECTION, VIRAL);
        String filter = sharedPrefs.getString(FILTER, POPULAR);
        String subr = sharedPrefs.getString(SUBREDDIT, null);



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

        if (subr != null) {
            subreddit.setText(subr);
            viral.setChecked(false);
            user.setChecked(false);
            score.setChecked(false);
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
                    AppMsg.makeText(ContentActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                }
            });
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FlurryAgent.onStartSession(this, Constants.Flurry.FLURRY_API_KEY);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        FlurryAgent.onEndSession(this);
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
                FlurryAgent.logEvent(Constants.Flurry.VIRAL_CLICKED);
                sharedPrefs.edit()
                        .putString(SUBREDDIT, null)
                        .commit();
                subreddit.setText("");
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
                filterMenu.toggle(true);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlurryAgent.logEvent(Constants.Flurry.USER_CLICKED);
                sharedPrefs.edit()
                        .putString(SUBREDDIT, null)
                        .commit();
                subreddit.setText("");
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
                filterMenu.toggle(true);
            }
        });

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlurryAgent.logEvent(Constants.Flurry.SCORE_CLICKED);
                sharedPrefs.edit()
                        .putString(SUBREDDIT, null)
                        .commit();
                subreddit.setText("");
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
                filterMenu.toggle(true);
            }
        });

        newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlurryAgent.logEvent(Constants.Flurry.NEWEST_CLICKED);
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
                filterMenu.toggle(true);
            }
        });

        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlurryAgent.logEvent(Constants.Flurry.POPULAR_CLICKED);
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
                filterMenu.toggle(true);
            }
        });

        myImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridFragment.getMyImages();
                menu.toggle(true);
                FlurryAgent.logEvent(Constants.Flurry.VIEW_MY_IMAGES);
            }
        });

        myAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridFragment.getMyAlbums();
                menu.toggle(true);
                FlurryAgent.logEvent(Constants.Flurry.VIEW_MY_ALBUMS);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                FlurryAgent.logEvent(Constants.Flurry.UPLOAD_IMAGE);
            }
        });

        subreddit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_GO){
                    sharedPrefs.edit()
                            .putString(SUBREDDIT, subreddit.getText().toString())
                            .commit();
                    gridFragment.refreshGrid();
                    filterMenu.toggle(true);
                    viral.setChecked(false);
                    user.setChecked(false);
                    score.setChecked(false);
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("r", subreddit.getText().toString());
                    FlurryAgent.logEvent(Constants.Flurry.SUBREDDIT, articleParams);
                }
                return false;
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
                FlurryAgent.logEvent(Constants.Flurry.FILTER_BUTTON_CLICKED);
                return true;
            case R.id.refresh:
                gridFragment.refreshGrid();
                FlurryAgent.logEvent(Constants.Flurry.REFRESH_GALLERY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap yourSelectedImage = null;
                    try {
                        yourSelectedImage = decodeUri(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        AppMsg.makeText(ContentActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    }

                    imageFile = new File(getCacheDir(), "image");
                    try {
                        imageFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        AppMsg.makeText(ContentActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    }

                    //Convert bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    //write the bytes in file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(imageFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        AppMsg.makeText(ContentActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    }
                    try {
                        if (fos != null)
                            fos.write(bitmapdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                        AppMsg.makeText(ContentActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                    }

                    ImgyApi.postImage(ContentActivity.this, imageFile, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            super.onSuccess(response);
                            AppMsg.makeText(ContentActivity.this, getString(R.string.upload_success), AppMsg.STYLE_INFO).show();
                        }

                        @Override
                        public void onFailure(Throwable e, JSONObject errorResponse) {
                            super.onFailure(e, errorResponse);
                            AppMsg.makeText(ContentActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                            FlurryAgent.logEvent(Constants.Flurry.IMAGE_UPLOAD_FAILURE);
                        }

                        @Override
                        public void onFailure(Throwable error, String content) {
                            super.onFailure(error, content);
                            AppMsg.makeText(ContentActivity.this, getString(R.string.general_error), AppMsg.STYLE_ALERT).show();
                            FlurryAgent.logEvent(Constants.Flurry.IMAGE_UPLOAD_FAILURE);
                        }
                    });
                }

                break;
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 420;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }
}
