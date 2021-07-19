package sg.edu.rp.c346.id20016584.ndpsong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String COLUMN_ID="_id";
    private static final String DATABASE_NAME = "song.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SONG = "song";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SINGERS="singers";
    private static final String COLUMN_YEAR="year";
    private static final String COLUMN_STARS="stars";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSongTableSql="CREATE TABLE "+ TABLE_SONG+"("
                + COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SINGERS+ " TEXT,"
                + COLUMN_STARS+ " INTEGER, "
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_TITLE+ " TEXT ) ";
        db.execSQL(createSongTableSql);
        Log.i("info", "created tables");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + TABLE_SONG + " ADD COLUMN  song_name TEXT ");
    }
    public long insertsong(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        long result = db.insert(TABLE_SONG, null, values);
        db.close();
        Log.d("SQL Insert","ID:"+ result); //id returned, shouldn’t be -1
        return result;
    }
    public ArrayList<song> getAllSong(){
        ArrayList<song> songs=new ArrayList<song>();

        String selectQuery="SELECT "+COLUMN_ID+" ,"
                +COLUMN_TITLE+ COLUMN_YEAR+
                COLUMN_STARS +COLUMN_SINGERS+" FROM "+TABLE_SONG;

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                int id= cursor.getInt(0);
                String title= cursor.getString(1);
                song Song= new song(id, title);
                songs.add(Song);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songs;
    }
    public int updatesong(song data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, data.getTitle());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.get_id())};
        int result = db.update(TABLE_SONG, values, condition, args);
        db.close();
        return result;
    }
    public int deletesong(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_SONG, condition, args);
        db.close();
        return result;
    }
    public ArrayList<song> getAllSong(String keyword) {
        ArrayList<song> songs = new ArrayList<song>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns= {COLUMN_ID, COLUMN_TITLE};
        String condition = COLUMN_TITLE + " Like ?";
        String[] args = { "%" +  keyword + "%"};
        Cursor cursor = db.query(TABLE_SONG, columns, condition, args,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                song Song = new song(id, title);
                songs.add(Song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songs;
    }
}
