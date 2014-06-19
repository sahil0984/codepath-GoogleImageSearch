package com.codepath.googleimagesearch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditFilterDialog extends DialogFragment {

	private Spinner spnImageType;
	private Spinner spnImageSize;
	private Spinner spnImageColorFilter;
	private EditText etImageSite;
	private Button btnSaveFilters;
	
	private static ImageFilter imageFilter;
	
	public EditFilterDialog () {
		//Empty constructor required for Dialog Fragment
	}
	
	public static EditFilterDialog newInstance(ImageFilter filter) {
		EditFilterDialog  frag = new EditFilterDialog();
		Bundle args = new Bundle();
				
		args.putSerializable("filter", filter);
	    frag.setArguments(args);

	    return frag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        getDialog().setTitle("Edit Filters");
        
		View view = inflater.inflate(R.layout.fragment_filters, container);
		spnImageType = (Spinner) view.findViewById(R.id.spnImageType);
		spnImageSize = (Spinner) view.findViewById(R.id.spnImageSize);
		spnImageColorFilter = (Spinner) view.findViewById(R.id.spnImageColorFilter);
		etImageSite = (EditText) view.findViewById(R.id.etEditSite);
		btnSaveFilters = (Button) view.findViewById(R.id.btnSaveFilters);
				
		btnSaveFilters.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageFilter filter = new ImageFilter(0,0,0,"");
				filter.setImageType((int) spnImageType.getSelectedItemId()); 
				filter.setImageSize((int) spnImageSize.getSelectedItemId()); 
				filter.setImageColorFilter((int) spnImageColorFilter.getSelectedItemId());
				filter.setImageSite(etImageSite.getText().toString());
				
			    dataPasser.onDataPass(filter);
			    dismiss();
			}
		});
		
		
		imageFilter = (ImageFilter) getArguments().getSerializable("filter");
		spnImageType.setSelection(imageFilter.getImageType());
		spnImageSize.setSelection(imageFilter.getImageSize());
		spnImageColorFilter.setSelection(imageFilter.getImageColorFilter());
		etImageSite.setText(imageFilter.getImageSite());

		
		return view;
	}
	
	//Passing data from fragment to activity
	//----------------------------------------
	//To pass data back to main activity, create an interface and implement it in main activity
	public interface OnDataPass {
	    public void onDataPass(ImageFilter filter);
	}
	OnDataPass dataPasser;
	@Override
	public void onAttach(Activity a) {
	    super.onAttach(a);
	    dataPasser = (OnDataPass) a;
	}
	
}
