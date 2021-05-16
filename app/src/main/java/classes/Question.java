package classes;

public class Question {

    private final int qID, trueAnswer;
    private final String answer1, answer2, answer3, answer4, answer5, filePath, fileName;
    private String questionText;
    private final byte[] file;

    public byte[] getFile() {
        return file;
    }

    public String getFileName() { return fileName; }

    public String getFilePath() { return filePath; }

    public int getqID() {
        return qID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public String getAnswer5() { return answer5; }

    public int getTrueAnswer() {
        return trueAnswer;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Question(int qID, String questionText, String answer1, String answer2, String answer3,
                    String answer4, String answer5, int trueAnswer, byte[] file, String filePath, String fileName) {
        this.qID = qID;
        this.questionText = questionText;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.trueAnswer = trueAnswer;
        this.file = file;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getTrueAnswer(int trueAnswer) {
        if (trueAnswer == 1){
            return this.answer1;
        }
        else if (trueAnswer == 2){
            return this.answer2;
        }
        else if (trueAnswer == 3){
            return this.answer3;
        }
        else if (trueAnswer == 4){
            return this.answer4;
        }
        else{
            return this.answer5;
        }
    }

    public String getAnswer(int index) {
        if (index == 0){
            return this.getAnswer1();
        }
        else if (index == 1){
            return this.getAnswer2();
        }
        else if (index == 2){
            return this.getAnswer3();
        }
        else if (index == 3){
            return this.getAnswer4();
        }
        else{
            return this.getAnswer5();
        }
    }
}
