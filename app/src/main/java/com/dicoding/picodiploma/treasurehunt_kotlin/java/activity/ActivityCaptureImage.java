package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import static com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config.MY_CAMERA_PERMISSION_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.File;
import com.google.zxing.Result;

import java.io.FileOutputStream;


public class ActivityCaptureImage extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String FLOW_ID = "";
    ImageView imgOvj;
    TextView btnOpen;
    TextView btnUpload;
    TextView txtHint;
    TextView txtTitle;
    BitmapDrawable drawable;
    Bitmap bitmap;
    boolean isUpload = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dialog_ovj_capture_image);
        setContentView(R.layout.activity_open_camera);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FLOW_ID= extras.getString("FLOW_ID");
            Log.d("FLOW_ID CAMERA", " : " + FLOW_ID);
            //The key argument here must match that used in the other activity
        }
        txtTitle = findViewById(R.id.txtTitle);
        imgOvj = findViewById(R.id.imgOvj);
        txtHint = findViewById(R.id.txtHint);
        btnUpload= findViewById(R.id.btnUpload);
        btnOpen= findViewById(R.id.btnOpen);
        btnUpload.setVisibility(View.INVISIBLE);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, Config.CAMERA_REQUEST);
                }
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Config.CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Activity","requestCode : "+requestCode+" resultCode : "+resultCode);

        if (requestCode == Config.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            isUpload = false;
            btnOpen.setVisibility(View.INVISIBLE);
            txtHint.setVisibility(View.INVISIBLE);
            btnUpload.setVisibility(View.VISIBLE);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgOvj.setImageBitmap(photo);
            txtTitle.setText("UPLOAD FOTOMU");
            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    shareImageWhatsApp();
                   /* Intent intent = new Intent(ActivityCaptureImage.this,ActivityPlayGame.class);
                    intent.putExtra("FLOW_ID",FLOW_ID);
                    intent.putExtra("STATUS", Config.CAPTURE_IMAGE);
                    startActivity(intent);*/
                }
            });
        }
        if(isUpload == true){
            Intent intent = new Intent(ActivityCaptureImage.this,ActivityPlayGame.class);
            intent.putExtra("FLOW_ID",FLOW_ID);
            intent.putExtra("STATUS", Config.CAPTURE_IMAGE);
            startActivity(intent);
        }
        if (requestCode == 100) {
            if(resultCode == Activity.RESULT_OK){
                Intent intent = new Intent(ActivityCaptureImage.this,ActivityPlayGame.class);
                intent.putExtra("FLOW_ID",FLOW_ID);
                intent.putExtra("STATUS", Config.CAPTURE_IMAGE);
                startActivity(intent);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    public void shareImageWhatsApp() {

        Bitmap imgBitmap = ((BitmapDrawable)imgOvj.getDrawable()).getBitmap();
        //Bitmap imgBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.hutan_phalaka);
        String imgBitmapPath= MediaStore.Images.Media.insertImage(getContentResolver(),imgBitmap,"Keseruanku",null);
        if(imgBitmapPath!=null){
            Uri imgBitmapUri=Uri.parse(imgBitmapPath);
            Intent shareIntent=new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM,imgBitmapUri);
            //startActivity(Intent.createChooser(shareIntent,"Bagikan foto melalui"));
//            ActivityCaptureImage.this.setResult(RESULT_OK);
            isUpload = true;
            startActivityForResult(Intent.createChooser(shareIntent,"Bagikan foto melalui"), 100);

        }else{
            Toast.makeText(ActivityCaptureImage.this,""+imgBitmapPath.toString(),Toast.LENGTH_LONG).show();
        }

    }

}
