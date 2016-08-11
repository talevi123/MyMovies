package com.tal.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.tal.mymovies.R;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = (TextView) findViewById(R.id.title);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        Button   loginBtn = (Button)findViewById(R.id.login_button);


        loginBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String usernameInput=username.getText().toString();
                String passwordInput=password.getText().toString();

                if(usernameInput !=null && passwordInput !=null) {
                    Intent intent = new Intent(LoginActivity.this, MoviesListActivity.class);
                    intent.putExtra("username", usernameInput);
                    intent.putExtra("password", passwordInput);
                    startActivity(intent);
                }

                else {
                    Toast.makeText(LoginActivity.this, "Please fill Password or username",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
