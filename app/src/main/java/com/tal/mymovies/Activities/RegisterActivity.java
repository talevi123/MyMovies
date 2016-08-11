package com.tal.mymovies.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tal.mymovies.R;

public class RegisterActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fullName = (EditText) findViewById(R.id.reg_full_name);
        final EditText email = (EditText) findViewById(R.id.reg_email);
        final EditText username = (EditText) findViewById(R.id.reg_username);
        final EditText passowrd = (EditText) findViewById(R.id.reg_password);
        Button registerBtn = (Button) findViewById(R.id.registerBtn);

        sp = getSharedPreferences("user", MODE_PRIVATE);

        assert registerBtn != null;
        registerBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String fullNameInput = fullName.getText().toString();
                String emailInput = email.getText().toString();
                String usernameInput = username.getText().toString();
                String passwordInput = passowrd.getText().toString();

                if (fullNameInput != null && emailInput != null && passwordInput != null && usernameInput != null) {

                    Intent intent = new Intent(RegisterActivity.this, MoviesListActivity.class);

                    SharedPreferences.Editor sedt = sp.edit();
                    sedt.putString("username", usernameInput);
                    sedt.putString("password", passwordInput);
                    sedt.commit();

                    intent.putExtra("full_name", fullNameInput);
                    intent.putExtra("email", emailInput);
                    intent.putExtra("username", usernameInput);
                    intent.putExtra("password", passwordInput);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
