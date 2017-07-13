package com.appculture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Surface;

import com.diegocarloslima.byakugallery.lib.TileBitmapDrawable;
import com.diegocarloslima.byakugallery.lib.TouchImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class PanoramaImageView extends TouchImageView implements SensorEventListener {
    private static final int NEGATIVE_DIRECTION = -1;
    private static final int POSITIVE_DIRECTION = 1;

    private static final int PORTRAIT = 10;
    private static final int LANDSCAPE = 11;
    private static final int REVERSE_PORTRAITE = 12;
    private static final int REVERSE_LANDSCAPE = 13;

    private SensorManager sensorManager;
    private Sensor sensor;
    private float speed = 7.0f;

    public PanoramaImageView(Context context) {
        super(context);
        init();
    }

    public PanoramaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PanoramaImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (getScreenOrientation() == PORTRAIT) {
                float X = event.values[1] * speed;
                float Y = 0;
                getImageMatrix().postTranslate(X, Y);
                scrollHorizontally(X);
                scrollVertically(Y);
                invalidate();
            } else if (getScreenOrientation() == LANDSCAPE) {
                float Z = event.values[0] * speed;
                float Y = 0;
                getImageMatrix().postTranslate(Z, Y);
                scrollHorizontally(Z);
                scrollVertically(Y);
                invalidate();
            } else if (getScreenOrientation() == REVERSE_PORTRAITE) {
                float X = event.values[1] * speed;
                float Y = 0;
                getImageMatrix().postTranslate(X, Y);
                scrollHorizontally(X);
                scrollVertically(Y);
                invalidate();
            } else if (getScreenOrientation() == REVERSE_LANDSCAPE) {
                float Z = -event.values[0] * speed;
                float Y = 0;
                getImageMatrix().postTranslate(-Z, Y);
                scrollHorizontally(Z);
                scrollVertically(Y);
                invalidate();
            }
        }
    }

    private void scrollVertically(float y) {
        if (!canScrollVertically(NEGATIVE_DIRECTION)) {
            getImageMatrix().postTranslate(0, -y);
        } else if (!canScrollVertically(POSITIVE_DIRECTION)) {
            getImageMatrix().postTranslate(0, -y);
        }
    }

    private void scrollHorizontally(float x) {
        if (!canScrollHorizontally(NEGATIVE_DIRECTION)) {
            getImageMatrix().postTranslate(-x, 0);
        } else if (!canScrollHorizontally(POSITIVE_DIRECTION)) {
            getImageMatrix().postTranslate(-x, 0);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void startAccelerometer() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopAccelerometer() {
        sensorManager.unregisterListener(this);
    }

    private void init() {
        sensorManager = (SensorManager) ((Activity) getContext()).getSystemService(Activity.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    private int getScreenOrientation() {
        final int rotation = ((Activity) this.getContext()).getWindowManager().getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return PORTRAIT;
            case Surface.ROTATION_90:
                return LANDSCAPE;
            case Surface.ROTATION_180:
                return REVERSE_PORTRAITE;
            default:
                return REVERSE_LANDSCAPE;
        }
    }

    public void setPanoramaFromInputStream(InputStream is, @DrawableRes int placeholderRes, @Nullable TileBitmapDrawable.OnInitializeListener initializeListener) {
        final Drawable placeHolder = ContextCompat.getDrawable(getContext(), placeholderRes);
        TileBitmapDrawable.attachTileBitmapDrawable(this, is, placeHolder, initializeListener);
    }

    public void setPanoramaFromInputStream(InputStream is, @Nullable TileBitmapDrawable.OnInitializeListener initializeListener) {
        setPanoramaFromInputStream(is, 0, initializeListener);
    }

    public void setPanoramaFromInputStream(InputStream is) {
        setPanoramaFromInputStream(is, null);
    }

    public void setPanoramaFromRes(@RawRes int rawResImage, @DrawableRes int placeholderRes, @Nullable TileBitmapDrawable.OnInitializeListener initializeListener) {
        final InputStream is = getResources().openRawResource(rawResImage);
        final Drawable placeHolder = ContextCompat.getDrawable(getContext(), placeholderRes);
        TileBitmapDrawable.attachTileBitmapDrawable(this, is, placeHolder, initializeListener);
    }

    public void setPanoramaFromRes(@RawRes int rawResImage, @DrawableRes int placeholderRes) {
        setPanoramaFromRes(rawResImage, placeholderRes, null);
    }

    public void setPanoramaFromBitmap(Bitmap bitmap, @DrawableRes int placeholderRes) {
        setPanoramaFromBitmap(bitmap, placeholderRes, null);
    }

    public void setPanoramaFromBitmap(final Bitmap bitmap, @DrawableRes final int placeholderRes, @Nullable final TileBitmapDrawable.OnInitializeListener initializeListener) {
        setScaleTypeMatrixIfNeeded();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Drawable placeHolder = null;
                if (placeholderRes != 0) {
                    placeHolder = ContextCompat.getDrawable(getContext(), placeholderRes);
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapData = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(bitmapData);
                loadImageFromUiThread(bs, placeHolder, initializeListener);
            }
        }).start();

    }

    public void setPanoramaFromBitmap(Bitmap bitmap) {
        setPanoramaFromBitmap(bitmap, null);
    }

    public void setPanoramaFromBitmap(Bitmap bitmap, @Nullable final TileBitmapDrawable.OnInitializeListener initializeListener) {
        setPanoramaFromBitmap(bitmap, 0, initializeListener);
    }

    private void loadImageFromUiThread(final InputStream inputStream, final Drawable placeholder, @Nullable final TileBitmapDrawable.OnInitializeListener initializeListener) {
        post(new Runnable() {
            @Override
            public void run() {
                TileBitmapDrawable.attachTileBitmapDrawable(PanoramaImageView.this, inputStream, placeholder, initializeListener);
            }
        });

    }

    private void setScaleTypeMatrixIfNeeded() {
        if (getScaleType() != ScaleType.MATRIX) {
            setScaleType(ScaleType.MATRIX);
        }
    }
}
