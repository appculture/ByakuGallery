package com.diegocarloslima.byakugallery.sample;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.appculture.PanoramaImageView;
import com.diegocarloslima.byakugallery.R;
import com.diegocarloslima.byakugallery.lib.TileBitmapDrawable;

import java.io.InputStream;

public class PanoramaImageViewSampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.panorama_image_view_sample);

        final PanoramaImageView panoramaImageView = (PanoramaImageView) findViewById(R.id.touch_image_view_sample_image);
        final InputStream is = getResources().openRawResource(R.raw.sample_panorama);
        final Drawable placeHolder = getResources().getDrawable(R.drawable.android_placeholder);
        panoramaImageView.setMaxScale(4);
        panoramaImageView.setScrollableOnTouch(true);
        TileBitmapDrawable.attachTileBitmapDrawable(panoramaImageView, is, placeHolder, null);
    }
}
