package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import classes.QuestionAdapter;
import classes.DBHelper;
import classes.Question;

public class ListActivity extends AppCompatActivity {

    Context context;
    ArrayList<Question> arrayList;
    RecyclerView r_view;
    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        database = new DBHelper(this);
        arrayList = database.getAllQuestions();
        context = this;
        setTitle("Questions");
        r_view = findViewById(R.id.r_view_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        r_view.setLayoutManager(linearLayoutManager);
        QuestionAdapter questionAdapter = new QuestionAdapter(arrayList,context);
        r_view.setAdapter(questionAdapter);
    }
}