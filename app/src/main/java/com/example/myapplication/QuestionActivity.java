package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.io.File;
import classes.DBHelper;
import classes.Question;

public class QuestionActivity extends AppCompatActivity {

    ChipGroup chipGroup;
    Chip ch1, ch2, ch3, ch4, ch5;
    DBHelper database;
    TextView questionTxt, answerTxt;
    Button updateButton, deleteButton, seeButton;
    ImageButton closeButton;
    Question question;
    EditText inputEditTextField;
    ImageView imageData, audioData;
    VideoView videoData;
    PopupWindow popUp;
    RelativeLayout relativeLayout;
    MediaController mediaControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Question Page");
        setContentView(R.layout.activity_question);
        defineVariables();
    }

    private void putQuestion(int qID) {
        question = database.getQuestion(qID);
        questionTxt.setText(question.getQuestionText());
        ch1.setText(question.getAnswer1());
        ch2.setText(question.getAnswer2());
        ch3.setText(question.getAnswer3());
        ch4.setText(question.getAnswer4());
        ch5.setText(question.getAnswer5());
        answerTxt.setText(question.getTrueAnswer(question.getTrueAnswer()));
    }

    private void defineVariables() {
        Intent intent = getIntent();
        chipGroup = (ChipGroup) findViewById(R.id.group1);
        questionTxt = (TextView) findViewById(R.id.questionText);
        ch1 = (Chip) findViewById(R.id.ch1);
        ch2 = (Chip) findViewById(R.id.ch2);
        ch3 = (Chip) findViewById(R.id.ch3);
        ch4 = (Chip) findViewById(R.id.ch4);
        ch5 = (Chip) findViewById(R.id.ch5);
        answerTxt = (TextView) findViewById(R.id.answerText);
        updateButton = (Button) findViewById(R.id.btnUpdateQuestion);
        deleteButton = (Button) findViewById(R.id.btnDeleteQuestion);
        seeButton = (Button) findViewById(R.id.btnSeeAttachment);
        database = new DBHelper(this);
        inputEditTextField = new EditText(this);
        int qID = intent.getIntExtra("question_id", 0);
        putQuestion(qID);
        if(question.getFileName() == null){
            seeButton.setEnabled(false);
        }
    }

    public void onUpdateQuestion(View view) {
        AlertDialog diaBox = AskUpdate();
        diaBox.show();
    }

    public void onDeleteQuestion(View view) {
        AlertDialog diaBox = AskDelete();
        diaBox.show();
    }

    public void onSeeAttachment(View view) {
        File myPath = new File(getBaseContext().getFilesDir(),question.getFileName());
        String extension = question.getFileName().split("\\.")[1];
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        boolean isAudio = false;
        MediaPlayer mp = new MediaPlayer();
        params.gravity = Gravity.CENTER;
        popUp = new PopupWindow(this);
        if(extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg")){
            View customView = inflater.inflate(R.layout.media_image_item,null);
            popUp.setContentView(customView);
            popUp.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            popUp.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
            relativeLayout = (RelativeLayout) customView.findViewById(R.id.rl_custom_layout);
            closeButton = (ImageButton) customView.findViewById(R.id.image_close);
            imageData = (ImageView) customView.findViewById(R.id.image_show);
            imageData.setImageDrawable(Drawable.createFromPath(myPath.toString()));
        }
        // Inflate the custom layout/view
        else if(extension.equals("mp4") || extension.equals("avi")){
            View customView = inflater.inflate(R.layout.media_video_item,null);
            popUp.setContentView(customView);
            popUp.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            popUp.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
            relativeLayout = (RelativeLayout) customView.findViewById(R.id.rl_custom_layout);
            closeButton = (ImageButton) customView.findViewById(R.id.image_close);
            videoData = (VideoView) customView.findViewById(R.id.video_show);
            if (mediaControls == null) {
                // create an object of media controller class
                mediaControls = new MediaController(QuestionActivity.this);
                mediaControls.setAnchorView(videoData);
            }
            // set the media controller for video view
            videoData.setMediaController(mediaControls);
            // set the uri for the video view
            videoData.setVideoPath(myPath.toString());
            // start a video
            videoData.start();

            videoData.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(getApplicationContext(), "Video finished!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
                }
            });
            videoData.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                    return false;
                }
            });
        }
        else{
            isAudio = true;
            View customView = inflater.inflate(R.layout.media_audio_item,null);
            popUp.setContentView(customView);
            popUp.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            popUp.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
            relativeLayout = (RelativeLayout) customView.findViewById(R.id.rl_custom_layout);
            closeButton = (ImageButton) customView.findViewById(R.id.image_close);
            audioData = (ImageView) customView.findViewById(R.id.audio_show);
            audioData.setImageResource(R.drawable.ic_launcher_foreground);
            try {
                mp.setDataSource(myPath.toString());
                mp.prepare();
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean finalIsAudio = isAudio;
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                popUp.dismiss();
                if(finalIsAudio){
                    mp.stop();
                }
            }
        });
        popUp.showAtLocation(relativeLayout, Gravity.CENTER,0,0);
    }

    private AlertDialog AskDelete() {
        return new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you really want to Delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                            File file;
                            if (database.deleteQuestion(question.getqID())) {
                                if (question.getFileName() != null &&
                                        (file = new File(getFilesDir(), question.getFileName())).delete()){
                                }
                                Toast.makeText(getBaseContext(), "Question deleted successfully!", Toast.LENGTH_LONG).show();
                            }
                        else System.out.println("Unsuccessful");
                        dialog.dismiss();
                        finish();
                        Intent intent = new Intent(getBaseContext(), ListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    private AlertDialog AskUpdate() {
        return new AlertDialog.Builder(this)
                .setTitle("Update")
                .setMessage("Please enter your  new question text")
                .setView(inputEditTextField)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = inputEditTextField.getText().toString();
                        //Log.d("onclick","editext value is: "+ editTextInput);
                        questionTxt.setText(editTextInput);
                        question.setQuestionText(editTextInput);
                        database.updateQuestion(question);
                        finish();
                        Intent intent = new Intent(getBaseContext(), ListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}