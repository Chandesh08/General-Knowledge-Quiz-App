package com.example.dashbard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    ImageView startQuizImg;
    ImageView SettingQuizImg;
    ImageView HighQuizImg;
    ImageView HelpImg;
    String API_URL = "http://192.168.43.149:8080/questionapi/index.php?data=all";

    private int highscore;
    MySQLiteHelper db = new MySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(MainActivity.this, BackgroundSoundService.class);
        startService(intent);
        new ConnectionDb().execute();

        startQuizImg = findViewById(R.id.startQuizImg);
        startQuizImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivityForResult(new Intent(MainActivity.this, PlayGameActivity.class), REQUEST_CODE_QUIZ);
                return false;

            }
        });

        SettingQuizImg = findViewById(R.id.settingsImg);
        SettingQuizImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                return false;
            }
        });

        HighQuizImg = findViewById(R.id.highScporesImg);
        HighQuizImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(MainActivity.this, HighScoreActivity.class));
                return false;
            }
        });

        HelpImg = findViewById(R.id.helpImg);
        HelpImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(MainActivity.this, Postplay.class));
                return false;
            }
        });

    }

    class ConnectionDb extends AsyncTask<Void, Void, String> {
        private Exception exception;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();


                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                db.removeAll();
            }

            String JSON_STRING = response.toString();
            try {
                // get JSONObject from JSON file
                JSONObject obj = new JSONObject(JSON_STRING);
                JSONArray userArray = obj.getJSONArray("data");
                // fetch JSONObject named employee


                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject data = userArray.getJSONObject(i);

                    db.addBook(new Question(0, data.getString("quest"), data.getString("ans")));
                }
                int a = db.getProfilesCount();
                Log.d("Count", String.valueOf(a));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, BackgroundSoundService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(PlayGameActivity.EXTRA_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        //textViewHighscore.setText("Highscore: " + highscore);
    }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        //textViewHighscore.setText("Highscore: " + highscore);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }

}