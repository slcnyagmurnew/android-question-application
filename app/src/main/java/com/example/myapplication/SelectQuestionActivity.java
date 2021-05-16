package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

import classes.DBHelper;
import classes.Question;

public class SelectQuestionActivity extends AppCompatActivity {

    ChipGroup chipGroup;
    Chip ch1, ch2, ch3, ch4, ch5;
    DBHelper database;
    TextView questionTxt, answerTxt;
    Button selectButton;
    int qID, level;
    Question question;
    public ArrayList<Integer> selectedAnswers = new ArrayList<>();
    public ArrayList<Chip> chips = new ArrayList<>();
    public ArrayList<String> answers = new ArrayList<>();
    public ArrayList<Boolean> booleanArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_question);
        defineVariables();
    }

    public void defineVariables(){
        Intent intent = getIntent();
        qID = intent.getIntExtra("question_id", 0);
        level = intent.getIntExtra("level", 0);
        chipGroup = (ChipGroup) findViewById(R.id.group2);
        ch1 = (Chip) findViewById(R.id.sch1);
        ch2 = (Chip) findViewById(R.id.sch2);
        ch3 = (Chip) findViewById(R.id.sch3);
        ch4 = (Chip) findViewById(R.id.sch4);
        ch5 = (Chip) findViewById(R.id.sch5);
        chips.add(ch1); chips.add(ch2); chips.add(ch3); chips.add(ch4); chips.add(ch5);
        database = new DBHelper(this);
        questionTxt = (TextView) findViewById(R.id.selectQuestionText);
        answerTxt = (TextView) findViewById(R.id.selectAnswerText);
        selectButton = (Button) findViewById(R.id.btnSelectQuestion);
        question = database.getQuestion(qID);
        questionTxt.setText(question.getQuestionText());
        answers.add(question.getAnswer1());
        answers.add(question.getAnswer2());
        answers.add(question.getAnswer3());
        answers.add(question.getAnswer4());
        answers.add(question.getAnswer5());
        answerTxt.setText(question.getTrueAnswer(question.getTrueAnswer()));
        Chip chip;
        for (int i = 0; i < 5; i++) {
            chip = chips.get(i);
            chip.setTag(i);
            booleanArrayList.add(false);
            chip.setText(answers.get(i));
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener(changeListener);
        }
    }

    public void onSelectQuestion(View view){
        int trueCount = 0;
        for (int i = 0; i < 5; i++) {
            if (booleanArrayList.get(i) /* or array[i] */) {
                trueCount++;
                selectedAnswers.add(i);
            }
        }
        if(trueCount != level || !booleanArrayList.get(question.getTrueAnswer()-1)){
            Toast.makeText(SelectQuestionActivity.this,"Please select " + level + " answers include true answer!",
                    Toast.LENGTH_SHORT).show();
            selectedAnswers = new ArrayList<>();
        }
        else{
            Intent intent = new Intent(this, CreateExamActivity.class);
            intent.putExtra("added_question_id", question.getqID());
            intent.putExtra("selected_array", selectedAnswers);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private final CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            int tag = (int) compoundButton.getTag();
            booleanArrayList.set(tag, isChecked);
        }
    };
}