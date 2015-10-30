package com.example.bursny.talent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import android.content.pm.Signature;

import org.json.JSONException;

public class login extends Activity {

    private EditText username;
    private EditText password;

    private ParseUser currentUser;

    private Intent mainIntent;

    final List<String> permissions = Arrays.asList("public_profile", "email");

    private String email;
    private String name;

    Profile fbProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainIntent = new Intent(login.this, main.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


        //check if logged in

        currentUser = ParseUser.getCurrentUser();
        fbProfile = Profile.getCurrentProfile();
        

        if (currentUser != null) {
            //currentUser.logOut();
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


        Button fb_button = (Button) findViewById(R.id.fb_button);
        fb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb_login();
            }
        });


    }

    private void fb_login() {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            private int fb_success = 1;

            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                    fb_success = 0;
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    get_fb_user_info();
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    get_parse_user_info();
                }

                if (fb_success == 1) {
                    startActivity(mainIntent);
                }
            }
        });
    }

    private void get_fb_user_info() {
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    try {
                        email = response.getJSONObject().getString("email");
                    } catch (JSONException e) {
                        email = null;
                    }
                    Log.d("MyApp", "Getting name");
                    name = response.getJSONObject().getString("name");
                    save_fb_user();
                } catch (JSONException e) {
                    Log.d("MyApp", e.toString());
                }
            }
        }).executeAsync();
    }

    private void get_parse_user_info() {
        currentUser = ParseUser.getCurrentUser();

        Toast.makeText(login.this, "Welcome back " + username.toString(), Toast.LENGTH_SHORT).show();

    }


    private void save_fb_user() {
        Log.d("MyApp", "Saving user...");
        currentUser = ParseUser.getCurrentUser();
        currentUser.setUsername(name);
        if (email != null) {
            currentUser.setEmail(email);
        }

        currentUser.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(login.this,"User signed up successfully", Toast.LENGTH_LONG).show();
                Log.d("MyApp", "User signed up successfully");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
