package com.tal.mymovies.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tal.mymovies.R;

public class EditMovieActivity extends AppCompatActivity {

    private ImageView image;
    private Button saveBtn;
    private Button cancelBtn;
    private static final int CAMERA_PICK =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        setCameraBtn();
        edit();
    }

    public void setCameraBtn(){
        Button capturedImageButton = (Button)findViewById(R.id.camBtn);
        image = (ImageView) findViewById(R.id.icon);
        capturedImageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, CAMERA_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(CAMERA_PICK == requestCode && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            image.setImageBitmap(bitmap);
        }
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PICK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_PICK);
                } else {

                }
                break;
        }
    }

    private void edit (){
        EditText title = (EditText) findViewById(R.id.title);
        EditText director = (EditText) findViewById(R.id.director);
        EditText genre = (EditText) findViewById(R.id.genre);
        EditText min = (EditText) findViewById(R.id.min);
        EditText rating = (EditText) findViewById(R.id.r);
        EditText plot = (EditText) findViewById(R.id.plot);

        String getTitle = title.getText().toString();
        String getDirector = director.getText().toString();
        String getGenre = genre.getText().toString();
        String getMin = min.getText().toString();
        String getRating = rating.getText().toString();
        String getPlot = plot.getText().toString();

        long ts = System.currentTimeMillis()/1000;
        String imdbId = String.valueOf(ts);

        saveBtn = (Button) findViewById(R.id.CanBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  DBManager.getInstance(MyMoviesApplication.getInstance()).addToFav(movie);
            }
        });

        cancelBtn = (Button) findViewById(R.id.SaveBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}






