package com.abdulalim.moviespot.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.abdulalim.moviespot.Constants;
import com.abdulalim.moviespot.DBHelper.DB;
import com.abdulalim.moviespot.FileHelper.FileHelper;
import com.abdulalim.moviespot.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIMEOUT = 3000;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = getSharedPreferences(Constants.SharedPrefs, Context.MODE_PRIVATE);

        //Change activity after 3 seconds
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkFileSettings();
            }
        }, SPLASH_TIMEOUT);
    }

    void checkFileSettings(){
        //CHeck if File exists
        FileHelper fileHelper = new FileHelper();
        if(fileHelper.fileExists(this)){
            //Check if user ticked "Remember Me" during last login
            try{
                JSONObject jsonObject = fileHelper.readFromFile(this);
                boolean rememberMe = jsonObject.getBoolean("rememberMe");
                if (rememberMe){
                    //Fetch userName and Password from file
                    String username = jsonObject.getString("username");
                    String password = jsonObject.getString("password");

                    DB db = new DB(this);
                    int userID = db.sign_in_username_and_password(username, password);

                    //userID will be stored in SharedPreferences to be used later in other activities without using the Intents to pass data
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userID", userID);
                    editor.apply();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish(); //This destroys the current activity.
                    return;
                }
            }catch (JSONException e) {
                e.printStackTrace();
                //Go to LoginScreen
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
                return;
            }
        }
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}
