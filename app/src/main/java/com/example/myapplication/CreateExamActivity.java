package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import classes.DBHelper;
import classes.Question;
import classes.QuestionAdapterForExam;

public class CreateExamActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    ArrayList<Question> arrayList;
    RecyclerView r_view;
    DBHelper database;
    Button saveExam, changePreferences;
    Hashtable<Integer, ArrayList<Integer>> selected_questions;
    SharedPreferences sharedPreferences;
    String username, dateText, timeText;
    TextView durationText, questionScore, difficultyLevel;
    EditText date, time;
    DatePickerDialog picker;
    TimePickerDialog tpd;
    int added_question_id;
    public ArrayList<Integer> selected_answers;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Duration = "durationKey" ;
    public static final String Score = "scoreKey";
    public static final String Level = "levelKey";
    private static final int WRITE_REQUEST_CODE = 101;
    private static final int SELECT_ANSWERS_CODE = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam);
        defineVariables();
        defineListeners();
        setTitle("Create Exam");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("selected_qID"));
    }

    public void defineVariables() {
        database = new DBHelper(this);
        arrayList = database.getAllQuestions();
        context = this;
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        r_view = findViewById(R.id.r_view_create);
        time = findViewById(R.id.examTime);
        date = findViewById(R.id.examDate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        r_view.setLayoutManager(linearLayoutManager);
        QuestionAdapterForExam questionAdapter = new QuestionAdapterForExam(arrayList,context);
        r_view.setAdapter(questionAdapter);
        changePreferences = (Button) findViewById(R.id.btnChangePreferences);
        saveExam = (Button) findViewById(R.id.btnCreateSaveExam);
        selected_questions = new Hashtable<>();
        durationText = (TextView) findViewById(R.id.txtExamDuration);
        questionScore = (TextView) findViewById(R.id.txtQuestionScore);
        difficultyLevel = (TextView) findViewById(R.id.txtDifficultyLevel);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        durationText.setText(getResources().getString(R.string.duration_default,
                String.valueOf(sharedPreferences.getInt(Duration, 0))));
        questionScore.setText(getResources().getString(R.string.score_default,
                String.valueOf(sharedPreferences.getInt(Score, 0))));
        difficultyLevel.setText(getResources().getString(R.string.level_default,
                String.valueOf(sharedPreferences.getInt(Level, 0))));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnChangePreferences:
                intent = new Intent(this, ExamSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnCreateSaveExam:
                createWriteFile();
                break;
            case R.id.examTime:
                getTimePicker();
                break;
            case R.id.examDate:
                getDatePicker();
                break;
            default:
                break;
        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int qID = intent.getIntExtra("question_id", 0);
            if(!selected_questions.containsKey(qID)){
                Toast.makeText(CreateExamActivity.this,qID + " added!",Toast.LENGTH_SHORT).show();
                Intent selectIntent = new Intent(getBaseContext(), SelectQuestionActivity.class);
                selectIntent.putExtra("question_id", qID);
                selectIntent.putExtra("level", sharedPreferences.getInt(Level, 0));
                startActivityForResult(selectIntent, SELECT_ANSWERS_CODE);
            }
            else {
                selected_questions.remove(qID);
                Toast.makeText(CreateExamActivity.this,qID + " removed!" ,Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void createWriteFile() {
        if(selected_questions.size() == 0){
            System.out.println("Empty");
        }
        else{
            dateText = date.getText().toString();
            timeText = time.getText().toString();
            createFile();
        }
    }

    // create text file
    private void createFile() {
        // when you create document, you need to add Intent.ACTION_CREATE_DOCUMENT
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "Exam.txt");
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null
                            && data.getData() != null) {
                        writeInFile(data.getData());
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
        if (requestCode == SELECT_ANSWERS_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    added_question_id = data.getIntExtra("added_question_id", 0);
                    selected_answers = data.getIntegerArrayListExtra("selected_array");
                    //answers.add(selected_answers);
                    selected_questions.put(added_question_id, selected_answers);
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

    private void writeInFile(@NonNull Uri uri) {
        OutputStream outputStream;
        try {
            outputStream = getContentResolver().openOutputStream(uri);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            //bw.write(text);
            bw.write("Duration:" + String.valueOf(sharedPreferences.getInt(Duration, 0)));
            bw.write("\nScore:" + String.valueOf(sharedPreferences.getInt(Score, 0)));
            bw.write("\nLevel:" + String.valueOf(sharedPreferences.getInt(Level, 0)));
            bw.write("\nExam Date:" + dateText);
            bw.write("\nExam Time:" + timeText);
            List<Integer> keys = new ArrayList<>(selected_questions.keySet());
            for (int i = 0; i<selected_questions.size(); i++){
                Question question = database.getQuestion(keys.get(i));
                bw.write("\nQuestion:" + question.getQuestionText());
                ArrayList<Integer> answerList = selected_questions.get(question.getqID());
                for (int j = 0; j<answerList.size(); j++){
                    bw.write("\nAnswer: " + question.getAnswer(answerList.get(j)));
                }
                bw.write("\nTrue Answer: " + question.getTrueAnswer(question.getTrueAnswer()));
            }
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void defineListeners() {
        saveExam.setOnClickListener(this);
        changePreferences.setOnClickListener(this);
        time.setOnClickListener(this);
        date.setOnClickListener(this);
    }

    private void getTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        tpd = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // hourOfDay ve minute değerleri seçilen saat değerleridir.
                        // Edittextte bu değerleri gösteriyoruz.
                        time.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
        tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Select", tpd);
        tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Cancel", tpd);
        tpd.show();
    }

    private void getDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(CreateExamActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
        picker.show();
    }
}