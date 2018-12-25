package com.diegocarloslima.byakugallery.sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.appculture.PanoramaImageView;
import com.diegocarloslima.byakugallery.R;
import com.diegocarloslima.byakugallery.lib.TileBitmapDrawable;
import com.diegocarloslima.byakugallery.lib.TouchImageView;

import java.io.InputStream;

public class TouchImageViewSampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.touch_image_view_sample);

		final TouchImageView image = (TouchImageView) findViewById(R.id.touch_image_view_sample_image);
		image.setScrollableOnTouch(true);
		image.setEndlessScrollingEnabled(true);
		final InputStream is = getResources().openRawResource(R.raw.photo4);
		final Drawable placeHolder = getResources().getDrawable(R.drawable.android_placeholder);
		TileBitmapDrawable.attachTileBitmapDrawable(image, is, placeHolder, Bitmap.Config.ARGB_8888, 4, null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		((PanoramaImageView) findViewById(R.id.touch_image_view_sample_image)).startAccelerometer();
	}

	@Override
	protected void onPause() {
		super.onPause();
		((PanoramaImageView) findViewById(R.id.touch_image_view_sample_image)).stopAccelerometer();
	}
}
