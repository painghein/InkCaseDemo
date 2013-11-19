package com.gajah.inkcase.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.gajah.inkcase.demo.R;
import com.gajah.inkcaseLib.InkCase;
import com.gajah.inkcaseLib.InkCaseCompanionException;
import com.gajah.inkcaseLib.InkCaseUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Log;

public class Util {
	public static final String TAG = "Util-Log";
	public static final String welcomeName = "welcome.jpg";
	public static final String page0Name = "page0.jpg";
	public static final String page1Name = "page1.jpg";
	public static final String page2Name = "page2.jpg";
	public static final String page3Name = "page3.jpg";
	public static final String page4Name = "page4.jpg";
	public static final String page5Name = "page5.jpg";
	public static final String page6Name = "page6.jpg";
	public static final String page7Name = "page7.jpg";
	public static final String page8Name = "page8.jpg";
	
	/**
	 * scale to 720*960 maybe better display
	 * @param src
	 * @return
	 */
	public static Bitmap getScaleBitmap(Bitmap src) {
		Bitmap bitmap = Bitmap.createScaledBitmap(src, 720, 960, true);
		return bitmap;
	}

	public static void copyPhotoToSDCard(Context context) {
		File welcomeFile = new File(context.getExternalCacheDir(), welcomeName);
		File page0File = new File(context.getExternalCacheDir(), page0Name);
		File page1File = new File(context.getExternalCacheDir(), page1Name);
		File page2File = new File(context.getExternalCacheDir(), page2Name);
		File page3File = new File(context.getExternalCacheDir(), page3Name);
		File page4File = new File(context.getExternalCacheDir(), page4Name);
		File page5File = new File(context.getExternalCacheDir(), page5Name);
		File page6File = new File(context.getExternalCacheDir(), page6Name);
		File page7File = new File(context.getExternalCacheDir(), page7Name);
		File page8File = new File(context.getExternalCacheDir(), page8Name);

		try{
			copyPhoto(context, welcomeFile, R.drawable.welcome);
			copyPhoto(context, page0File, R.drawable.page0);
			copyPhoto(context, page1File, R.drawable.page1);
			copyPhoto(context, page2File, R.drawable.page2);
			copyPhoto(context, page3File, R.drawable.page3);
			copyPhoto(context, page4File, R.drawable.page4);
			copyPhoto(context, page5File, R.drawable.page5);
			copyPhoto(context, page6File, R.drawable.page6);
			copyPhoto(context, page7File, R.drawable.page7);
			copyPhoto(context, page8File, R.drawable.page8);
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	public static void copyPhoto(Context context, File outPath, int resId) throws IOException {
		if (outPath.exists())
			return;

		outPath.getParentFile().mkdirs();
		try {
			outPath.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resId);
		try {
			FileOutputStream fOut = new FileOutputStream(outPath);
			bitmap.compress(CompressFormat.JPEG, 60, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.recycle();
		bitmap = null;
	}

	/**
	 * just send photo to InkCase
	 * 
	 * @param context
	 * @param imageUri can not be null
	 */
	public static void sendPhotoToInkCase(Context context, Uri imageUri) {
		if (imageUri == null) {
			Log.d(TAG, "imageUri:" + imageUri);
			return;
		}

		Intent sharingIntent = new Intent(InkCase.ACTION_SEND_TO_INKCASE);
		sharingIntent.setType("image/jpeg");
		sharingIntent.putExtra(InkCase.EXTRA_FUNCTION_CODE,
				InkCase.CODE_SEND_WALLPAPER);
		sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
		sharingIntent.putExtra(InkCase.EXTRA_APP_NAME, InkCase.APP_PHOTO);
		sharingIntent.putExtra(InkCase.EXTRA_APP_PACKAGE,
				context.getPackageName());
		sharingIntent.putExtra(InkCase.EXTRA_FILENAME,
				new File(imageUri.getPath()).getName());
		try {
			InkCaseUtils.startInkCaseActivity(context, sharingIntent);
		} catch (InkCaseCompanionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reader send page(photo) to InkCase
	 * 
	 * @param context
	 * @param imageUri can not be null
	 * @param bookTitle
	 * @param pagePosition
	 *            page position in book,converted into a String
	 * @param requestIndex
	 *            only contain one of
	 *            {"no","previous","next","previous_finish","next_finish"},normal use
	 *            "previous" or "next"."previous_finish" represent current page is first page,
	 *            "next_finish" represent current page is last page.first time must put into "no";
	 *            
	 */
	public static void sendPageToInkCase(Context context, Uri imageUri,
			String bookTitle, String pagePosition, String requestIndex) {
		if (imageUri == null) {
			Log.d(TAG, "imageUri:" + imageUri);
			return;
		}
		Intent sharingIntent = new Intent(InkCase.ACTION_SEND_TO_INKCASE);
		sharingIntent.setType("image/jpeg");
		sharingIntent.putExtra(InkCase.EXTRA_FUNCTION_CODE,
				InkCase.CODE_SEND_WALLPAPER);
		sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
		sharingIntent.putExtra(InkCase.EXTRA_APP_NAME, InkCase.APP_EPIREADER);
		sharingIntent.putExtra(InkCase.EXTRA_BOOK_TITLE, bookTitle);
		sharingIntent.putExtra(InkCase.EXTRA_CURRENT_PAGE_POS, pagePosition);
		sharingIntent.putExtra(InkCase.EXTRA_REQUEST_INDEX, requestIndex);
		sharingIntent.putExtra(InkCase.EXTRA_APP_PACKAGE,
				context.getPackageName());
		sharingIntent.putExtra(InkCase.EXTRA_FILENAME,
				new File(imageUri.getPath()).getName());
		try {
			InkCaseUtils.startInkCaseActivity(context, sharingIntent);
		} catch (InkCaseCompanionException e) {
			e.printStackTrace();
		}
	}

}
