package com.tal.mymovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tal.mymovies.R;


public class LoginActivity extends BaseActivity{

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Button   loginBtn = (Button) findViewById(R.id.login_button);
        TextView regLink = (TextView) findViewById(R.id.link_sign_up);


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

        regLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
