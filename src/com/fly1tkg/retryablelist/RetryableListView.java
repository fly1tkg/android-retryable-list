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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RetryableListView extends LinearLayout {
    private int mLayoutId = R.layout.arl_retryablelistview;
    private int mErrorDrawableId;
    private String mErrorMessage;
    private String mEmptyMessage;
    private State mState;
    private boolean mAlreadyInflated;
    private ListView mListView;
    private ImageView mErrorImageView;
    private TextView mErrorMessageTextView;
    private Button mRetryButton;
    private ProgressBar mProgressBar;

    public enum State {
        Loading, Empty, Loaded, Error
    }

    public RetryableListView(Context context) {
        this(context, null);
    }

    public RetryableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mErrorMessage = context.getString(R.string.arl_error_network);
        mEmptyMessage = context.getString(R.string.arl_empty);
        this.onFinishInflate();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RetryableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mErrorMessage = context.getString(R.string.arl_error_network);
        mEmptyMessage = context.getString(R.string.arl_empty);
        this.onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        if (!mAlreadyInflated) {
            mAlreadyInflated = true;
            inflate(getContext(), mLayoutId, this);
            mListView = (ListView) findViewById(R.id.arl_listView);
            mErrorImageView = (ImageView) findViewById(R.id.arl_error_image);
            mErrorMessageTextView = (TextView) findViewById(R.id.arl_error_message);
            mRetryButton = (Button) findViewById(R.id.arl_retry_button);
            mProgressBar = (ProgressBar) findViewById(R.id.arl_progressBar);
            onLoadStart();
        }
        super.onFinishInflate();
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public void setLayoutId(int mLayoutId) {
        this.mLayoutId = mLayoutId;
    }

    public int getErrorImageResource() {
        return mErrorDrawableId;
    }

    public void setErrorImageResource(int resId) {
        this.mErrorDrawableId = resId;
        mErrorImageView.setImageResource(resId);
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.mErrorMessage = errorMessage;
    }

    public void setErrorMessage(int resId) {
        setErrorMessage(getContext().getString(resId));
    }

    public String getEmptyMessage() {
        return mEmptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.mEmptyMessage = emptyMessage;
    }

    public void setEmptyMessage(int resId) {
        setEmptyMessage(getContext().getString(resId));
    }

    public State getState() {
        return mState;
    }

    public void setState(State mState) {
        this.mState = mState;
    }

    public ListView getListView() {
        return mListView;
    }

    public void setListView(ListView mListView) {
        this.mListView = mListView;
    }

    public ImageView getErrorImageView() {
        return mErrorImageView;
    }

    public void setErrorImageView(ImageView mErrorImageView) {
        this.mErrorImageView = mErrorImageView;
    }

    public TextView getErrorMessageTextView() {
        return mErrorMessageTextView;
    }

    public void setErrorMessageTextView(TextView mErrorMessageTextView) {
        this.mErrorMessageTextView = mErrorMessageTextView;
    }

    public Button getRetryButton() {
        return mRetryButton;
    }

    public void setRetryButton(Button mRetryButton) {
        this.mRetryButton = mRetryButton;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void setProgressBar(ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
    }

    public void onLoadStart() {
        mErrorImageView.setVisibility(GONE);
        mErrorMessageTextView.setVisibility(GONE);
        mRetryButton.setVisibility(GONE);
        mListView.setVisibility(GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mState = State.Loading;
    }

    public void onLoadFinish() {
        mErrorImageView.setVisibility(GONE);
        mRetryButton.setVisibility(GONE);
        mProgressBar.setVisibility(GONE);
        ListAdapter adapter = mListView.getAdapter();
        if (adapter == null || adapter.isEmpty()) {
            mErrorMessageTextView.setText(mEmptyMessage);
            mErrorMessageTextView.setVisibility(VISIBLE);
            mListView.setVisibility(GONE);
            mState = State.Empty;
        } else {
            mErrorMessageTextView.setVisibility(GONE);
            mListView.setVisibility(VISIBLE);
            mState = State.Loaded;
        }
    }

    public void onLoadError(OnClickListener l) {
        mErrorMessageTextView.setText(mErrorMessage);
        mErrorMessageTextView.setVisibility(VISIBLE);
        mErrorImageView.setVisibility(VISIBLE);
        mErrorMessageTextView.setVisibility(VISIBLE);
        mRetryButton.setVisibility(VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mRetryButton.setOnClickListener(l);
        mState = State.Error;
    }

    public void addFooterView(View v) {
        mListView.addFooterView(v);
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        mListView.addFooterView(v, data, isSelectable);
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        mListView.addHeaderView(v, data, isSelectable);
    }

    public void addHeaderView(View v) {
        mListView.addHeaderView(v);
    }

    public boolean areFooterDividersEnabled() {
        return mListView.areFooterDividersEnabled();
    }

    public boolean areHeaderDividersEnabled() {
        return mListView.areHeaderDividersEnabled();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return mListView.dispatchKeyEvent(event);
    }

    public ListAdapter getAdapter() {
        return mListView.getAdapter();
    }

    public long[] getCheckItemIds() {
        return mListView.getCheckedItemIds();
    }

    public Drawable getDivider() {
        return mListView.getDivider();
    }

    public int getDividerHeight() {
        return mListView.getDividerHeight();
    }

    public int getFooterViewsCount() {
        return mListView.getFooterViewsCount();
    }

    public int getHeaderViewsCount() {
        return mListView.getHeaderViewsCount();
    }

    public boolean getItemsCanFocus() {
        return mListView.getItemsCanFocus();
    }

    public int getMaxScrollAmount() {
        return mListView.getMaxScrollAmount();
    }

    public Drawable getOverscrollFooter() {
        return mListView.getOverscrollFooter();
    }

    public Drawable getOverscrollHeader() {
        return mListView.getOverscrollHeader();
    }

    public boolean removeFooterView(View v) {
        return mListView.removeFooterView(v);
    }

    public boolean removeHeaderView(View v) {
        return mListView.removeHeaderView(v);
    }

    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public void setDivider(Drawable divider) {
        mListView.setDivider(divider);
    }

    public void setDividerHeight(int height) {
        mListView.setDividerHeight(height);
    }

    public void setFooterDividersEnabled(boolean footerDividersEnabled) {
        mListView.setFooterDividersEnabled(footerDividersEnabled);
    }

    public void setHeaderDividersEnabled(boolean headerDividersEnabled) {
        mListView.setHeaderDividersEnabled(headerDividersEnabled);
    }

    public void setItemsCanFocus(boolean itemsCanFocus) {
        mListView.setItemsCanFocus(itemsCanFocus);
    }

    public void setOverscrollFooter(Drawable footer) {
        mListView.setOverscrollFooter(footer);
    }

    public void setOverscrollHeader(Drawable header) {
        setOverscrollHeader(header);
    }

    public void setRemoteViewsAdapter(Intent intent) {
        mListView.setRemoteViewsAdapter(intent);
    }

    public void setSelection(int position) {
        mListView.setSelection(position);
    }

    public void setSelectionAfterHeaderView() {
        mListView.setSelectionAfterHeaderView();
    }

    public void setSelectionFromTop(int position, int y) {
        mListView.setSelectionFromTop(position, y);
    }

    public void smoothScrollByOffset(int offset) {
        mListView.smoothScrollByOffset(offset);
    }

    public void smoothScrollToPosition(int position) {
        mListView.smoothScrollToPosition(position);
    }

}
