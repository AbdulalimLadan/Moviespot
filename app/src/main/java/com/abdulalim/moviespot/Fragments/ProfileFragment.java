package com.abdulalim.moviespot.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.abdulalim.moviespot.Activities.LoginActivity;
import com.abdulalim.moviespot.Constants;
import com.abdulalim.moviespot.DBHelper.DB;
import com.abdulalim.moviespot.DBHelper.User;
import com.abdulalim.moviespot.FileHelper.FileHelper;
import com.abdulalim.moviespot.R;

import org.json.JSONObject;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    TextView T_fullname, T_email, T_username, T_signout;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Constants.SharedPrefs, Context.MODE_PRIVATE);
        initViews(v);

        return v;
    }

    private void initViews(View v) {
        T_fullname = v.findViewById(R.id.profile_full_name);
        T_email = v.findViewById(R.id.profile_email);
        T_username = v.findViewById(R.id.profile_username);
        T_signout = v.findViewById(R.id.profile_signout);

        int userID = sharedPreferences.getInt("userID", 0);

        //Fetch UserDetails from DB
        DB db = new DB(getActivity().getApplicationContext());
        User user = db.fetchUserDetails(userID);

        String fullname = user.getFirstname()+" "+user.getLastname();
        String email = user.getEmail();
        String username = user.getUsername();

        T_fullname.setText(fullname);
        T_email.setText(email);
        T_username.setText(username);

        T_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }





    private void SignOut()
    {
        //Clear out the json file
        FileHelper fileHelper = new FileHelper();
        fileHelper.writeToFile(new JSONObject(), getActivity().getApplicationContext());

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
    public void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage("Are you sure you want to sign out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SignOut();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
