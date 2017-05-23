package com.appculture;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.Surface;

import com.diegocarloslima.byakugallery.lib.TouchImageView;

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
}
