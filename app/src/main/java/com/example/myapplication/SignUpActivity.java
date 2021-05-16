package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import classes.DBHelper;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    EditText newPersonName, newPersonSurname, newPersonEmail, newPersonPhone, newPersonPassword,
    newPersonBirthDate, newPersonRePassword;
    String name, surname, email, phoneNumber, birthDate, password, rePassword;
    DBHelper database;
    int imgID;
    ImageButton wheat, vegan, honey, organic, nut, walnut;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Registration");
        setContentView(R.layout.activity_sign_up);
        defineVariables();
        defineListeners();
    }

    private void callSuccessNotice() {
        Context context = getApplicationContext();
        CharSequence text = "Successfully registered!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void callExistsNotice() {
        Context context = getApplicationContext();
        CharSequence text = "This email already exists!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void callWrongInput() {
        Context context = getApplicationContext();
        CharSequence text = "Please fill in all fields correctly!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void defineVariables() {
        newPersonName = (EditText) findViewById(R.id.newName);
        newPersonSurname = (EditText) findViewById(R.id.newSurname);
        newPersonEmail = (EditText) findViewById(R.id.newEmail);
        newPersonPhone = (EditText) findViewById(R.id.newPhone);
        newPersonBirthDate = (EditText) findViewById(R.id.newBirthDate);
        newPersonPassword = (EditText) findViewById(R.id.newPassword);
        newPersonRePassword = (EditText) findViewById(R.id.newRePassword);
        honey = (ImageButton) findViewById(R.id.btnImgHoney);
        walnut = (ImageButton) findViewById(R.id.btnImgWalnut);
        nut = (ImageButton) findViewById(R.id.btnImgNut);
        vegan = (ImageButton) findViewById(R.id.btnImgVegan);
        organic = (ImageButton) findViewById(R.id.btnImgOrganic);
        wheat = (ImageButton) findViewById(R.id.btnImgWheat);
        database = new DBHelper(this);
    }

    private void clearEditTexts() {
        newPersonName.setText("");
        newPersonSurname.setText("");
        newPersonEmail.setText("");
        newPersonPhone.setText("");
        newPersonBirthDate.setText("");
        newPersonPassword.setText("");
        newPersonRePassword.setText("");
    }

    private static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private static boolean validatePassword(String password, String rePassword) {
        return password.equals(rePassword);
    }

    public void onGetDatePicker(View view) {
        DatePickerDialog picker;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(SignUpActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newPersonBirthDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
        picker.show();
    }

    public void onSignUpSubmit(View view) {
        try {
            name = newPersonName.getText().toString();
            surname = newPersonSurname.getText().toString();
            email = newPersonEmail.getText().toString();
            phoneNumber = newPersonPhone.getText().toString();
            birthDate = newPersonBirthDate.getText().toString();
            Date dateObject = Date.valueOf(birthDate);
            password = newPersonPassword.getText().toString();
            rePassword = newPersonRePassword.getText().toString();
            if(!validateEmail(email) || !validatePassword(password, rePassword)){
                callWrongInput();
                return;
            }
            if(database.checkEmail(email)) {
                callExistsNotice();
                clearEditTexts();
                return;
            }
            boolean check = database.insertPerson(name, surname, email, phoneNumber,
                    dateObject, password, imgID);
            if(check){
                callSuccessNotice();
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else{
                System.out.println("failed");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnImgHoney:
                if(imgID == R.drawable.honey) {
                    honey.setColorFilter(null);
                    this.imgID = -1;
                }
                else{
                    honey.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY);
                    this.imgID = R.drawable.honey;
                }
                break;
            case R.id.btnImgNut:
                if(imgID == R.drawable.nut) {
                    nut.setColorFilter(null);
                    this.imgID = -1;
                }
                else{
                    nut.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY);
                    this.imgID = R.drawable.nut;
                }
                break;
            case R.id.btnImgOrganic:
                if(imgID == R.drawable.organic) {
                    organic.setColorFilter(null);
                    this.imgID = -1;
                }
                else{
                    organic.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY);
                    this.imgID = R.drawable.organic;
                }
                break;
            case R.id.btnImgVegan:
                if(imgID == R.drawable.vegan) {
                    vegan.setColorFilter(null);
                    this.imgID = -1;
                }
                else{
                    vegan.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY);
                    this.imgID = R.drawable.vegan;
                }
                break;
            case R.id.btnImgWalnut:
                if(imgID == R.drawable.walnut) {
                    walnut.setColorFilter(null);
                    this.imgID = -1;
                }
                else{
                    walnut.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY);
                    this.imgID = R.drawable.walnut;
                }
                break;
            case R.id.btnImgWheat:
                if(imgID == R.drawable.wheat) {
                    wheat.setColorFilter(null);
                    this.imgID = -1;
                }
                else{
                    wheat.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY);
                    this.imgID = R.drawable.wheat;
                }
                break;
            default:
                break;
        }
    }

    public void defineListeners() {
        honey.setOnClickListener(this);
        wheat.setOnClickListener(this);
        walnut.setOnClickListener(this);
        organic.setOnClickListener(this);
        vegan.setOnClickListener(this);
        nut.setOnClickListener(this);
    }
}