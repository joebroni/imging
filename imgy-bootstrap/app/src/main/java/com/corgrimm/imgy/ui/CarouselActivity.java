

package com.corgrimm.imgy.ui;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.util.Log;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.R.id;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.models.GalleryAlbum;
import com.corgrimm.imgy.models.GalleryImage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.viewpagerindicator.TitlePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.inject.InjectView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Activity to view the carousel and view pager indicator with fragments.
 */
public class CarouselActivity extends RoboSherlockFragmentActivity {

//    @InjectView(id.tpi_header) private TitlePageIndicator indicator;
//    @InjectView(id.vp_pages) private ViewPager pager;

    @Inject protected ObjectMapper objectMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.carousel_view);

//        pager.setAdapter(new BootstrapPagerAdapter(getResources(), getSupportFragmentManager()));
//
//        indicator.setViewPager(pager);
//        pager.setCurrentItem(1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case id.timer:
//                final Intent i = new Intent(this, BootstrapTimerActivity.class);
//                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
