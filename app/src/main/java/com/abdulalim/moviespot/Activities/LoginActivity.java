package com.abdulalim.moviespot.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.abdulalim.moviespot.Constants;
import com.abdulalim.moviespot.DBHelper.DB;
import com.abdulalim.moviespot.FileHelper.FileHelper;
import com.abdulalim.moviespot.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    CheckBox C_remember_me;
    EditText E_username, E_password;
    String username, password;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        sharedPreferences = getSharedPreferences(Constants.SharedPrefs, Context.MODE_PRIVATE);

        initViews();

    }

    private void initViews() {
        C_remember_me = findViewById(R.id.rememberMe);

        E_username = findViewById(R.id.l_username);
        E_password = findViewById(R.id.l_password);
    }

    public void login(View view){
        username = E_username.getText().toString();
        password = E_password.getText().toString();

        if(validateFields()){
            DB db = new DB(this);
            //Logging in will provide the user's ID.
            //Which will be used to fetch Favourite movies selected by the user
            int userID = db.sign_in_username_and_password(username, password);

            //userID will be stored in SharedPreferences to be used later in other activities without using the Intents to pass data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userID", userID);
            editor.apply();

            if (C_remember_me.isChecked()){
                //Write username and password to file
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("rememberMe", true);
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FileHelper fileHelper = new FileHelper();
                fileHelper.writeToFile(jsonObject, this);
            }

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish(); //This destroys the current activity.
        }

    }

    private boolean validateFields() {
        if(!username.isEmpty()){
            if(!password.isEmpty()){
                return true;
            }else {
                E_password.setError("Field cannot be left blank!");
            }
        }else {
            E_username.setError("Field cannot be left blank!");
        }
        return false;
    }

    public void toSignUp(View view){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        //Below line, overrides the transition between activites switching.
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }
}
