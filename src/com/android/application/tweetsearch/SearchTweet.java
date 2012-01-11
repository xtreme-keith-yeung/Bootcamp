package com.android.application.tweetsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SearchTweet extends Activity {
	public static final String SEARCH_KEY = "search_key";
	
	private EditText mSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_tweet);
		
		mSearch = (EditText) findViewById(R.id.search_phrase);
		Button confirmButton = (Button) findViewById(R.id.confirm);
		
		confirmButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Bundle bundle = new Bundle();
				
				bundle.putString(SEARCH_KEY, mSearch.getText().toString());
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();
			}
			
		});
	}
}
