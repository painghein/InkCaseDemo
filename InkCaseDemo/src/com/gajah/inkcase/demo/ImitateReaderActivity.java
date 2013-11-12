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
import android.util.Log;
import android.view.Menu;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.imitate_reader, menu);
		return true;
	}

	public void onSendFirstPageBtnClicked(View view) {
		Util.sendPageToInkCase(ImitateReaderActivity.this,
				Uri.fromFile(new File(getExternalCacheDir(), Util.page1Name)),
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
			String msg="";
			int keyCode = intent.getIntExtra(InkCase.EXTRA_KEYCODE,
					InkCase.KEYCODE_POWER);
			String requestDirection = intent
					.getStringExtra(InkCase.EXTRA_REQUEST_INDEX);
			int curPagePos = Integer.parseInt(intent
					.getStringExtra(InkCase.EXTRA_CURRENT_PAGE_POS));

			switch (keyCode) {
			case InkCase.CODE_REQUEST_DATA:
				//handle InkCase request page
				msg = "\n" + "request " + requestDirection;

				int sendPagePos = 1;
				Log.e(TAG, "curPagePos:" + curPagePos);
				if (requestDirection.equals("previous")) {
					sendPagePos = (curPagePos - 1 + 3) % 3;
				} else if (requestDirection.equals("next")) {
					sendPagePos = (curPagePos + 1) % 3;
				}
				String fileName = Util.page1Name;
				if (sendPagePos == 0) {
					fileName = Util.page0Name;
				} else if (sendPagePos == 2) {
					fileName = Util.page2Name;
				}

				Util.sendPageToInkCase(
						ImitateReaderActivity.this,
						Uri.fromFile(new File(getExternalCacheDir(), fileName)),
						"book", sendPagePos + "", requestDirection);
				break;
				
			case InkCase.KEYCODE_LEFT:
				//do nothing
				Log.d(TAG, "left key clicked");
				msg ="\n" + "left key clicked";
				break;
			case InkCase.KEYCODE_RIGHT:
				//do nothing
				Log.d(TAG, "right key clicked");
				msg ="\n" + "right key clicked";
				break;
			}
			resultTV.append(msg);
			
		}
	};

}
