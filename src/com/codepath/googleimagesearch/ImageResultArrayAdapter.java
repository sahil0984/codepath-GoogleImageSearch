package com.codepath.googleimagesearch;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.graphics.Matrix.ScaleToFit;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView.ScaleType;


public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ImageResult imageInfo = this.getItem(position);
//		SmartImageView ivImage;
//		if (convertView == null) {
//			LayoutInflater inflater = LayoutInflater.from(getContext());
//			ivImage = (SmartImageView) inflater.inflate(R.layout.item_image_result, parent, false);
//			
//		} else {
//			ivImage = (SmartImageView) convertView;
//			ivImage.setImageResource(android.R.color.transparent);
//		}
//		ivImage.setImageUrl(imageInfo.getThumbUrl());
//		return ivImage;
//	
//	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = this.getItem(position);
		//SmartImageView ivImage;
        ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.item_image_result, parent, false);
		    holder = new ViewHolder();
		    holder.ivImage = (SmartImageView) convertView.findViewById(R.id.smartViewImage);

			convertView.setTag(holder);
			
		} else {
		    holder = (ViewHolder) convertView.getTag();
		    //To remove the flickering use the holder and the following clear command
		    holder.ivImage.setImageUrl(null);
			//holder.ivImage.setImageResource(android.R.color.transparent);
		}
		holder.ivImage.setImageUrl(imageInfo.getThumbUrl());
		
		return convertView;
	
	}
	

	static class ViewHolder {
	    public SmartImageView ivImage;

	}
	
}
