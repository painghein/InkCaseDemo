package com.gajah.inkcase.demo;

import java.io.File;

import com.gajah.inkcase.demo.utils.Util;
import com.gajah.inkcaseLib.InkCase;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class ImitateReaderActivity extends Activity {
	public static final String TAG = "ImitateReaderActivity";

	TextView resultTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imitate_reader);
		resultTV = (TextView) findViewById(R.id.resultTV);
		resultTV.setMovementMethod(new ScrollingMovementMethod());

		IntentFilter intent = new IntentFilter(InkCase.KEY_EVENTS);
		registerReceiver(keyEventReceiver, intent);
	}

	public void onSendFirstPageBtnClicked(View view) {

		// first time requestIndex must put into "no"
		Util.sendPageToInkCase(ImitateReaderActivity.this, getImageUri(1),
				"book", "1", "no");
	}

	// public void onPreviousBtnClicked(View view) {
	// Util.sendPageToInkCase(ImitateReaderActivity.this, Uri.fromFile(new
	// File(getExternalCacheDir(),Util.page0Name)), "book", "0", "previous");
	// }
	//
	// public void onNextBtnClicked(View view) {
	// Util.sendPageToInkCase(ImitateReaderActivity.this, Uri.fromFile(new
	// File(getExternalCacheDir(),Util.page2Name)), "book", "2", "next");
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(keyEventReceiver);
	}

	BroadcastReceiver keyEventReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// String msg="";
			int keyCode = intent.getIntExtra(InkCase.EXTRA_KEYCODE,
					InkCase.KEYCODE_POWER);
			String requestDirection = intent
					.getStringExtra(InkCase.EXTRA_REQUEST_INDEX);
			int curPagePos = Integer.parseInt(intent
					.getStringExtra(InkCase.EXTRA_CURRENT_PAGE_POS));

			switch (keyCode) {
			// handle InkCase request page
			case InkCase.CODE_REQUEST_DATA:
				// msg = "\n" + "request " + requestDirection;
				int sendPagePos = curPagePos;
				String requestIndex = requestDirection;

				Uri imageUri = null;
				if (requestDirection.equals(IndexType.previous)) {
					if (sendPagePos == 0)// first page,just put
											// "previous_finish" inside and
											// reply InkCase
						requestIndex = IndexType.previous_finish;
					else {
						sendPagePos--;
						requestIndex = IndexType.previous;
					}

				} else if (requestDirection.equals(IndexType.next)) {
					if (sendPagePos == 8)// first page,just put "next_finish"
											// inside and reply InkCase
						requestIndex = IndexType.next_finish;
					else {
						sendPagePos++;
						requestIndex = IndexType.next;
					}
				}
				imageUri = getImageUri(sendPagePos);
				Util.sendPageToInkCase(ImitateReaderActivity.this, imageUri,
						"book", sendPagePos + "", requestIndex);
				break;

			// case InkCase.KEYCODE_LEFT:
			// //do nothing
			// Log.d(TAG, "left key clicked");
			// msg ="\n" + "left key clicked";
			// break;
			// case InkCase.KEYCODE_RIGHT:
			// //do nothing
			// Log.d(TAG, "right key clicked");
			// msg ="\n" + "right key clicked";
			// break;
			}
			// resultTV.append(msg);

		}
	};

	private Uri getImageUri(int index) {
		String fileName;
		switch (index) {
		case 0:
			fileName = Util.page0Name;
			break;
		case 2:
			fileName = Util.page2Name;
			break;
		case 3:
			fileName = Util.page3Name;
			break;
		case 4:
			fileName = Util.page4Name;
			break;
		case 5:
			fileName = Util.page5Name;
			break;
		case 6:
			fileName = Util.page6Name;
			break;
		case 7:
			fileName = Util.page7Name;
			break;
		case 8:
			fileName = Util.page8Name;
			break;
		default:
			fileName = Util.page1Name;
		}
		return Uri.fromFile(new File(getExternalCacheDir(), fileName));

	}

	private class IndexType {
		// reader request index
		public final static String previous = "previous";
		public final static String next = "next";
		public final static String previous_finish = "previous_finish";
		public final static String next_finish = "next_finish";
	}

}
