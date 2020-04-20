package com.abdulalim.moviespot.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.abdulalim.moviespot.DBHelper.DB;
import com.abdulalim.moviespot.DBHelper.User;
import com.abdulalim.moviespot.R;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    EditText E_firstname, E_lastname, E_username, E_email, E_password, E_password_confirm;
    String firstname, lastname, username, email, password, password_confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initViews();
    }

    private void initViews() {
        E_firstname = findViewById(R.id.s_firstname);
        E_lastname = findViewById(R.id.s_lastname);
        E_username = findViewById(R.id.s_username);
        E_email = findViewById(R.id.s_email);
        E_password = findViewById(R.id.s_password);
        E_password_confirm = findViewById(R.id.s_password_confirm);
    }


    public void SignUP(View view){
        firstname = E_firstname.getText().toString();
        lastname = E_lastname.getText().toString();
        username = E_username.getText().toString();
        email = E_email.getText().toString();
        password = E_password.getText().toString();
        password_confirm = E_password_confirm.getText().toString();

        if(validateFields()){ //If all fields are valid
            //Login here
            DB db = new DB(this);
            User user = new User();
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            db.createUser(user);
            Toast.makeText(this, "Account created succesfully!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    private boolean validateFields() {
        if(!firstname.isEmpty()){
            if(!lastname.isEmpty()){
                if(!username.isEmpty()){
                    if(!email.isEmpty()){
                        if(!password.isEmpty()){
                            if(!password_confirm.isEmpty()){
                                if(password.equals(password_confirm)){
                                    //Fields are valid
                                    return true;
                                }else {
                                    E_password.setError("Passwords do not match!");
                                    E_password_confirm.setError("Passwords do not match!");
                                }
                            }else {
                                E_password_confirm.setError("Field cannot be left blank!");
                            }
                        }else {
                            E_password.setError("Field cannot be left blank!");
                        }
                    }else {
                        E_email.setError("Field cannot be left blank!");
                    }
                }else {
                    E_username.setError("Field cannot be left blank!");
                }
            }else {
                E_lastname.setError("Field cannot be left blank!");
            }
        }else {
            E_firstname.setError("Field cannot be left blank!");
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    public void backToLogin(View v)
    {
        onBackPressed();
    }
}
