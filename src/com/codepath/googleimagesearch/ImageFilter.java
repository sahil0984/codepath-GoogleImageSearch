package com.codepath.googleimagesearch;

import java.io.Serializable;

public class ImageFilter implements Serializable {

	private static final long serialVersionUID = -916537732538748912L;
	private int ImageType;
	private int ImageSize;
	private int ImageColorFilter;
	private String ImageSite;
	
	
	public ImageFilter(int imageType, int imageSize,
			int imageColorFilter, String imageSite) {
		super();
		ImageType = imageType;
		ImageSize = imageSize;
		ImageColorFilter = imageColorFilter;
		ImageSite = imageSite;
	}
	
	public int getImageType() {
		return ImageType;
	}
	public void setImageType(int imageType) {
		ImageType = imageType;
	}
	
	public int getImageSize() {
		return ImageSize;
	}
	public void setImageSize(int imageSize) {
		ImageSize = imageSize;
	}
	
	public int getImageColorFilter() {
		return ImageColorFilter;
	}
	public void setImageColorFilter(int imageColorFilter) {
		ImageColorFilter = imageColorFilter;
	}
	
	public String getImageSite() {
		return ImageSite;
	}
	public void setImageSite(String imageSite) {
		ImageSite = imageSite;
	}
}
