package com.codepath.googleimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends Activity {
	
	EditText etQuery;
	Button btnSearch;
	GridView gvResults;
	
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position,
					long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				//i.putExtra("url", imageResult.getFullUrl());
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
	}

	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}
	
	public void onImageSearch(View v) {
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT)
				.show();
		AsyncHttpClient client = new AsyncHttpClient();
		
		// https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android
		
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + 
					"start=" + 0 + "&v=1.0&q=" + Uri.encode(query), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					JSONArray imageJsonResults = null;
					try {
						imageJsonResults = response.getJSONObject(
								"responseData").getJSONArray("results");
						imageResults.clear();
						imageAdapter.addAll(ImageResult
								.fromJSONArray(imageJsonResults));
						Log.d("DEBUG", imageResults.toString());
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			
		});
	}
}