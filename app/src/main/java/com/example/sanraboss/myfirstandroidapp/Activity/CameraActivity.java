package com.example.sanraboss.myfirstandroidapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.sanraboss.myfirstandroidapp.Class.CameraPreview;
import com.example.sanraboss.myfirstandroidapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CAMERA_ACTIVITY";
    private Camera mCamera;
    private CameraPreview mPreview;
    private Button retakeButton;
    private Button confirmButton;
    private Button captureButton;
    private byte[] pictureByteData;
    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            pictureByteData = data;
            retakeButton.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.VISIBLE);
            captureButton.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        pictureByteData = null;
        retakeButton = (Button) findViewById(R.id.button_retake);
        retakeButton.setVisibility(View.INVISIBLE);
        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPreview.retakePicture();
                captureButton.setVisibility(View.VISIBLE);
                retakeButton.setVisibility(View.INVISIBLE);
                confirmButton.setVisibility(View.INVISIBLE);
            }
        });
        confirmButton = (Button) findViewById(R.id.button_use_photo);
        confirmButton.setVisibility(View.INVISIBLE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePicture();
            }
        });
        captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mPreview.takePicture(mPicture);

                    }
                }
        );

    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }


    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private void savePicture() {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            return;
        }
        if (pictureByteData == null) {
            return;
        }

        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(pictureByteData, 0, pictureByteData.length);
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(bytes.toByteArray());
            fos.close();
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
//                Log.d("bitmap height", bitmap.getHeight()+"");
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(),matrix, true);
            FileOutputStream fos2 = new FileOutputStream(pictureFile.toString());
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos2);
            fos2.close();
//                fos.write(data);
//                fos.close();

        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        Intent i = new Intent(CameraActivity.this, PreviewActivity.class);
        i.putExtra("pictureName", pictureFile.getAbsolutePath());
        startActivity(i);
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            Log.d("get Camera", e.toString());
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "/myfirstandroidapp");
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myfirstandroidapp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new Date().toString();
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

//        File.createTempFile(mediaFile.getName(), "jpg", mediaStorageDir);

        return mediaFile;
    }


}
