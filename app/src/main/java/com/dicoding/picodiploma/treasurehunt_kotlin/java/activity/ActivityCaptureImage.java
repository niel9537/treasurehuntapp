package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import static com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config.MY_CAMERA_PERMISSION_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    BitmapDrawable drawable;
    Bitmap bitmap;
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
        if (requestCode == Config.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            btnOpen.setVisibility(View.INVISIBLE);
            txtHint.setVisibility(View.INVISIBLE);
            btnUpload.setVisibility(View.VISIBLE);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgOvj.setImageBitmap(photo);

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // share();
                    Intent intent = new Intent(ActivityCaptureImage.this,ActivityPlayGame.class);
                    intent.putExtra("FLOW_ID",FLOW_ID);
                    intent.putExtra("STATUS", Config.CAPTURE_IMAGE);
                    startActivity(intent);
                }
            });
        }
    }

/*    private void share(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        drawable = (BitmapDrawable)  imgOvj.getDrawable();
        bitmap = drawable.getBitmap();
        File file = new File(getExternalCacheDir()+"/"+"treasurehunt"+".png");
        Intent intent = null;
        try{
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
        startActivity(Intent.createChooser(intent,"Share image via : "));
    }*/

}
