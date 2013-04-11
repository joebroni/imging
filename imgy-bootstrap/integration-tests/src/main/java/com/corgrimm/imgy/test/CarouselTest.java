

package com.corgrimm.imgy.test;

import android.test.ActivityInstrumentationTestCase2;

import com.corgrimm.imgy.ui.ContentActivity;


/**
 * Test displaying of carousel.
 */
public class CarouselTest extends ActivityInstrumentationTestCase2<ContentActivity> {

    /**
     * Create test for {@link com.corgrimm.imgy.ui.ContentActivity}
     */
    public CarouselTest() {
        super(ContentActivity.class);
    }

    /**
     * Verify activity exists
     */
    public void testActivityExists() {
        assertNotNull(getActivity());
    }
}
