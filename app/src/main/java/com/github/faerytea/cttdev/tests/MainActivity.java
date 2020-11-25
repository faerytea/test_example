package com.github.faerytea.cttdev.tests;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSortClick(@NonNull View button) {
        final SortActivity.Type tp;
        switch (button.getId()) {
            case R.id.int_sort: tp = SortActivity.Type.INT; break;
            case R.id.char_sort: tp = SortActivity.Type.CHAR; break;
            case R.id.word_sort: tp = SortActivity.Type.WORD; break;
            default: return;
        }
        startActivity(SortActivity.createStartIntent(this, tp));
    }
}