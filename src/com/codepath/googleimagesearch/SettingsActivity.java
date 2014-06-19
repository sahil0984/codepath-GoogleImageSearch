package com.codepath.googleimagesearch;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
	
	private Spinner spnImageType;
	private Spinner spnImageSize;
	private Spinner spnImageColorFilter;
	private EditText etImageSite;
	private Button btnSaveFilters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		setupViews();
		
		ImageFilter filter = (ImageFilter) getIntent().getSerializableExtra("filter");
		//SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		//ivImage.setImageUrl(result.getFullUrl());
		spnImageType.setSelection(filter.getImageType());
		spnImageSize.setSelection(filter.getImageSize());
		spnImageColorFilter.setSelection(filter.getImageColorFilter());
		etImageSite.setText(filter.getImageSite());
	}

	public void setupViews() {
		spnImageType = (Spinner) findViewById(R.id.spnImageType);
		spnImageSize = (Spinner) findViewById(R.id.spnImageSize);
		spnImageColorFilter = (Spinner) findViewById(R.id.spnImageColorFilter);
		etImageSite = (EditText) findViewById(R.id.etEditSite);
		btnSaveFilters = (Button) findViewById(R.id.btnSaveFilters);
	}
	
	public void onSavedFilters(View v) {
		ImageFilter filter = new ImageFilter(0,0,0,"");
		filter.setImageType((int) spnImageType.getSelectedItemId()); 
		filter.setImageSize((int) spnImageSize.getSelectedItemId()); 
		filter.setImageColorFilter((int) spnImageColorFilter.getSelectedItemId());
		filter.setImageSite(etImageSite.getText().toString());

		
		Intent data = new Intent();
    	data.putExtra("filter", filter);
    	setResult(RESULT_OK, data); // set result code and bundle data for response
    	finish(); 
	}
}
