package com.android.application.tweetsearch;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import twitter4j.Tweet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Tweet[] mTweet;
	private Bitmap[] mIcon;
	
	public TweetAdapter(Context context, List<Tweet> twts)
	{
		mInflater = LayoutInflater.from(context);
		
		mTweet = new Tweet[twts.size()];
		mIcon = new Bitmap[twts.size()];
		
		for (int i = 0; i < twts.size(); ++i) {
			mTweet[i] = twts.get(i);
			try {
				URL url = new URL(twts.get(i).getProfileImageUrl());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				
				conn.connect();
				mIcon[i] = BitmapFactory.decodeStream(conn.getInputStream());
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getCount() {
		return mTweet.length;
	}

	public Object getItem(int pos) {
		return mTweet[pos];
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_row, null);
			
			holder = new ViewHolder();
			holder.username = (TextView) convertView.findViewById(R.id.username);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.username.setText(mTweet[pos].getFromUser());
		holder.content.setText(mTweet[pos].getText());
		holder.time.setText(mTweet[pos].getCreatedAt().toLocaleString());
		holder.icon.setImageBitmap(mIcon[pos]);
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView username;
		TextView content;
		TextView time;
		ImageView icon;
	}

}
