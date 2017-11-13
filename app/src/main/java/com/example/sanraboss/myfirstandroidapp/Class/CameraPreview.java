package com.example.sanraboss.myfirstandroidapp.Class;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import static android.content.ContentValues.TAG;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
//        setWillNotDraw(false);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            Camera.Parameters params = mCamera.getParameters();
            String currentversion = android.os.Build.VERSION.SDK;
            Log.d("System out", "currentVersion " + currentversion);
            int currentInt = android.os.Build.VERSION.SDK_INT;
            Log.d("System out", "currentVersion " + currentInt);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (currentInt != 7) {
                    mCamera.setDisplayOrientation(90);
                } else {
                    Log.d("System out", "Portrait " + currentInt);

                    params.setRotation(90);

                /*
                 * params.set("orientation", "portrait");
                 * params.set("rotation",90);
                 */
                    mCamera.setParameters(params);
                }
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // camera.setDisplayOrientation(0);
                if (currentInt != 7) {
                    mCamera.setDisplayOrientation(0);
                } else {
                    Log.d("System out", "Landscape " + currentInt);
                    params.set("orientation", "landscape");
                    params.set("rotation", 90);
                    mCamera.setParameters(params);
                }
            }
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
//    @Override
//    protected void onDraw(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(Color.WHITE);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawPaint(paint);
//
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(20);
//        canvas.drawText("Test", 10, 25, paint);
//    }
}
