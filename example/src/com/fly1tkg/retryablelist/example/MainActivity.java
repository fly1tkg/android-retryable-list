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

package com.fly1tkg.retryablelist.example;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import com.fly1tkg.retryablelist.RetryableListView;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<List<String>> {
    private List<String> mItems;
    private ArrayAdapter<String> mAdapter;
    private RetryableListView mListView;
    private int mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (RetryableListView) findViewById(R.id.listView);
        mItems = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);
        mListView.setAdapter(mAdapter);
        mListView.onLoadStart();
    }

    public void onEmptyButtonClick(View v) {
        mState = 0;
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    public void onNormalButtonClick(View v) {
        mState = 1;
        getSupportLoaderManager().restartLoader(1, null, this);
    }

    public void onErrorButtonClick(View v) {
        mState = 2;
        getSupportLoaderManager().restartLoader(2, null, this);
    }

    static class ListLoader extends AsyncTaskLoader<List<String>> {

        int state;

        public ListLoader(Context context, int state) {
            super(context);
            this.state = state;
        }

        @Override
        public List<String> loadInBackground() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            List<String> listitems = new ArrayList<String>();
            switch (state) {
                case 0:
                    break;
                case 1:
                    for (int i = 1; i <= 20; i++) {
                        listitems.add("item" + i);
                    }
                    break;
                case 2:
                    break;

                default:
                    break;
            }
            return listitems;
        }
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        mListView.onLoadStart();
        ListLoader appLoader = new ListLoader(this, id);
        appLoader.forceLoad();
        return appLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<String>> arg0, List<String> arg1) {
        mItems = arg1;
        mAdapter.clear();
        for (String item : mItems) {
            mAdapter.add(item);
        }
        mAdapter.notifyDataSetChanged();
        if (mState == 2) {
            mListView.onLoadError(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNormalButtonClick(null);
                }
            });
        } else {
            mListView.onLoadFinish();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> arg0) {}

}
