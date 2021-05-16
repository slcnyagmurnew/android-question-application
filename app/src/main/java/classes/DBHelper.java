package classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Persons.db";
    private final SQLiteDatabase database = this.getWritableDatabase();
    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table persons(email TEXT primary key, name TEXT, surname TEXT, phone TEXT unique, birthDate TEXT, " +
                "password TEXT, photoID INT)");
        db.execSQL("create Table questions(qID INT primary key, question TEXT, answer1 TEXT, answer2 TEXT, answer3 TEXT, " +
                "answer4 TEXT, answer5 TEXT, trueAnswer INT, file BLOB, filePath TEXT, fileName TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists persons");
    }

    public boolean insertPerson(String name, String surname, String email, String phone,
                                Date birthDate, String password, int photoID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("phone", phone);
        contentValues.put("birthDate", String.valueOf(birthDate));
        contentValues.put("password", bytesToHex(password));
        contentValues.put("photoID",photoID);
        long result = database.insert("persons",null, contentValues);
        return !(result == -1);
    }

    public String bytesToHex(String originalString) {
        byte[]encodedHash = new byte[0];
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encodedHash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            e.printStackTrace();
        }

        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean checkEmail(String email) {
        Cursor cursor = database.rawQuery("select * from persons where email = ?", new String[] {email});
        return (cursor.getCount() > 0);
    }

    public Person checkLogin(String email, String password) {
        password = bytesToHex(password);
        try {
            Cursor cursor = database.rawQuery("select * from persons where email = ? and password = ?", new String[] {email, password});
            if (cursor.moveToFirst()) {
                int emailIndex = cursor.getColumnIndexOrThrow("email");
                int nameIndex = cursor.getColumnIndexOrThrow("name");
                int passwordIndex = cursor.getColumnIndexOrThrow("password");
                int photoIDIndex = cursor.getColumnIndexOrThrow("photoID");
                String email_ = cursor.getString(emailIndex);
                String name_ = cursor.getString(nameIndex);
                String password_ = cursor.getString(passwordIndex);
                int photoID_ = cursor.getInt(photoIDIndex);
                cursor.close();
                return new Person(name_, email_, password_, photoID_);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Question getQuestion(int qId) {
        try {
            Cursor cursor = database.rawQuery("select * from questions where qID = ?", new String[]{String.valueOf(qId)});
            if (cursor.moveToFirst()) {
                int qIDIndex = cursor.getColumnIndexOrThrow("qID");
                int questionIndex = cursor.getColumnIndexOrThrow("question");
                int answer1Index = cursor.getColumnIndexOrThrow("answer1");
                int answer2Index = cursor.getColumnIndexOrThrow("answer2");
                int answer3Index = cursor.getColumnIndexOrThrow("answer3");
                int answer4Index = cursor.getColumnIndexOrThrow("answer4");
                int answer5Index = cursor.getColumnIndexOrThrow("answer5");
                int trueAnswerIndex = cursor.getColumnIndexOrThrow("trueAnswer");
                int fileIndex = cursor.getColumnIndexOrThrow("file");
                int filePathIndex = cursor.getColumnIndexOrThrow("filePath");
                int fileNameIndex = cursor.getColumnIndexOrThrow("fileName");
                int qID = cursor.getInt(qIDIndex);
                String question = cursor.getString(questionIndex);
                String answer1 = cursor.getString(answer1Index);
                String answer2 = cursor.getString(answer2Index);
                String answer3 = cursor.getString(answer3Index);
                String answer4 = cursor.getString(answer4Index);
                String answer5 = cursor.getString(answer5Index);
                int trueAnswer = cursor.getInt(trueAnswerIndex);
                byte[] fileArray = cursor.getBlob(fileIndex);
                String filePath = cursor.getString(filePathIndex);
                String fileName = cursor.getString(fileNameIndex);
                cursor.close();
                return new Question(qID, question, answer1, answer2, answer3, answer4, answer5, trueAnswer,
                        fileArray, filePath, fileName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from questions", null);
        try {
            while (cursor.moveToNext()) {
                int qIDIndex = cursor.getColumnIndexOrThrow("qID");
                int questionIndex = cursor.getColumnIndexOrThrow("question");
                int answer1Index = cursor.getColumnIndexOrThrow("answer1");
                int answer2Index = cursor.getColumnIndexOrThrow("answer2");
                int answer3Index = cursor.getColumnIndexOrThrow("answer3");
                int answer4Index = cursor.getColumnIndexOrThrow("answer4");
                int answer5Index = cursor.getColumnIndexOrThrow("answer5");
                int trueAnswerIndex = cursor.getColumnIndexOrThrow("trueAnswer");
                int fileIndex = cursor.getColumnIndexOrThrow("file");
                int filePathIndex = cursor.getColumnIndexOrThrow("filePath");
                int fileNameIndex = cursor.getColumnIndexOrThrow("fileName");
                int qID = cursor.getInt(qIDIndex);
                String question = cursor.getString(questionIndex);
                String answer1 = cursor.getString(answer1Index);
                String answer2 = cursor.getString(answer2Index);
                String answer3 = cursor.getString(answer3Index);
                String answer4 = cursor.getString(answer4Index);
                String answer5 = cursor.getString(answer5Index);
                int trueAnswer = cursor.getInt(trueAnswerIndex);
                byte[] fileArray = cursor.getBlob(fileIndex);
                String filePath = cursor.getString(filePathIndex);
                String fileName = cursor.getString(fileNameIndex);
                questions.add(new Question(qID, question, answer1, answer2, answer3, answer4, answer5,
                        trueAnswer, fileArray, filePath, fileName));
            }
        } finally {
            cursor.close();
        }
        return questions;
    }

    public int getLastQID() {
        try {
            Cursor cursor = database.rawQuery("select * from questions order by qID desc limit 1", null);
            if (cursor.moveToFirst()) {
                int qIDIndex = cursor.getColumnIndexOrThrow("qID");
                int qID = cursor.getInt(qIDIndex);
                cursor.close();
                return qID;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean insertQuestion(Question question) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("qID",question.getqID());
        contentValues.put("question", question.getQuestionText());
        contentValues.put("answer1", question.getAnswer1());
        contentValues.put("answer2", question.getAnswer2());
        contentValues.put("answer3", question.getAnswer3());
        contentValues.put("answer4", question.getAnswer4());
        contentValues.put("answer5", question.getAnswer5());
        contentValues.put("trueAnswer",question.getTrueAnswer());
        contentValues.put("file", question.getFile());
        contentValues.put("filePath", question.getFilePath());
        contentValues.put("fileName", question.getFileName());
        long result = database.insert("questions",null, contentValues);
        return !(result == -1);
    }

    public boolean deleteQuestion(int qID) {
        try {
            database.execSQL("delete from questions where qID = ?", new String[]{String.valueOf(qID)});
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateQuestion(Question question) {
        try {
            database.execSQL("update questions set question = ? where  qID = ?",
                    new String[]{question.getQuestionText(), String.valueOf(question.getqID())});
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
