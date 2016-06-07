package com.enterra.android.intentchooserdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView phoneNumber;
    TextView camera;
    CoordinatorLayout container;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkNecessaryPermissions();
    }

    private void checkNecessaryPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    private void initViews() {
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        camera = (TextView) findViewById(R.id.camera);
        container = (CoordinatorLayout) findViewById(R.id.container);
    }

    private void setEvents() {
        phoneNumber.setOnClickListener(this);
        camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Check for CALL_PHONE permission
        switch (v.getId()) {
            case R.id.phoneNumber:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] { Manifest.permission.CALL_PHONE },
                        CALL_PHONE_PERMISSION_REQUEST_CODE);
                } else {
                    startCallPhone();
                }
                break;
            case R.id.camera:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] { Manifest.permission.CAMERA },
                        CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    startPhotoShooter();
                }
                break;
        }
    }

    private void startPhotoShooter() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    private void startCallPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL,
            Uri.fromParts("tel", phoneNumber.getText().toString(), null));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_PHONE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCallPhone();
                } else {
                    Snackbar.make(container, "Permission was not granted", Snackbar.LENGTH_SHORT)
                        .show();
                }
                break;
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPhotoShooter();
                } else {
                    Snackbar.make(container, "Permission was not granted", Snackbar.LENGTH_SHORT)
                        .show();
                }
                break;
            case WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do nothing
                    Toast.makeText(this, "Enjoy using our app!",
                        Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(container, "Permission was not granted", Snackbar.LENGTH_SHORT)
                        .show();
                }
                break;
            default:
                Snackbar.make(container, "UnknownRequestCode:" + String.valueOf(requestCode),
                    Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}
