package com.miu360.taxi_check.view;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miu360.inspect.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * @author shiner
 */
public class ListViewHolder {
	public PullToRefreshListView list;
	public View empty;

	public static ListViewHolder initList(Context ctx, View root) {
		ListViewHolder holder = new ListViewHolder();
		holder.list = (PullToRefreshListView) root.findViewById(R.id.list);
		holder.list.setShowIndicator(false);
		// holder.list.setOnScrollListener(new PauseOnScrollListener(ImageLoader
		// .getInstance(), false, true));
		holder.empty = root.findViewById(R.id.empty);
		return holder;
	}

	public static ListViewHolder initList(Activity act) {
		ListViewHolder holder = new ListViewHolder();
		holder.list = (PullToRefreshListView) act.findViewById(R.id.list);
		holder.list.setShowIndicator(false);
		// holder.list.setOnScrollListener(new PauseOnScrollListener(ImageLoader
		// .getInstance(), false, true));
		holder.empty = act.findViewById(R.id.empty);
		return holder;
	}

	public void mayShowEmpty(int count) {
		empty.setVisibility(count > 0 ? View.GONE : View.VISIBLE);
	}

}
