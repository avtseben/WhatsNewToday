package ru.alexandertsebenko.whatsnewtoday.ui;
/**
 * WhatsNewToday приложение для фиксации полученного опыта,знаний за день
 */
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.alexandertsebenko.whatsnewtoday.R;
import ru.alexandertsebenko.whatsnewtoday.db.WntSqliteHelper;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_NEW = 300;
    public static final int REQUEST_CODE_EDIT = 301;
    private WntSqliteHelper mDbHelper;
    private SQLiteDatabase mDb;
    private Cursor mCursor;
    private SimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            ListView listView = (ListView)findViewById(R.id.lvMainAll);
            mDbHelper = new WntSqliteHelper(MainActivity.this);
            mDb = mDbHelper.getReadableDatabase();
            mCursor = mDb.query("WNT",
                    new String[] {"_id","WNT_DATE","WNT_TEXT"},
                    null,null,null,null,null);
            mCursorAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2,
                    mCursor,
                    new String[] {"WNT_DATE","WNT_TEXT"},
                    new int[] {android.R.id.text1,android.R.id.text2},
                    0);
            listView.setAdapter(mCursorAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                    intent.putExtra("_id", (int)id);
                    startActivity(intent);
                }});
        } catch (SQLiteException e) {
            Toast.makeText(MainActivity.this,"Database unavailable", Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClicked();
            }
        });
    }
    @Override
    protected void onRestart() {
        Log.d("Main","onRestart");
        super.onRestart();
        try {
            mCursor = mDb.query("WNT",
                    new String[] {"_id","WNT_DATE","WNT_TEXT"},
                    null,null,null,null,null);
            ListView lv = (ListView)findViewById(R.id.lvMainAll);
            CursorAdapter adapter = (CursorAdapter) lv.getAdapter();
            adapter.changeCursor(mCursor);
        } catch (SQLiteException e) {
            Toast.makeText(MainActivity.this,"Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            String today = getTodayInString();
            String text = data.getStringExtra("KEY_TEXT");
            switch (requestCode) {
                case REQUEST_CODE_ADD_NEW:
                    mDbHelper.insertNew(mDb, today, text);
                    break;
                case REQUEST_CODE_EDIT:
                    mDbHelper.updateTextByDate(mDb, today, text);
                    break;
            }
        }
    }
    /**
    *При нажатии на кнопку FAB делаем запрос в базу, если там есть запись за сегодня
    *то берем эту запись и передаём её в Интент.
    *Если записей еще нет то просто вызываем AddActivity без extras
    */
    private void fabClicked(){
        Intent outIntent = new Intent(this, AddActivity.class);
        String today = getTodayInString();
        String text;
        mCursor = mDb.query("WNT",
                new String[] {"WNT_TEXT"},
                "WNT_DATE = ?",
                new String[]{today},
                null,null,null);
        if(mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            text = mCursor.getString(0);
            outIntent.putExtra("KEY_DATE", today);//TODO Дата должна быть в ActionBar заголовке AddActivity
            outIntent.putExtra("KEY_TEXT", text);
            startActivityForResult(outIntent,REQUEST_CODE_EDIT);
        }
        else startActivityForResult(outIntent,REQUEST_CODE_ADD_NEW);
    }

    /**
     * Возвращает сегодняшнюю дату в String например 20.06.16
     * @return
     */
    private String getTodayInString() {
        Date today;
        String output;
        SimpleDateFormat formatter;
        String pattern = "dd.MM.yy";

        formatter = new SimpleDateFormat(pattern);
        today = new Date();
        output = formatter.format(today);
        return output;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDb.close();
        mCursor.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
