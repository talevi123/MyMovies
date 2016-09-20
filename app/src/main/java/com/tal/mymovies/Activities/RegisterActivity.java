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

public class RegisterActivity extends BaseActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fullName = (EditText)findViewById(R.id.name);
        final EditText email = (EditText)findViewById(R.id.email);
        final EditText passowrd = (EditText)findViewById(R.id.password);
        Button registerBtn = (Button) findViewById(R.id.registerBtn);

        sp = getSharedPreferences("key", 0);

        if (registerBtn != null) {
            registerBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String fullNameInput=fullName.getText().toString();
                    String emailInput=email.getText().toString();
                    String passwordInput=passowrd.getText().toString();

                    if( fullNameInput !=null && emailInput !=null && passwordInput !=null) {

                        Intent intent = new Intent(RegisterActivity.this, MoviesListActivity.class);

                        SharedPreferences.Editor sedt = sp.edit();
                        sedt.putString("textvalue2", passwordInput);
                        sedt.commit();

                        intent.putExtra("full_name", fullNameInput);
                        intent.putExtra("email", emailInput);
                        intent.putExtra("password", passwordInput);
                        startActivity(intent);
                    }

                    else {
                        Toast.makeText(RegisterActivity.this, "Please fill all details",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

//SharedPreferences sp = getSharedPreferences("key", 0);
        //String tValue = sp.getString("textvalue","");
        //String tOperative = sp.getString("textvalue2","");
        //Intent in = getIntent();
        //String fullName = in.getStringExtra("full_name");
        //String email = in.getStringExtra("email");
        //String username = in.getStringExtra("username");




    }
}
