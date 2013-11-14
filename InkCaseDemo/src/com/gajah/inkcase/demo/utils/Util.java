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

		try{
			copyPhoto(context, welcomeFile, R.drawable.welcome);
			copyPhoto(context, page0File, R.drawable.page0);
			copyPhoto(context, page1File, R.drawable.page1);
			copyPhoto(context, page2File, R.drawable.page2);
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
	 * @param imageUri
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
	 * @param imageUri
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
