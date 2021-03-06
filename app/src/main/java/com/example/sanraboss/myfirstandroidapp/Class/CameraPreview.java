package com.example.sanraboss.myfirstandroidapp.Class;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private final String TAG = "CAMERA_PREVIEW";
    private FocusView focusView;
    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback(){

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            if (arg0){
                mCamera.cancelAutoFocus();
            }
        }
    };

    public CameraPreview(Context context, Camera camera) {
        super(context);
//        setWillNotDraw(false);
        mCamera = camera;
        focusView = new FocusView(context, null);
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void takePicture(Camera.PictureCallback mPicture) {
        mCamera.takePicture(null, null, mPicture);
    }

    public void retakePicture() {
        mCamera.startPreview();
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
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    mCamera.setParameters(params);
//                    mCamera.autoFocus(autoFocusCallback);
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
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    mCamera.setParameters(params);
//                    mCamera.autoFocus(autoFocusCallback);
                }
            }
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void doTouchFocus(final Rect tfocusRect) {
        try {
            List<Camera.Area> focusList = new ArrayList<Camera.Area>();
            Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
            focusList.add(focusArea);

            Camera.Parameters param = mCamera.getParameters();
            param.setFocusAreas(focusList);
//            param.setMeteringAreas(focusList);
            mCamera.setParameters(param);
            mCamera.autoFocus(autoFocusCallback);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
            Log.i(TAG, "Unable to autofocus");
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();

            Rect touchRect = new Rect(
                    (int)(x - 100),
                    (int)(y - 100),
                    (int)(x + 100),
                    (int)(y + 100));


            final Rect targetFocusRect = new Rect(
                    touchRect.left * 2000/this.getWidth() - 1000,
                    touchRect.top * 2000/this.getHeight() - 1000,
                    touchRect.right * 2000/this.getWidth() - 1000,
                    touchRect.bottom * 2000/this.getHeight() - 1000);

            doTouchFocus(targetFocusRect);
            focusView.setHaveTouch(true, touchRect);
            focusView.invalidate();

            // Remove the square indicator after 1000 msec
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    focusView.setHaveTouch(false, new Rect(0,0,0,0));
                    focusView.invalidate();
                }
            }, 1000);


        }

        return false;
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
