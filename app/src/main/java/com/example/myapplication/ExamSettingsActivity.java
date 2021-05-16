package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExamSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Integer[] levels = {2, 3, 4, 5};
    Spinner difficultySpinner;
    EditText duration, score;
    int selectedLevel;
    Button save;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Duration = "durationKey" ;
    public static final String Score = "scoreKey";
    public static final String Level = "levelKey";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_settings);
        defineVariables();
        setTitle("Exam Settings");
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(ExamSettingsActivity.this, android.R.layout.simple_spinner_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);
    }

    public void defineVariables() {
        difficultySpinner = (Spinner) findViewById(R.id.difficultyLevel);
        difficultySpinner.setOnItemSelectedListener(this);
        duration = (EditText) findViewById(R.id.examDuration);
        score = (EditText) findViewById(R.id.questionScore);
        save = (Button) findViewById(R.id.btnSavePreferences);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedLevel = levels[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(),"Please Select a Difficulty Level!" , Toast.LENGTH_LONG).show();
    }

    public void onSavePreferences(View view) {
        int examDuration = Integer.parseInt(duration.getText().toString());
        int questionScore = Integer.parseInt(score.getText().toString());
        int level = selectedLevel;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(Duration, examDuration);
        editor.putInt(Score, questionScore);
        editor.putInt(Level, level);
        editor.apply();
        Toast.makeText(ExamSettingsActivity.this,"Thanks!",Toast.LENGTH_LONG).show();
        finish();
    }
}