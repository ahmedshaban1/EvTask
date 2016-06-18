package eventtustask.com.eventtustask.Sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.models.UserEntities;

import java.util.ArrayList;
import java.util.List;

public class FollowersSqlite extends SQLiteOpenHelper {


    private static final String TABLE_FOLLOWERS = "FOLLOWERS";
    private static final String KEY_ID = "id";
    private static final String NAME = "NAME";
    private static final String SCREEN_NAME = "SCREEN_NAME";
    private static final String BIO = "description";
    private static final String IMAGE = "image";
    private static final String BACKGROUND = "BACKGROUND";

    private static final String DATABASE_NAME = "twitter";
    private static final int DATABASE_VERSION = 1;


    public FollowersSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE =
                "CREATE TABLE " + TABLE_FOLLOWERS + "("
                        + KEY_ID + " BIGINT PRIMARY KEY ,"
                        + SCREEN_NAME + " TEXT,"
                        + BIO + " TEXT,"
                        + NAME + " TEXT,"
                        + IMAGE + " TEXT ,"
                        +  BACKGROUND + " TEXT  "+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWERS);
        onCreate(db);
    }

    public boolean addItem(User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, u.getId());
        values.put(SCREEN_NAME, u.screenName);
        values.put(BIO, u.description);
        values.put(NAME, u.name);
        values.put(IMAGE, u.profileImageUrl);
        values.put(BACKGROUND, u.profileBannerUrl);
        // Inserting Row
        db.insert(TABLE_FOLLOWERS, null, values);
        Log.i("sucess inserting", "record sucess" + u.getId());
        db.close();
        return true;

    }


    public List<User> getAllItmes() {
        List<User> items = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FOLLOWERS ;
        //String selectQuery = "SELECT  * FROM " + TABLE_NEWS ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserEntities userEntities  = null;
        if (cursor.moveToFirst()) {
            do {
                User item = new User(false,null,false,false,cursor.getString(2),null,userEntities,1,false,1,1,false,
                        cursor.getLong(0),null,false,null,1,null,cursor.getString(3),null,null,null,false,cursor.getString(5),cursor.getString(4),null,null,null,
                        null,null,
                        false, false,  cursor.getString(1),
                        false,null,1,null,
                        null,1,false,null,
                        null);
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }
    public User get_by_id(long id) {
        List<User> items = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FOLLOWERS +" WHERE "+KEY_ID+"="+id ;
        //String selectQuery = "SELECT  * FROM " + TABLE_NEWS ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserEntities userEntities  = null;
        if (cursor.moveToFirst()) {
            do {
                User item = new User(false,null,false,false,cursor.getString(2),null,userEntities,1,false,1,1,false,cursor.getLong(0),null,false,null,1,null,cursor.getString(3),null,null,null,false,cursor.getString(5),cursor.getString(4),null,null,null,
                        null,null,
                        false, false,  cursor.getString(1),
                        false,null,1,null,
                        null,1,false,null,
                        null);
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items.get(0);
    }






   public  void  EmptyTable(){
        String selectQuery =  " DELETE FROM  " + TABLE_FOLLOWERS ;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
        db.close();
    }


}


