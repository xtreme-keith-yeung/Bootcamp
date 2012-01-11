package com.android.application.tweetsearch;

import java.util.List;

import twitter4j.Query;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class TweetSearchActivity extends ListActivity {
	private static final int ACTIVITY_SEARCH = 0;
	
	private static final int REFRESH_ID = Menu.FIRST;
	private static final int SEARCH_ID = Menu.FIRST + 1;
	
	private Twitter twitter = new TwitterFactory().getInstance();
	private Query query;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        query = new Query("#bieber");
        
        //refreshTweets();
        new refreshTask().execute(query);
        
    }
    
    private void refreshTweets() {
    	try {
			List<Tweet> twts = twitter.search(query).getTweets();
			setListAdapter(new TweetAdapter(this, twts));
		} catch (TwitterException te) {
			te.printStackTrace();
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, REFRESH_ID, 0, R.string.refresh);
		menu.add(0, SEARCH_ID, 0, R.string.search);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case REFRESH_ID:
				//refreshTweets();
				new refreshTask().execute(query);
				return true;
			case SEARCH_ID:
				Intent i = new Intent(this, SearchTweet.class);
				startActivityForResult(i, ACTIVITY_SEARCH);
				return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			if (requestCode == ACTIVITY_SEARCH) {
				if (extras.getString(SearchTweet.SEARCH_KEY) != null) {
					query = new Query(extras.getString(SearchTweet.SEARCH_KEY));
					//refreshTweets();
					new refreshTask().execute(query);
				}
			}
		}
	}
	
	private class refreshTask extends AsyncTask<Query, Void, List<Tweet>> {
		protected ProgressDialog dialog;
		
		protected void onPreExecute() {
			dialog = ProgressDialog.show(TweetSearchActivity.this, "", "Loading Tweets...", true);
		}

		@Override
		protected List<Tweet> doInBackground(Query... queries) {
			int count = queries.length;
			if (count != 1)
				return null;
			query = queries[0];
			try {
				List<Tweet> twts = twitter.search(queries[0]).getTweets();
				return twts;
			} catch (TwitterException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		protected void onPostExecute(List<Tweet> result) {
			setListAdapter(new TweetAdapter(TweetSearchActivity.this, result));
			dialog.dismiss();
		}
		
	}
}