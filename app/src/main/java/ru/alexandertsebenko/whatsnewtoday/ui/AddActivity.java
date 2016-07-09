package ru.alexandertsebenko.whatsnewtoday.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ru.alexandertsebenko.whatsnewtoday.R;

public class AddActivity extends AppCompatActivity{

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mEditText = (EditText)findViewById(R.id.etAddActivity);

        Intent inIntent = getIntent();
        if (inIntent.getStringExtra("KEY_TEXT") != null) mEditText.setText(inIntent.getStringExtra("KEY_TEXT"));
        else mEditText.setHint("Что нового сегодня?");
        getSupportActionBar().setTitle(inIntent.getStringExtra("KEY_DATE"));
    }
    public void onCancel(View view) {
        Intent outIntent = new Intent();
        setResult(RESULT_CANCELED, outIntent);
        Log.d("AddLog","onCancel");
        finish();
    }
    public void onSave(View view) {
        Intent outIntent = new Intent();
        outIntent.putExtra("KEY_TEXT",mEditText.getText().toString());
        setResult(RESULT_OK,outIntent);
        Log.d("AddLog","onSave " + mEditText.getText());
        finish();
    }
}
