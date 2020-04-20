package com.abdulalim.moviespot.FileHelper;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileHelper {

    public void writeToFile(JSONObject jsonObject, Context context){
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("wootlab-movie-db.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(jsonObject.toString());
            outputStreamWriter.close();
        }
        catch (IOException ex){
            Log.e("FileHelper", "Failed to write to file : " + ex.getMessage());
        }
    }
    public JSONObject readFromFile(Context context){
        JSONObject jsonObject = null;
        try {
            String returnValue;
            InputStream inputStream = context.openFileInput("wootlab-movie-db.json");
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null){
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                returnValue = stringBuilder.toString();
                jsonObject = new JSONObject(returnValue);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public boolean fileExists(Context context){
        File file = new File(context.getFilesDir(), "wootlab-movie-db.json");
        Log.i("FILE", "File : " + file.exists());
        return file.exists();
    }
}
