package com.github.faerytea.cttdev.tests;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.faerytea.cttdev.tests.collection.IntArrayList;
import com.github.faerytea.cttdev.tests.collection.IntList;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class SortActivity extends AppCompatActivity {
    @VisibleForTesting(otherwise = Modifier.PRIVATE)
    static final String KEY_TYPE = "type";
    public static final String KEY_RESULT = "result";
    private EditText input;
    private TextView result;
    private Function<String, String> sorter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        result = findViewById(R.id.result);
        if (savedInstanceState != null) {
            result.setText(savedInstanceState.getCharSequence(KEY_RESULT, ""));
        }
        input = findViewById(R.id.data);
        switch (Type.values()[getIntent().getIntExtra(KEY_TYPE, -1)]) {
            case INT:
                sorter = data -> {
                    final String[] numbers = data.split("\\W+");
                    final IntList ints = new IntArrayList(numbers.length);
                    for (String n : numbers) {
                        try {
                            ints.add(Integer.parseInt(n));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(this, getString(R.string.error, n), Toast.LENGTH_LONG).show();
                            return "";
                        }
                    }
                    ints.sort();
                    return ints.toString();
                };
                break;
            case CHAR:
                sorter = data -> {
                    final char[] chars = data.toCharArray();
                    Arrays.sort(chars);
                    return new String(chars);
                };
                break;
            case WORD:
                sorter = data -> {
                    final String[] words = data.split("\\W+");
                    Arrays.sort(words);
                    return TextUtils.join(" ", words);
                };
                break;
        }
    }

    public void onSortClick(@NonNull View ignored) {
        result.setText(sorter.apply(input.getText().toString()));
    }

    public void onResultClick(View view) {
        ContextCompat.getSystemService(this, ClipboardManager.class)
                .setPrimaryClip(new ClipData("sorter", new String[]{"text/plain"}, new ClipData.Item(result.getText())));
    }

    public static Intent createStartIntent(@NonNull Context ctx, @NonNull Type type) {
        final Intent res = new Intent(ctx, SortActivity.class);
        res.putExtra(KEY_TYPE, type.ordinal());
        return res;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_RESULT, result.getText());
    }

    public enum Type {
        INT, CHAR, WORD
    }
}