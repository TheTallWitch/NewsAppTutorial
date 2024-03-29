package com.developercat.newsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "news.db";
    private static final String TABLE_NAME = "news_table";

    private static final String ID = "ID";
    private static final String NEWS_ID = "newsId";
    private static final String NEWS_TITLE = "newsTitle";
    private static final String NEWS_SUB_TITLE = "newsSubTitle";
    private static final String NEWS_TEXT = "newsText";
    private static final String NEWS_IMAGE = "newsImage";
    private static final String NEWS_IMAGE_DESC = "newsImageDesc";
    private static final String NEWS_TIME = "newsTime";
    private static final String NEWS_PUBLISHER = "newsPublisher";

    public DatabaseHelper(Context context) {
        //database gets created here
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NEWS_ID + " INTEGER, " + NEWS_TITLE + " TEXT, " + NEWS_SUB_TITLE + " TEXT, " + NEWS_TEXT + " TEXT, " + NEWS_IMAGE + " TEXT, " + NEWS_IMAGE_DESC + " TEXT, " + NEWS_TIME + " TEXT, " + NEWS_PUBLISHER + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNews(int newsId, String title, String subTitle, String text, String image, String imageDesc, String time, String publisher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS_ID, newsId);
        contentValues.put(NEWS_TITLE, title);
        contentValues.put(NEWS_SUB_TITLE, subTitle);
        contentValues.put(NEWS_TEXT, text);
        contentValues.put(NEWS_IMAGE, image);
        contentValues.put(NEWS_IMAGE_DESC, imageDesc);
        contentValues.put(NEWS_TIME, time);
        contentValues.put(NEWS_PUBLISHER, publisher);
        long result = db.insert(TABLE_NAME, null, contentValues); //-1 -> failed, else returns ID
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public List<NewsItem> getAllNews() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<NewsItem> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while (cursor.moveToNext()) {
            list.add(new NewsItem(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8)));
        }

        return list;
    }

    public NewsItem getNewsItem(int newsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        NewsItem item = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + NEWS_ID + " = ?", new String[] { String.valueOf(newsId) });

        if (cursor.moveToFirst()) {
            item = new NewsItem(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
        }

        return item;
    }

    public boolean updateNews(int newsId, String title, String subTitle, String text, String image, String imageDesc, String time, String publisher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS_TITLE, title);
        contentValues.put(NEWS_SUB_TITLE, subTitle);
        contentValues.put(NEWS_TEXT, text);
        contentValues.put(NEWS_IMAGE, image);
        contentValues.put(NEWS_IMAGE_DESC, imageDesc);
        contentValues.put(NEWS_TIME, time);
        contentValues.put(NEWS_PUBLISHER, publisher);

        db.update(TABLE_NAME, contentValues, NEWS_ID + " = ?", new String[] {String.valueOf(newsId)});
        return true;
    }

    public int deleteNews(int newsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, NEWS_ID + " = ?", new String[] {String.valueOf(newsId)});
        return result;
    }

    public int getFirstNewsId() {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;

        Cursor cursor = db.rawQuery("SELECT " + NEWS_ID + " FROM " + TABLE_NAME + " ORDER BY ID ASC LIMIT 1", null);

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        return id;
    }
}
