package com.tal.mymovies.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tal.mymovies.DB.DBManager;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.MyMoviesApplication;
import com.tal.mymovies.Network.Utility;
import com.tal.mymovies.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditMovieActivity extends AppCompatActivity {

    private ImageView image;
    private Button saveBtn;
    private Button cancelBtn;
    private static final int REQUEST_CAMERA =1;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        boolean result = Utility.checkCameraPermission(EditMovieActivity.this);
        if(result) setCameraBtn();

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
                startActivityForResult(photoCaptureIntent, REQUEST_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_CAMERA == requestCode && resultCode == RESULT_OK){
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
            case Utility.MY_PREMISSION_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {

                }
                break;
            }
        }
    }

    private void edit (){
        final EditText title = (EditText) findViewById(R.id.title);
        final EditText director = (EditText) findViewById(R.id.director);
        final EditText genre = (EditText) findViewById(R.id.genre);
        final EditText min = (EditText) findViewById(R.id.min);
        final EditText rating = (EditText) findViewById(R.id.r);
        final EditText plot = (EditText) findViewById(R.id.plot);

        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getTitle = title.getText().toString();
                String getDirector = director.getText().toString();
                String getGenre = genre.getText().toString();
                String getMin = min.getText().toString();
                String getRating = rating.getText().toString();
                String getPlot = plot.getText().toString();

                long ts = System.currentTimeMillis()/10000;
                String imdbId = String.valueOf(ts);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                byte[] image = Utility.getBytes(bitmap);

                movie = new Movie(imdbId, getTitle, getPlot, image, getMin, timeStamp, getDirector, getGenre, getRating);
                DBManager.getInstance(MyMoviesApplication.getInstance()).addToFav(movie);
                Toast.makeText(EditMovieActivity.this, "Movie add to favorites",Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn = (Button) findViewById(R.id.canBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}






