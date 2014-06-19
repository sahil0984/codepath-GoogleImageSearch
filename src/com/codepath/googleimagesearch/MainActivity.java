package com.codepath.googleimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
//import android.widget.SearchView;
//import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnCloseListener;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.codepath.googleimagesearch.EditFilterDialog.OnDataPass;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends SherlockFragmentActivity implements OnDataPass {
	
	//EditText etQuery;
	//Button btnSearch;
    GridView gvResults;
	
	String searchQuery;
	
	//private int currentPage;
	
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	
	ImageFilter imageFilter;
	String imageType;
	String imageSize;
	String imageColor;
	String imageSite;
	
	private final int REQUEST_CODE = 20;

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
		
		gvResults.setOnScrollListener(new EndlessScrollListener() {
	    @Override
	    public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
	    		if (isNetworkAvailable() & (totalItemsCount<64) & (totalItemsCount!=0)) {
		    		customLoadMoreDataFromApi(totalItemsCount);
	    		}
                // or customLoadMoreDataFromApi(totalItemsCount); 
	    }
        });
		
		imageFilter = new ImageFilter(0,0,0,"");
		
	}

	public void setupViews() {
		//etQuery = (EditText) findViewById(R.id.etQuery);
		//btnSearch = (Button) findViewById(R.id.btnSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}
	
	public void onImageSearch(String query) {
		//String query = etQuery.getText().toString();
		searchQuery = query;
		//Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT)
		//		.show();
		AsyncHttpClient client = new AsyncHttpClient();
		
		// https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android
		//as_filetype=jpg
		//imgtype=face
		//imgsz=icon
		//imgcolor=black
		//imgc=gray -> grayscale
		//as_sitesearch=photobucket.com
				
		translateImageProperties();
		imageAdapter.clear();

		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8" + 
					"&start=" + 0 + "&v=1.0&q=" + Uri.encode(query) +
					"&imgtype=" + imageType +
					"&imgsz=" + imageSize + 
					"&imgcolor=" + imageColor +
					"&as_sitesearch=" + imageSite, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					JSONArray imageJsonResults = null;
					try {
						imageJsonResults = response.getJSONObject(
								"responseData").getJSONArray("results");
						//imageResults.clear();
						//imageAdapter.clear();
						imageAdapter.addAll(ImageResult
								.fromJSONArray(imageJsonResults));
						Log.d("DEBUG", imageResults.toString());
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});

	}

	
    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
      // This method probably sends out a network request and appends new data items to your adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
    	
		//String query = etQuery.getText().toString();
    	
		AsyncHttpClient client = new AsyncHttpClient();

		//client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + 
		//		"start=" + offset + "&v=1.0&q=" + Uri.encode(searchQuery), new JsonHttpResponseHandler() {
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8" + 
				"&start=" + offset + "&v=1.0&q=" + Uri.encode(searchQuery) +
				"&imgtype=" + imageType +
				"&imgsz=" + imageSize + 
				"&imgcolor=" + imageColor + 
				"&as_sitesearch=" + imageSite, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject(
							"responseData").getJSONArray("results");
					//imageResults.clear();
					imageAdapter.addAll(ImageResult
							.fromJSONArray(imageJsonResults));
					Log.d("DEBUG", imageResults.toString());
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
// 
//    	ActionBar actionBar = getActionBar();
//    	String title = (String) actionBar.getTitle();
//    	//actionBar.hide();
//    	
//        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
//        return true;
//    }
    

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    com.actionbarsherlock.view.MenuItem searchItem = (com.actionbarsherlock.view.MenuItem) menu.findItem(R.id.action_search);
	    final com.actionbarsherlock.widget.SearchView searchView = (com.actionbarsherlock.widget.SearchView) searchItem.getActionView();
	    searchView.setQueryHint(Html.fromHtml((String)"<i>"+"Search..."+"</i>"));
	    searchView.setPadding(0, 0, 10, 0);
	    searchView.setOnQueryTextListener(new OnQueryTextListener() {
	       @Override
	       public boolean onQueryTextSubmit(String query) {
	            // perform query here
	    	   if (isNetworkAvailable() == true) { 
	    		   onImageSearch(query);
	    	   } else {
	    			Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT)
					.show();
	    	   }
	            return true;
	       }

	       @Override
	       public boolean onQueryTextChange(String newText) {
	           return false;
	       }
	   });
	    
	    searchView.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {
				//((com.actionbarsherlock.widget.SearchView) searchView.getRootView()).setIconified(true);
				return false;
			}
	    });
	   return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.miSettings:
	            //onSettings(item);
	            showFiltersDialog();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
    
    
	}
	

	private void showFiltersDialog() {
		FragmentManager fm = getSupportFragmentManager();
		EditFilterDialog editFilterDialog = EditFilterDialog.newInstance(imageFilter);
		editFilterDialog.show(fm, "fragment_filters");
		
	}

//	public void onSettings(com.actionbarsherlock.view.MenuItem item) {
//		
//		Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
//		i.putExtra("filter", imageFilter);
//		startActivityForResult(i, REQUEST_CODE);
//		
//	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    	     // Extract name value from result extras
    		ImageFilter filter = (ImageFilter) data.getSerializableExtra("filter");

    		
    		imageFilter.setImageType(filter.getImageType());
    		imageFilter.setImageSize(filter.getImageSize());
    		imageFilter.setImageColorFilter(filter.getImageColorFilter());
    		imageFilter.setImageSite(filter.getImageSite().toString());

    		//writeItems();
    	}
    }
    
	
	private void translateImageProperties() {
		switch(imageFilter.getImageType()) {
			case 0: imageType=""; break;
			case 1: imageType="face"; break;
			case 2: imageType="photo"; break;
			case 3: imageType="clipart"; break;
			case 4: imageType="lineart"; break;
			default: imageType=""; break;
		}
		switch(imageFilter.getImageSize()) {
			case 0: imageSize=""; break;
			case 1: imageSize="icon"; break;
			case 2: imageSize="medium"; break;
			case 3: imageSize="xxlarge"; break;
			case 4: imageSize="huge"; break;
			default: imageSize=""; break;
		}
		switch(imageFilter.getImageColorFilter()) {
			case 0: imageColor=""; break;
			case 1: imageColor="blue"; break;
			case 2: imageColor="brown"; break;
			case 3: imageColor="gray"; break;
			case 4: imageColor="green"; break;
			case 5: imageColor="orange"; break;
			case 6: imageColor="pink"; break;
			case 7: imageColor="purple"; break;
			case 8: imageColor="red"; break;
			case 9: imageColor="teal"; break;
			case 10: imageColor="white"; break;
			case 11: imageColor="yellow"; break;
			default: imageColor=""; break;
		}
		imageSite = imageFilter.getImageSite();
		
		//Toast.makeText(getApplicationContext(), "Type="+imageFilter.getImageType()+"Size="+imageFilter.getImageSize()+"Color="+imageFilter.getImageColorFilter(), Toast.LENGTH_SHORT)
		//.show();
	}
	
	//TODO: Add a dialog alert??
	private Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	
	
	@Override
	public void onDataPass(ImageFilter filter) {
	    //Log.d("LOG","hello " + data);
		imageFilter = filter;
	}
	
}