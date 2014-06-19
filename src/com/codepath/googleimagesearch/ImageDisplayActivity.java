package com.codepath.googleimagesearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.loopj.android.image.SmartImageTask.OnCompleteListener;
import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.app.ActionBar;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;


public class ImageDisplayActivity extends Activity {

	private static final int REQUEST_CODE = 20;
	private ShareActionProvider miShareAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		
		ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
		//SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		//ivImage.setImageUrl(result.getFullUrl(),R.drawable.image_not_available, R.drawable.loading);
	    //ivImage.setImageUrl(result.getFullUrl(), R.drawable.image_not_available, R.drawable.loading, new OnCompleteListener() {
	    //    @Override
	    //    public void onComplete() {
	    //        // Setup share intent now that image has loaded
	    //        //setupShareIntent();
	    //    }
	    //});
	    
		ImageView pImage = (ImageView) findViewById(R.id.ivResult);
	    Picasso.with(getBaseContext()).load(result.getFullUrl()).placeholder(R.drawable.loading).error(R.drawable.image_not_available).into(pImage, new Callback() {
			
			@Override
			public void onSuccess() {
				setupShareIntent();
			}
			
			@Override
			public void onError() {				
			}
		});
	    
	    
		pImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (getActionBar().isShowing()) {
					getActionBar().hide();
				} else {
					getActionBar().show();
					//getActionBar().setTitle("Title");
				}
			}
		});
	}
	
	// Can be triggered by a view event such as a button press
	public void onShareItem() {
	    // Get access to bitmap image from view
	    ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
	    // Get access to the URI for the bitmap
	    Uri bmpUri = getLocalBitmapUri(ivImage);
	    if (bmpUri != null) {		    
	        startActivity(Intent.createChooser(getIntent(), "Share Image"));    
	    } else {
	        // ...sharing failed, handle error
	    }
	}

	
	public void setupShareIntent() {
	    // Fetch Bitmap Uri locally
	    ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
	    Uri bmpUri = getLocalBitmapUri(ivImage); // see previous remote images section
	    // Create share intent as described above
	    Intent shareIntent = new Intent();
	    shareIntent.setAction(Intent.ACTION_SEND);
	    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
	    shareIntent.setType("image/*");
	    // Attach share event to the menu item provider
	    miShareAction.setShareIntent(shareIntent);
	}
	
	// Returns the URI path to the Bitmap displayed in specified ImageView
	public Uri getLocalBitmapUri(ImageView imageView) {
	    // Extract Bitmap from ImageView drawable
	    Drawable drawable = imageView.getDrawable();
	    Bitmap bmp = null;
	    if (drawable instanceof BitmapDrawable){
	       bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
	    } else {
	       return null;
	    }
	    // Store image to default external storage directory
	    Uri bmpUri = null;
	    try {
	        File file =  new File(Environment.getExternalStoragePublicDirectory(  
	            Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
	        file.getParentFile().mkdirs();
	        FileOutputStream out = new FileOutputStream(file);
	        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
	        out.close();
	        bmpUri = Uri.fromFile(file);
	    } catch (IOException e) {
	        e.printStackTrace();
			Toast.makeText(getApplicationContext(), "ERR", Toast.LENGTH_SHORT)
			.show();
	    }
	    return bmpUri;
	}


	
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

  	ActionBar actionBar = getActionBar();
  	String title = (String) actionBar.getTitle();
  	//actionBar.hide();
  	
      getMenuInflater().inflate(R.menu.image_display_activity_actions, menu);
      
	    // Locate MenuItem with ShareActionProvider
	    MenuItem item = menu.findItem(R.id.miShare);
	    // Fetch reference to the share action provider
	    miShareAction = (ShareActionProvider) item.getActionProvider();
	    	    
      return true;
  }
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.miShare:
	        	onShareItem();
	            return true;
	        case R.id.miWallpaper:
	        	//onWallpaper();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
    
	}



}
