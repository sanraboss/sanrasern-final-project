package com.example.sanraboss.myfirstandroidapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sanraboss.myfirstandroidapp.R;

public class MainActivity extends AppCompatActivity {

    private Button takeSelfieButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkCameraHardware(this)) {

        }
        setContentView(R.layout.activity_main);
        initComponents();
    }
    private void initComponents() {
        takeSelfieButton = (Button) findViewById(R.id.take_selfie);
        takeSelfieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}
