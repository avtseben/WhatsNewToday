package ru.alexandertsebenko.whatsnewtoday.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import ru.alexandertsebenko.whatsnewtoday.R;
import ru.alexandertsebenko.whatsnewtoday.db.WntSqliteHelper;

/**
 * Просмотр записи за один день
 */

public class ShowActivity extends AppCompatActivity{

    private String mTitleDate;
    private String mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Intent inIntent = getIntent();
        int id = inIntent.getIntExtra("_id", 0);

        WntSqliteHelper dbHelper = new WntSqliteHelper(ShowActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        TextView tv = (TextView)findViewById(R.id.tvShowActivity);
        Cursor cursor = db.query("WNT",
                new String[] {"WNT_DATE","WNT_TEXT"},
                "_id = ?",
                new String[] {Integer.toString(id)},
                null,null,null);
        cursor.moveToFirst();
        mTitleDate = cursor.getString(0);
        mText = cursor.getString(1);
        tv.setText(mText);
        getSupportActionBar().setTitle(mTitleDate);
        cursor.close();
        db.close();
    }
}
