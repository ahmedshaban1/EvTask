package eventtustask.com.eventtustask.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.twitter.sdk.android.core.models.Coordinates;
import com.twitter.sdk.android.core.models.Place;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.models.UserEntities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed shabaan on 6/17/2016.
 */
public class TweetSqlite extends SQLiteOpenHelper {


    private static final String TABLE_TWEETS = "TWEETS";
    private static final String KEY_ID = "id";
    private static final String SCREEN_NAME = "SCREEN_NAME";
    private static final String TWEET = "description";
    private static final String IMAGE = "image";
    private static final String DATABASE_NAME = "twitter1";
    private static final int DATABASE_VERSION = 1;


    public TweetSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE =
                "CREATE TABLE " + TABLE_TWEETS + "("
                        + KEY_ID + " BIGINT PRIMARY KEY ,"
                        + SCREEN_NAME + " TEXT,"
                        + TWEET + " TEXT,"
                        + IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWEETS);
        onCreate(db);
    }

    public boolean addItem(Tweet t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, t.getId());
        values.put(SCREEN_NAME, t.user.screenName);
        values.put(TWEET, t.text);
        values.put(IMAGE, t.user.profileImageUrl);
        // Inserting Row
        db.insert(TABLE_TWEETS, null, values);
        Log.i("sucess inserting", "record sucess" + t.getId());
        db.close();
        return true;

    }


    public List<Tweet> getAllItmes() {
        List<Tweet> items = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TWEETS;
        //String selectQuery = "SELECT  * FROM " + TABLE_NEWS ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserEntities userEntities = null;
        if (cursor.moveToFirst()) {

            do {

                User user = new User(false,null,false,false,cursor.getString(2),null,userEntities,1,false,1,1,false,12222,null,false,null,1,null,null,null,null,null,false,null,cursor.getString(3),null,null,null,
                                null,null,
                                false, false,  cursor.getString(1),
                                false,null,1,null,
                                null,1,false,null,
                                null);
                        Tweet item = new Tweet(null,null,null,
                                        null,null,null, false,null,cursor.getLong(0),null, null, 123, null,
                                        123,null, null, null,
                                        false,null,1,false,
                                        null, null, cursor.getString(2), false,user,
                                        false,null,null);
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }


    public void EmptyTable() {
        String selectQuery = " DELETE FROM  " + TABLE_TWEETS;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
        db.close();
    }

}

