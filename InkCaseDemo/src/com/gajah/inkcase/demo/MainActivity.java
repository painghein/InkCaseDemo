package com.gajah.inkcase.demo;

import com.gajah.inkcase.demo.utils.Util;
import com.gajah.inkcaseLib.InkCase;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView resultTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		resultTV = (TextView) findViewById(R.id.resultTV);
		Util.copyPhotoToSDCard(this);
	}

	public void onSendPhotoBtnClicked(View view) {
		startActivity(new Intent(this, SendActivity.class));
	}
	
	public void onImitateReaderBtnClicked(View view){
		startActivity(new Intent(this, ImitateReaderActivity.class));
	}

}
