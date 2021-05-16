package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    String name_text, username;
    TextView textView;
    int imgID;
    ImageView imageView;
    ImageButton list, add, create, set, send, exit;
    Intent sendIntent;
    private static final int REQUEST_GALLERY = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        defineVariables();
        setTitle("Welcome " + name_text);
        defineListeners();
    }

    private void defineVariables() {
        list = (ImageButton) findViewById(R.id.btnListQuestions);
        add = (ImageButton) findViewById(R.id.btnAddQuestion);
        create = (ImageButton) findViewById(R.id.btnCreateExam);
        set = (ImageButton) findViewById(R.id.btnExamSettings);
        send = (ImageButton) findViewById(R.id.btnSendExam);
        exit = (ImageButton) findViewById(R.id.btnExitApp);
        Intent intent = getIntent();
        textView = (TextView) findViewById(R.id.idText);
        imageView = (ImageView) findViewById(R.id.personImg);
        name_text = intent.getStringExtra("name_text");
        username = intent.getStringExtra("username");
        imgID = intent.getIntExtra("person_image", 0);
        textView.setText(name_text);
        imageView.setImageResource(imgID);
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnListQuestions:
                intent = new Intent(this, ListActivity.class);
                intent.putExtra("name_text", name_text);
                intent.putExtra("person_image", imgID);
                startActivity(intent);
                break;
            case R.id.btnAddQuestion:
                intent = new Intent(this, AddQuestionActivity.class);
                intent.putExtra("name_text", name_text);
                startActivity(intent);
                break;
            case R.id.btnExamSettings:
                intent = new Intent(this, ExamSettingsActivity.class);
                intent.putExtra("name_text", name_text);
                startActivity(intent);
                break;
            case R.id.btnCreateExam:
                intent = new Intent(this, CreateExamActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            case R.id.btnSendExam:
                filePicker();
                break;
            case R.id.btnExitApp:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            default:
                break;
        }
    }

    public void defineListeners() {
        list.setOnClickListener(this);
        add.setOnClickListener(this);
        set.setOnClickListener(this);
        create.setOnClickListener(this);
        send.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void filePicker() {
        Toast.makeText(PersonActivity.this, "File Picker calls", Toast.LENGTH_SHORT).show();
        Intent chooseFile;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("text/plain");
        sendIntent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(sendIntent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try {
                assert data != null;
                Uri uri = data.getData();
                Intent sendFileIntent = new Intent();
                sendFileIntent.setAction(Intent.ACTION_SEND);
                sendFileIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sendFileIntent.setType("text/plain");
                sendFileIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Intent shareIntent = Intent.createChooser(sendFileIntent, null);
                startActivity(shareIntent);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}