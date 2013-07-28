package com.gajah.inkcase.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gajah.inkcaseLib.InkCase;
import com.gajah.inkcaseLib.InkCaseUtils;

public class DemoActivity extends Activity implements OnClickListener {

	ImageView imgToSend;
	Button btnSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);

		imgToSend = (ImageView) findViewById(R.id.imgPicToSend);
		btnSend = (Button) findViewById(R.id.btnSend);

		btnSend.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.demo, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == btnSend) {
			sendToInkCase();
		}

	}

	private void sendToInkCase() {
		imgToSend.setDrawingCacheEnabled(true);
		Bitmap b = imgToSend.getDrawingCache();
		if (b == null)
			throw new RuntimeException("No image to send");

		File fileToSend = new File(getExternalCacheDir(), "helloInkCase.jpg");
		try {
			FileOutputStream fOut = new FileOutputStream(fileToSend);

			b.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (fileToSend.exists()) {
			try {
				Intent sharingIntent = new Intent(
						InkCase.ACTION_SEND_TO_INKCASE);
				sharingIntent.setType("image/jpeg");
				sharingIntent.putExtra(InkCase.EXTRA_FUNCTION_CODE,
						InkCase.CODE_SEND_WALLPAPER);
				sharingIntent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(fileToSend));
				sharingIntent.putExtra(InkCase.EXTRA_APP_NAME, InkCase.APP_NOW);
				sharingIntent.putExtra(InkCase.EXTRA_FILENAME,
						fileToSend.getName());
				InkCaseUtils.startInkCaseActivity(this, sharingIntent);
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

		}
	}

}
