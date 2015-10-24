package com.example.bursny.talent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class login extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainIntent = new Intent(login.this, main.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


        //check if logged in

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            startActivity(mainIntent);
        } //else continue to login page



        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        Button signup_button = (Button) findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to signup page
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });

        Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login
                login();
            }
        });


    }

    private void login() {
        String username_txt = username.getText().toString().trim();
        String password_txt = password.getText().toString().trim();


        final ProgressDialog login_progress = ProgressDialog.show(this, null, "Logging in...");

        ParseUser.logInInBackground(username_txt, password_txt, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                login_progress.dismiss();
                if (e == null) {
                    //Successfully logged in
                    startActivity(mainIntent);
                } else {
                    Toast.makeText(login.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
