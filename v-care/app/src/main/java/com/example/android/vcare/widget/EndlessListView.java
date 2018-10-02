package com.example.android.vcare.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.android.vcare.R;




/*
 * Copyright (C) 2012 Fabian Leon Ortega <http://orleonsoft.blogspot.com/,
 *  http://yelamablog.blogspot.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class EndlessListView extends ListView implements OnScrollListener {

	private static final String TAG = "LoadMoreListView";

	/**
	 * Listener that will receive notifications every time the list scrolls.
	 */
	private OnScrollListener mOnScrollListener;

	private View mProgressBarLoadMore;

	private boolean hasMorePages;

	// Listener to process load more items when user reaches the end of the list
	private OnLoadMoreListener mOnLoadMoreListener;
	// To know if the list is loading more items
	private boolean mIsLoadingMore = false;
	private int mCurrentScrollState;

	public EndlessListView(Context context) {
		super(context);
		init(context);
	}

	public EndlessListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {

		// footer
		View footer = LayoutInflater.from(context).inflate(R.layout.load_more_footer, this, false);
		mProgressBarLoadMore = footer.findViewById(R.id.load_more_progressBar);
		addFooterView(footer, null, false);

		this.hasMorePages = true;

		super.setOnScrollListener(this);
		TypedValue typedValue = new TypedValue();
		context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
		setSelector(typedValue.resourceId);
//		defaultSeletor();
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}

	/**
	 * Set the listener that will receive notifications every time the list
	 * scrolls.
	 * 
	 * @param l
	 *            The scroll listener.
	 */
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mOnScrollListener = l;
	}

	/**
	 * Register a callback to be invoked when this list reaches the end (last
	 * item be visible)
	 * 
	 * @param onLoadMoreListener
	 *            The callback to run.
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}

		if (mOnLoadMoreListener != null) {

			if (visibleItemCount == totalItemCount) {
				mProgressBarLoadMore.setVisibility(View.GONE);
				return;
			}

			//boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
			boolean loadMore = firstVisibleItem + visibleItemCount >= (totalItemCount - 1); //included footer count?
			//AgmoLog.d("Loadmore " + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount);
			//AgmoLog.d("Loadmore " + loadMore);
			//AgmoLog.d("Loadmore " + hasMorePages + " " +  mIsLoadingMore + " " + mCurrentScrollState);
			if (hasMorePages && !mIsLoadingMore && loadMore) {
				mProgressBarLoadMore.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}

	}

	public void onLoadMore() {
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}

	public void hasMorePages(boolean morePage) {
		this.hasMorePages = morePage;
		if (!hasMorePages)
			onLoadMoreComplete();
	}


	public boolean getHasMorePage() {
		return hasMorePages;
	}

	/**
	 * Notify the loading more operation has finished
	 */
	public void onLoadMoreComplete() {
		mIsLoadingMore = false;
		mProgressBarLoadMore.setVisibility(View.GONE);
	}

	/**
	 * Interface definition for a callback to be invoked when list reaches the
	 * last item (the user load more items in the list)
	 */
	public interface OnLoadMoreListener {
		/**
		 * Called when the list reaches the last item (the last item is visible
		 * to the user)
		 */
		public void onLoadMore();
	}

	public void defaultSeletor(){
		TypedValue ta = new TypedValue();
		getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, ta, true);
		this.setSelector(ta.resourceId);
	}

	public void setProgressBarLoadMoreVisible(int visible){
		this.mProgressBarLoadMore.setVisibility(visible);
	}
}