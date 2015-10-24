package com.example.bursny.talent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class signup extends AppCompatActivity {

    private EditText username;
    private EditText password1;
    private EditText password2;
    private EditText email;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = (EditText) findViewById(R.id.username);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        email = (EditText) findViewById(R.id.email);

        Button signup = (Button) findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_account();
            }
        });
    }

    private void create_account() {
        String username_txt = username.getText().toString().trim();
        String password_1_txt = password1.getText().toString().trim();
        String password_2_txt = password2.getText().toString().trim();
        String email_txt = email.getText().toString().trim();

        String str_error = null;

        //Checking if valid user and pass
        if (username_txt.length() == 0) {
            str_error = "Error: Username required";
        } else if (email_txt.length() == 0 ) {
            str_error = "Error: Email required";
        }else if (password_1_txt.length() == 0) {
            str_error = "Error: Password required";
        } else if (!password_1_txt.equals(password_2_txt)) {
            str_error = "Error: Passwords do not match";
        }

        if (str_error != null) {
            Toast.makeText(signup.this, str_error, Toast.LENGTH_LONG).show();
            return;
        }

        ParseUser user = new ParseUser();

        user.setUsername(username_txt);
        user.setPassword(password_1_txt);
        user.setEmail(email_txt);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(signup.this, login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    //error occured
                    Toast.makeText(signup.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
