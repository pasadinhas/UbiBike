package pt.ulisboa.tecnico.cmu.ubibike.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cedac on 02/05/16.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    public static final String TAG = "ubibke";

    public static final String DB_NAME = "Ubibike.db";

    public static final String MESSAGES_TABLE = "messages_table";
    public static final String TIME = "TIMESTAMP";
    public static final String USERNAME = "USERNAME";
    public static final String CONTENT = "CONTENT";

    public static final String PEERS_TABLE = "peers_table";
    public static final String IP = "IP";
    public static final String PEERNAME = "USERNAME";

    private static DatabaseManager instance = null;


    private DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, 1);
    }

    private DatabaseManager(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +
                    MESSAGES_TABLE +
                        " (" +
                TIME + " INTEGER PRIMARY KEY, " +
                USERNAME + " TEXT, " +
                CONTENT + " TEXT" +
                        ")");

        db.execSQL("create table " +
                PEERS_TABLE +
                " (" +
                IP + " TEXT PRIMARY KEY, " +
                PEERNAME + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PEERS_TABLE);
        onCreate(db);
    }

    public boolean insertMessage(String username, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis());
        values.put(USERNAME, username);
        values.put(CONTENT, content);

        long result = db.insert(MESSAGES_TABLE, null, values);

        if (result == -1) {
            return false;
        } else {
            Log.d(TAG, "insertMessage: inserted: a-time " + username + " " + content);
            return true;
        }
    }

    public Cursor getMessages (String ownUsername, String otherUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * from " + MESSAGES_TABLE +
                    " WHERE " + USERNAME + "=? OR " + USERNAME + "=?" +
                    " ORDER BY " + TIME;

        String[] args = new String[] {ownUsername, otherUsername};

        Cursor res = db.rawQuery(query, args);

        return res;
    }

    public boolean insertPeer(String ip, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "Select * from " + PEERS_TABLE + " where " + IP + " =?";
        String[] data = new String[] {ip};
        Cursor cursor = db.rawQuery(query, data);

        boolean success = false;

        if(cursor.getCount() <= 0){
            ContentValues values = new ContentValues();
            values.put(IP, ip);
            values.put(PEERNAME, name);

            long result = db.insert(PEERS_TABLE, null, values);

            success = (result == -1) ? false : true;
        }

        cursor.close();
        return success;
    }

    public Set<Pair<String, String>> getPeersSet() {
        Set<Pair<String, String>> output = new HashSet<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * from " + PEERS_TABLE;

        Cursor res = db.rawQuery(query, null);

        if(res.moveToFirst()) {
            do {
                String rowIP = res.getString(res.getColumnIndex(IP));
                String rowName = res.getString(res.getColumnIndex(PEERNAME));
                output.add(new Pair<>(rowIP, rowName));
            } while (res.moveToNext());
        }
        return output;
    }
}
