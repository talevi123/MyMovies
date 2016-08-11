package com.tal.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.tal.mymovies.R;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        TextView title = (TextView) findViewById(R.id.title);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        Button loginBtn = (Button) findViewById(R.id.login_button);
        Button regBtn = (Button) findViewById(R.id.main_register_button);

        assert loginBtn != null;
        loginBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String usernameInput = username.getText().toString();
                String passwordInput = password.getText().toString();

                if (usernameInput != null && passwordInput != null) {
                    Intent intent = new Intent(LoginActivity.this, MoviesListActivity.class);
                    intent.putExtra("username", usernameInput);
                    intent.putExtra("password", passwordInput);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill Password or username",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }
}
