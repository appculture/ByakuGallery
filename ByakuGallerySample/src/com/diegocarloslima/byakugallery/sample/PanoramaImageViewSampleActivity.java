package com.diegocarloslima.byakugallery.sample;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.appculture.PanoramaImageView;
import com.diegocarloslima.byakugallery.R;
import com.diegocarloslima.byakugallery.lib.TileBitmapDrawable;

import java.io.InputStream;

public class PanoramaImageViewSampleActivity extends Activity {

    private PanoramaImageView panoramaImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.panorama_image_view_sample);

        panoramaImageView = (PanoramaImageView) findViewById(R.id.panorama_image_view_sample_image);
        final InputStream is = getResources().openRawResource(R.raw.sample_panorama);
        final Drawable placeHolder = getResources().getDrawable(R.drawable.android_placeholder);
        panoramaImageView.setMaxScale(4);
        panoramaImageView.setScrollableOnTouch(true);
        TileBitmapDrawable.attachTileBitmapDrawable(panoramaImageView, is, placeHolder, null);

        findViewById(R.id.collapseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanoramaHeight(-50);
            }
        });

        findViewById(R.id.expandButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanoramaHeight(50);
            }
        });
    }

    private void setPanoramaHeight(int heightChange) {
        ViewGroup.LayoutParams layoutParams = panoramaImageView.getLayoutParams();
        layoutParams.height += heightChange;
        panoramaImageView.setLayoutParams(layoutParams);
    }
}
