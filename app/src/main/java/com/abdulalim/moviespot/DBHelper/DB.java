package com.abdulalim.moviespot.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {
    private static String DB_NAME = "wootlab_.db";
    private static String TABLE_USER = "USER";
    private static String TABLE_FAV = "FAVORITES";

    private static final String TABLE_USER_DROP_IF_EXIST = "drop table if exists " + TABLE_USER;
    private static final String TABLE_FAV_DROP_IF_EXIST = "drop table if exists " + TABLE_FAV;

    public DB(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USER + " (" +
                "" +Columns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                "" +Columns.FIRSTNAME+" TEXT," +
                "" +Columns.LASTNAME+" TEXT," +
                "" +Columns.USERNAME+" TEXT," +
                "" +Columns.EMAIL+" TEXT," +
                "" +Columns.PASSWORD+" TEXT" +
                ")"
        );
        db.execSQL("create table " + TABLE_FAV + " (" +
                "" +Columns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                "" +Columns.USER_ID+" INTEGER," +
                "" +Columns.MOVIEID+" INTEGER," +
                "" +Columns.TITLE+" TEXT," +
                "" +Columns.POSTER+" TEXT" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_FAV_DROP_IF_EXIST);
        db.execSQL(TABLE_USER_DROP_IF_EXIST);
        onCreate(db);
    }

    //Create a new User
    public void createUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.ID, user.getId());
        contentValues.put(Columns.FIRSTNAME, user.getFirstname());
        contentValues.put(Columns.LASTNAME, user.getLastname());
        contentValues.put(Columns.EMAIL, user.getEmail());
        contentValues.put(Columns.USERNAME, user.getUsername());
        contentValues.put(Columns.PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, contentValues);
    }

    //Check if username and password combination exists in the DB
    public int sign_in_username_and_password(String username, String password){
        int userID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+Columns.ID+" from "+TABLE_USER+" where "+Columns.USERNAME+"='"+username+"' and "+Columns.PASSWORD+"='"+password+"'", null);
        if(res.getCount() > 0){
            res.moveToFirst();
            userID = Integer.parseInt(res.getString(0));
        }else {
            userID = -1;
        }
        res.close();
        return userID;
    }

    //GetUserDetails
    public User fetchUserDetails(int id){
        User user = new User();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_USER+" where "+Columns.ID+"='"+id+"'", null);
        if(res.getCount() > 0){
            res.moveToFirst();
            user.setFirstname(res.getString(1));
            user.setLastname(res.getString(2));
            user.setUsername(res.getString(3));
            user.setEmail(res.getString(4));
        }else {
            return null;
        }
        res.close();
        return user;
    }



    //GetFavMoviesByUserID
    public List<Movie> fetchFavMovies(int userID){
        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_FAV+" where "+Columns.USER_ID+"='"+userID+"'", null);
        if(res.getCount() > 0){
        res.moveToFirst();
        while (!res.isAfterLast()){
            Movie movie = new Movie();
            movie.setMovieID(Integer.parseInt(res.getString(2)));
            movie.setTitle(res.getString(3));
            movie.setPosterPath(res.getString(4));
            movies.add(movie);
            res.moveToNext();
            }
        }
        res.close();
        return movies;
    }


    //Add new Movie
    public void addFavMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.USER_ID, movie.getUserID());
        contentValues.put(Columns.MOVIEID, movie.getMovieID());
        contentValues.put(Columns.TITLE, movie.getTitle());
        contentValues.put(Columns.POSTER, movie.getPosterPath());
        db.insert(TABLE_FAV, null, contentValues);
    }

    //Remove movie from Fav
    public void removeFavMovie(Movie movie){
        //Accept userID and MoveID
        //Remove movie where userID and movieID match the passed info
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAV, Columns.USER_ID+"="+movie.getUserID()+" and "+Columns.MOVIEID+"="+movie.getMovieID()+"", null);
    }

    //Check if movie is in Favorite list.
    public boolean isInFavorites(int userID, int movieID){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+Columns.ID+" from "+TABLE_FAV+" where "+Columns.USER_ID+"="+userID+" and "+Columns.MOVIEID+"="+movieID+"", null);
        if(res.getCount() > 0){
            result = true;
        }
        res.close();
        return result;
    }

    private static class Columns{
        //Below Columns will be in both tables
        private static final String USER_ID = "USER_ID";
        private static final String ID = "ID";

        //Below Columns will be in USER table
        private static final String FIRSTNAME = "FIRSTNAME";
        private static final String LASTNAME = "LASTNAME";
        private static final String USERNAME = "USERNAME";
        private static final String EMAIL = "EMAIL";
        private static final String PASSWORD = "PASSWORD";

        //Below Columns will be in FAVORITES table
        private static final String TITLE = "TITLE";
        private static final String MOVIEID = "MOVIEID";
        private static final String POSTER = "POSTER";
    }
}
