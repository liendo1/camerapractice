package com.example.camerapractice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE = 42;
    File photoFile;
    String FILE_NAME = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                photoFile = getPhotoFile(FILE_NAME);
                Uri fileProvider = FileProvider.getUriForFile(MainActivity.this,"com.example.camerapractice.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider);


                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent,REQUEST_CODE);
                    Button resetButton =(Button)findViewById(R.id.button_del);
                    resetButton.setVisibility(View.VISIBLE);
                    try {
                        Thread.sleep(1200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Unable to open camera", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private File getPhotoFile(String file_name) {
        //use 'get external filesdir  on context to access package-specific directories
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(file_name,".jpg",storageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storageDirectory;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Bundle taken = data.getExtras();
           // Bitmap imageTaken = (Bitmap) taken.get("data");
            Bitmap imageTaken = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(imageTaken);
        } else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}