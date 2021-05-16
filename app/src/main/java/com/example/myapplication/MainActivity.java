package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import classes.DBHelper;
import classes.Person;

public class MainActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    String email_text, password_text;
    int attempt;
    Button loginBtn;
    DBHelper database;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailText = (EditText) findViewById(R.id.editTextEmailAddress);
        passwordText = (EditText) findViewById(R.id.editTextPassword);
        loginBtn = (Button) findViewById(R.id.btnLogin);
        database = new DBHelper(this);
        attempt = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginBtn.setEnabled(true);
        attempt = 0;
    }

    public Person personControl(Person p) {
        return database.checkLogin(p.getEmail(), p.getPassword());
    }

    private void callFalseNotice() {
        Context context = getApplicationContext();
        CharSequence text = "Email or password was incorrect!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void callEmptyNotice() {
        Context context = getApplicationContext();
        CharSequence text = "Please enter your email and password!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void onLogin(View view) {
        // define variables isimli fonksiyonda tüm işlevler yerine getirilebilir
        // daha sonra o fonksiyon onCreate'te çağrılır
        Intent intent = new Intent(this, PersonActivity.class);
        email_text = emailText.getText().toString();
        password_text = passwordText.getText().toString();
        if(email_text.equals("") || password_text.equals("")) {
            callEmptyNotice();
        }
        else {
            Person newPerson = new Person(email_text, password_text);
            Person truePerson = personControl(newPerson);
            if(truePerson != null) {
                intent.putExtra("username", truePerson.getEmail());
                intent.putExtra("name_text", truePerson.getName());
                intent.putExtra("person_image", truePerson.getPhotoID());
                startActivity(intent);
            }
            else{
                if(attempt > 2){
                    loginBtn.setEnabled(false);
                }
                else{
                    callFalseNotice();
                }
            }
            attempt++;
        }
        emailText.setText("");
        passwordText.setText("");
    }

    public void onSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}