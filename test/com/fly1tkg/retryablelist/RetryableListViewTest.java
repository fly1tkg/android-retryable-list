/**
 * Copyright (C) 2014 Shoichi Takagi <fly1tkg@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.fly1tkg.retryablelist;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.fly1tkg.retryablelist.RetryableListView.State;

@RunWith(RobolectricTestRunner.class)
public class RetryableListViewTest {
    private RetryableListView mRetryableListView;

    @Before
    public void setUp() throws Exception {
        Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        mRetryableListView = new RetryableListView(activity);
    }

    @Test
    public void onLoadStartTest() throws Exception {
        mRetryableListView.onLoadStart();
        assertThat(mRetryableListView.getListView()).isGone();
        assertThat(mRetryableListView.getErrorImageView()).isGone();
        assertThat(mRetryableListView.getErrorMessageTextView()).isGone();
        assertThat(mRetryableListView.getRetryButton()).isGone();
        assertThat(mRetryableListView.getProgressBar()).isVisible();
        assertEquals(State.Loading, mRetryableListView.getState());
    }

    @Test
    public void emptyTest() throws Exception {
        mRetryableListView.onLoadFinish();
        assertThat(mRetryableListView.getListView()).isGone();
        assertThat(mRetryableListView.getErrorImageView()).isGone();
        assertThat(mRetryableListView.getErrorMessageTextView()).isVisible();
        assertThat(mRetryableListView.getRetryButton()).isGone();
        assertThat(mRetryableListView.getProgressBar()).isGone();
        assertEquals(State.Empty, mRetryableListView.getState());
    }

    boolean isRetryClicked = false;

    @Test
    public void onLoadErrorTest() throws Exception {
        mRetryableListView.onLoadError(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isRetryClicked = true;
            }
        });
        assertThat(mRetryableListView.getListView()).isGone();
        assertThat(mRetryableListView.getErrorImageView()).isVisible();
        assertThat(mRetryableListView.getErrorMessageTextView()).isVisible();
        assertThat(mRetryableListView.getRetryButton()).isVisible();
        assertThat(mRetryableListView.getProgressBar()).isGone();
        assertEquals(State.Error, mRetryableListView.getState());
        mRetryableListView.getRetryButton().performClick();
        assertEquals(true, isRetryClicked);
    }

}
