package com.example.dashbard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QuestionDB";
    private static final String TABLE_QUESTIONS = "Questions";

    private static final String KEY_ID = "id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER = "answer";

    private static final String[] COLUMNS = {KEY_ID, KEY_QUESTION, KEY_ANSWER};

    public MySQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL Statement to create Book table
        String CREATE_BOOK_TABLE = "CREATE TABLE Questions ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "question TEXT, " + "answer TEXT )";
        // create books table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        // create fresh books table
        this.onCreate(db);
    }

    public void addBook(Question question) {
        //for logging
        Log.d("addQuestion", question.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.getmTextRest());
        // get title
        values.put(KEY_ANSWER, question.ismAnswerTrues());
        // get author // 3. insert
        db.insert(TABLE_QUESTIONS,
                // table
                null,
                // nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
    }

    public Question getBook(int id) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor = db.query(TABLE_QUESTIONS, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        // 3. if we got results get the first one
        if (cursor != null) cursor.moveToFirst();
        // 4. build book object
        Question question = new Question();
        question.setmTextRestID(Integer.parseInt(cursor.getString(0)));
        question.setmTextRest(cursor.getString(1));
        question.setmAnswerTrues(cursor.getString(2));
        //log
        Log.d("getBook(" + id + ")", question.toString());
        // 5. return book
        return question;
    }

    public List<Question> getAllBooks() {
        List<Question> books = new ArrayList<>();
// 1. build the query
        //String query = "SELECT * FROM " + TABLE_QUESTIONS + "limit 10";
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(query, null);
        Cursor cursor = db.rawQuery( "select * from "+TABLE_QUESTIONS+" ORDER BY random() LIMIT 10", null );
// 3. go over each row, build book and add it to list
        Question question = null;
        if (cursor.moveToFirst()) {
            do {
                question = new Question();
                question.setmTextRestID(Integer.parseInt(cursor.getString(0)));
                question.setmTextRest(cursor.getString(1));
                question.setmAnswerTrues(cursor.getString(2));
// Add book to books
                books.add(question);
            } while (cursor.moveToNext());
        }
        Log.d("getAllBooks()", books.toString());
// return books
        return books;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QUESTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete("Questions", null, null);
    }
}
