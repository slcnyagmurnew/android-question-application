package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import classes.DBHelper;
import classes.Question;

public class AddQuestionActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 200;
    EditText questionTxt, answer1, answer2, answer3, answer4, answer5, trueAnswer;
    TextView fileName;
    Question newQuestion;
    byte[] byteArray;
    DBHelper database;
    Intent requestFileIntent;
    String file_path=null, file_name=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        defineVariables();
        setTitle("Add Question");
    }

    private void defineVariables() {
        questionTxt = (EditText) findViewById(R.id.newQuestionText);
        answer1 = (EditText) findViewById(R.id.newAnswer1);
        answer2 = (EditText) findViewById(R.id.newAnswer2);
        answer3 = (EditText) findViewById(R.id.newAnswer3);
        answer4 = (EditText) findViewById(R.id.newAnswer4);
        answer5 = (EditText) findViewById(R.id.newAnswer5);
        fileName = (TextView) findViewById(R.id.fileNameTxt);
        trueAnswer = (EditText) findViewById(R.id.newTrueAnswer);
        database = new DBHelper(this);
    }

    private void callSuccessful() {
        Context context = getApplicationContext();
        CharSequence text = "Question added successfully!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private boolean isValidQuestion() {
        return !(questionTxt.getText() == null || answer1.getText() == null
        || answer2.getText() == null || answer3.getText() == null
        || answer4.getText() == null || trueAnswer.getText() == null
        || Integer.parseInt(trueAnswer.getText().toString()) > 5
        || trueAnswer.getText().toString().equals("")
        || Integer.parseInt(trueAnswer.getText().toString()) < 1
        || answer5.getText() == null);
    }

    public void onSaveNewQuestion(View view) {
        if(!isValidQuestion()){
            Toast.makeText(AddQuestionActivity.this,"Please fill in Question information correctly!" ,Toast.LENGTH_SHORT).show();
            return;
        }
        String question = questionTxt.getText().toString();
        String answer_1 = answer1.getText().toString();
        String answer_2 = answer2.getText().toString();
        String answer_3 = answer3.getText().toString();
        String answer_4 = answer4.getText().toString();
        String answer_5 = answer5.getText().toString();
        int true_answer = Integer.parseInt(trueAnswer.getText().toString());
        newQuestion =  new Question(database.getLastQID()+1, question, answer_1, answer_2,
                answer_3, answer_4, answer_5, true_answer, byteArray, file_path, file_name);
        if(database.insertQuestion(newQuestion)){
            callSuccessful();
            finish();
        }
        else System.out.println("Error occurred");
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(AddQuestionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(AddQuestionActivity.this, "Please Give Permission to Upload File", Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityCompat.requestPermissions(AddQuestionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AddQuestionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void filePicker() {
        Toast.makeText(AddQuestionActivity.this, "File Picker calls", Toast.LENGTH_SHORT).show();
        requestFileIntent = new Intent();
        requestFileIntent.setType("*/*");
        requestFileIntent.setAction(Intent.ACTION_GET_CONTENT);
        requestFileIntent = Intent.createChooser(requestFileIntent, "Choose a file");
        startActivityForResult(requestFileIntent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try {
                assert data != null;
                String filePath = getRealPathFromUri(data.getData(), AddQuestionActivity.this);
                Log.d("File Path : "," "+filePath);
                //now we will upload the file
                this.file_path=filePath;
                File file=new File(filePath);
                fileName.setText(file.getName());
                this.file_name = file.getName();
                //File savedFile = new File(this.getFilesDir(), file.getName());
                byteArray = Files.readAllBytes(Paths.get(filePath));
                try (FileOutputStream fos = this.openFileOutput(file.getName(), Context.MODE_PRIVATE)) {
                    fos.write(byteArray);
                }
                // System.out.println(Arrays.toString(byteArray));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromUri(Uri uri, Activity activity) {
        String[] proj = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if(cursor == null) {
            return uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int id = cursor.getColumnIndex( MediaStore.Files.FileColumns.DATA );
            return cursor.getString(id);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(AddQuestionActivity.this, "Permission Successfull", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddQuestionActivity.this, "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void onSelectFile(View view) {
        if(checkPermission()){
            filePicker();
        }
        else{
            requestPermission();
        }
    }
}